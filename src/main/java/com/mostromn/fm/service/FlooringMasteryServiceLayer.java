/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.service;

import com.mostromn.fm.dao.FlooringMasteryDao;
import com.mostromn.fm.dao.FlooringMasteryDaoException;
import com.mostromn.fm.dto.Order;
import com.mostromn.fm.dto.Product;
import com.mostromn.fm.dto.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author nicholasmostrom
 */
public class FlooringMasteryServiceLayer {

    FlooringMasteryDao dao;
    LocalDate date = LocalDate.now();
    //Initializing the dependency injection
    public FlooringMasteryServiceLayer(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    //To get the word production to be true (which it alwsys is in the file
    //This is how we can initilize  some of the methods
    public boolean getConfiguration() throws FlooringMasteryDaoException {
        boolean isProduction = false;

        try {

            String production = dao.openConfig();
            if ("production".equalsIgnoreCase(production)) {
                isProduction = true;
            }
        } catch (FlooringMasteryDaoException e) {

        }

        return isProduction;
    }

    //Grabs from the products list to return to user
    public List<Product> returnProducts() throws FlooringMasteryDaoException {

        List<Product> products = dao.loadProductInfo();
        return products;

    }

    //Grabs from orders list and returns orders
    public List<Order> displayOrders(LocalDate date) throws FlooringMasteryDaoException {

        List<Order> orders = dao.getOrdersByDate(date);
        return orders;

    }

    //Calculating the cost using the current order as a parameter
    //setting area,material, labor, tax and total of cost to bid decimal after retrieving the input
    public void calculateCost(Order currentOrder) {

        BigDecimal area = currentOrder.getArea();
        BigDecimal materialCost = (area.multiply(currentOrder.getProduct().getCostPerSqFt())).setScale(2);
        BigDecimal laborCost = (area.multiply(currentOrder.getProduct().getLaborCostPerSqFt()));
        BigDecimal tax = laborCost.add(materialCost).multiply(currentOrder.getState().getTaxRate().movePointLeft(2));
        BigDecimal total = materialCost.add(laborCost).add(tax);

        //using half_up for rounding to get the (nearest neighbor)
        //Setting each price to the object
        currentOrder.setMaterialCost(materialCost.setScale(2, RoundingMode.HALF_UP));
        currentOrder.setLaborCost(laborCost.setScale(2, RoundingMode.HALF_UP));
        currentOrder.setTax(tax.setScale(2, RoundingMode.HALF_UP));
        currentOrder.setTotal(total.setScale(2, RoundingMode.HALF_UP));
    }

    //using the current order again, once the state has been selected or entered, we will set currentState to that selection
    //Then retrieving the ifo from the states list for taxes..etc
    public void getStateInfo(Order currentOrder)
            throws FlooringMasteryDaoException, InvalidStateException {

        State currentState = currentOrder.getState();

        List<State> states = dao.loadStateInfo();

        //Set the state to 0 to start off with, then increment up by one with i++
        //use  for loop, for each state abbreaviation entered, get the ifo and set the tax rate 
        //If it is an invalid state, exception will catch it and will be thrown.
        for (int i = 0; i < states.size(); i++) {

            if (currentState.getStateAbb().toUpperCase().equals(states.get(i).getStateAbb())) {
                currentOrder.getState().setTaxRate(states.get(i).getTaxRate());
                break;
            } else if (i == states.size() - 1) {
                throw new InvalidStateException(
                        "Invalid State.");
            }

        }
    }

    //Use validate answer to make sure all properties entered are what they are supposed to be
    public boolean validateAnswer(String answer) {
        boolean keepPlaying = false;
        if ("yes".equalsIgnoreCase(answer)) {
            keepPlaying = true;
        }

        return keepPlaying;
    }

    //to remove order a date must be entered, so the parameter is set with the date and throws the exception
    //a try-catch loop with the orders list, for the order entered to be correct, the order number must be entered
    //if any of the dates or numbers are incorrect then ity will throw out the message 
    public void removeOrder(LocalDate removalDate, int orderNum) throws InvalidDateException {

        try {

            List<Order> orders = dao.getOrdersByDate(removalDate);
            for (int i = 0; i < orders.size(); i++) {
                if (orderNum == orders.get(i).getOrderNumber()) {
                    dao.removeOrder(orders.get(i), removalDate);
                }else if(i == orders.size() - 1){
                    throw new InvalidDateException ("Order does not exist");
                }
            }

        } catch (FlooringMasteryDaoException e) {
            throw new InvalidDateException("The date you entered does not contain any orders.");
        }

    }

    //has current order in parameter to get the info 
    //if the product asked for is correct, then the if-else if loop will start and it will get all the information of teh product with the .get
    public void getProductInfo(Order currentOrder, List<Product> products) throws InvalidProductException {

        Product currentProduct = currentOrder.getProduct();

        for (int i = 0; i < products.size(); i++) {

            if (currentProduct.getType().equalsIgnoreCase(products.get(i).getType())) {
                currentOrder.getProduct().setCostPerSqFt(products.get(i).getCostPerSqFt());
                currentOrder.getProduct().setLaborCostPerSqFt(products.get(i).getLaborCostPerSqFt());
                break;
            } else if (i == products.size() - 1) {
                throw new InvalidProductException(
                        "Invalid Product.");
            }
        }
    }

    //For the determine order number, there will be maxNumber already instatiated at 0 so it is not possible to recieve 
    //then it will use a for loop to see if the order has been entered correctly and has been counted by the program
    //it will get all properties and assign it with crrentOrder.setOrderNumber(maxNumber +1)
    public void determineOrderNumber(Order currentOrder) {
        Integer maxNumber = 0;
        try {
            List<Order> orders = dao.getOrdersByDate(date);
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getOrderNumber() > maxNumber) {
                    maxNumber = orders.get(i).getOrderNumber();
                }

            }
            currentOrder.setOrderNumber(maxNumber + 1);

        } catch (FlooringMasteryDaoException e) {
            currentOrder.setOrderNumber(1001);
        }
    }

    //for get order using parameters of date and order num
    //and setting edited order to a new order so it can be edited
    //Then from the list if the get ordered number is equal to the order num, then edited order will equal to be the current one
    //Any other possibility of invalid date or order number, exception of invalid date is thrown
    public Order getOrder(LocalDate date, int orderNum) throws InvalidDateException {
        Order editedOrder = new Order();
        try {
            List<Order> orders = dao.getOrdersByDate(date);

            for (Order order : orders) {
                if (order.getOrderNumber() == orderNum) {
                    editedOrder = order;
                }
            }

            if (editedOrder.getOrderNumber() == null) {
                throw new InvalidDateException("That order does not exist.");

            }
        } catch (FlooringMasteryDaoException e) {
            throw new InvalidDateException(
                    "Order Number: " + orderNum + " does not exist for that date");
        }

        return editedOrder;
    }

    //Using the dao methd to add order to file
    public void addOrderToFile(Order order) {

        try {
            dao.addToFile(order, date);

        } catch (FlooringMasteryDaoException e) {

        }
    }

    //This is a method similiar to above only thsi si for the creation of the file
    public void addOrderToFile(Order order, LocalDate date) {

        try {

            dao.addToFile(order, date);

        } catch (FlooringMasteryDaoException e) {

        }
    }

}
