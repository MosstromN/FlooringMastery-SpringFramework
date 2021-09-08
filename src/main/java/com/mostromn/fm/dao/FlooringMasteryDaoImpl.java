/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.dao;

import com.mostromn.fm.dto.Order;
import com.mostromn.fm.dto.Product;
import com.mostromn.fm.dto.State;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author nicholasmostrom
 */

public class FlooringMasteryDaoImpl implements FlooringMasteryDao {

    private List<State> statesList = new ArrayList();
    private Map<String, Product> products = new HashMap<>();
    private Map<String, State> states = new HashMap<>();
    private Map<Integer, Order> orders = new HashMap<>();
    public static final String STATES_FILE = "states.txt";
    public static final String PRODUCTS_FILE = "products.txt";
    public static final String CONFIG_FILE = "config.txt";
    public static final String DELIMITER = ",";

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws FlooringMasteryDaoException {

        String ordersByDate = "Orders_" + date.toString() + ".txt";
//        orders.clear();
        loadOrders(ordersByDate);
        return new ArrayList<Order>(orders.values());
        //Have ordersByDate set to the date.toString
        //and then lod the orders by the date or name of the object instatieted above
        //Ultimately returning the orders from the Array List to the user
    }

    @Override
    public Order addToFile(Order order, LocalDate date) throws FlooringMasteryDaoException {
        Order newOrder = orders.put(order.getOrderNumber(), order);
        writeFile(date);
        return newOrder;
        //To add an order to a new file, must put the order as a new order and use the 
        // ( . put ) to place in List
    }

    @Override
    public void removeOrder(Order currentOrder, LocalDate date) throws FlooringMasteryDaoException {

        orders.remove(currentOrder.getOrderNumber(), currentOrder);
        writeFile(date);
        // using the current order and date to get ahold of the file holding the order information,
        //it will remove the order and write the file back with one less order if executed
    }

    @Override
    public String openConfig() throws FlooringMasteryDaoException {
        return loadConfig();
         //Open the Config to open the word production 
    }

    @Override
    public List<State> loadStateInfo() throws FlooringMasteryDaoException {
        loadStates();
        return new ArrayList<State>(states.values());
        //Loads Array List from the file states.txt (Uses the . values to get all comopenents)
    }

    @Override
    public List<Product> loadProductInfo() throws FlooringMasteryDaoException {
        loadProducts();
        return new ArrayList<Product>(products.values());
         //Loading products from Array list named products (the . values gets all values for users benefit)
    }
    
//Similar to other projects 
    private void writeFile(LocalDate date) throws FlooringMasteryDaoException {
        File file = new File("Orders_" + date.toString() + ".txt");
        PrintWriter out;
        //With the added new File ("orders" dato to string with the txt to make a new file 
        //Making sure a new file is created to each order 
        try {
            out = new PrintWriter(new FileWriter("Orders_" + date + ".txt"));
        } catch (IOException e) {
            throw new FlooringMasteryDaoException(
                    "Could not save items.", e);
        }

        
        
       List <Order> orderList = this.getOrdersByDate(date);
        for (Order currentOrder : orderList) 
                                            {
            out.println(currentOrder.getOrderNumber() + DELIMITER
                    + currentOrder.getCustomerName() + DELIMITER
                    + currentOrder.getState().getStateAbb() + DELIMITER
                    + currentOrder.getState().getTaxRate() + DELIMITER
                    + currentOrder.getProduct().getType() + DELIMITER
                    + currentOrder.getProduct().getCostPerSqFt() + DELIMITER
                    + currentOrder.getProduct().getLaborCostPerSqFt() + DELIMITER
                    + currentOrder.getArea() + DELIMITER
                    + currentOrder.getMaterialCost() + DELIMITER
                    + currentOrder.getLaborCost() + DELIMITER
                    + currentOrder.getTax() + DELIMITER
                    + currentOrder.getTotal());
            
            out.flush();
            }
        //close for clean up
        out.close();

    }

    private String loadConfig() throws FlooringMasteryDaoException {
        Scanner scanner;
        String currentLine = "";
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(CONFIG_FILE)));
        } catch (FileNotFoundException e) {

            throw new FlooringMasteryDaoException("");
        }
        while (scanner.hasNextLine()) {

            currentLine = scanner.nextLine();

        }

        return currentLine;

    }

        private void loadProducts() throws FlooringMasteryDaoException {

        //This Method is desgned to reach from the products file 
        // And get information on the Type of Flooring, price of flooring and labor cost
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCTS_FILE)));

        } catch (FileNotFoundException e) {
            throw new FlooringMasteryDaoException(
                    "-_- Could not load items data into memory.", e);
        }

        String currentLine;

        String[] currentTokens;

        while (scanner.hasNextLine()) {

            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            Product currentProduct = new Product();

            currentProduct.setType(currentTokens[0]);
            currentProduct.setCostPerSqFt(new BigDecimal(currentTokens[1]));
            currentProduct.setLaborCostPerSqFt(new BigDecimal(currentTokens[2]));
            products.put(currentProduct.getType(), currentProduct);
        }
        //When all finished, close the scanner.
        scanner.close();
    }
        
        
    private void loadOrders(String date) throws FlooringMasteryDaoException {
        Scanner scanner;
         // To load the ordered flloring MAstery pieces must open the scanner to get the date
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(new BufferedReader(new FileReader(date)));

        } catch (FileNotFoundException e) {

            throw new FlooringMasteryDaoException("");

        }

        String currentLine;

        String[] currentTokens;

        while (scanner.hasNextLine()) {

            //Then with eachline creating a token for each data point entry 
            //Such as Or #, Name, Product, Area, Cost etc..
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            Order currentOrder = new Order();

            currentOrder.setOrderNumber(Integer.parseInt(currentTokens[0]));
            currentOrder.setCustomerName(currentTokens[1]);

            State currentState = new State();
            currentState.setStateAbb(currentTokens[2]);
            currentState.setTaxRate(new BigDecimal(currentTokens[3]));
            currentOrder.setState(currentState);

            Product currentProduct = new Product();
            currentProduct.setType(currentTokens[4]);
            currentProduct.setCostPerSqFt(new BigDecimal(currentTokens[5]));
            currentProduct.setLaborCostPerSqFt(new BigDecimal(currentTokens[6]));
            currentOrder.setProduct(currentProduct);

            currentOrder.setArea(new BigDecimal(currentTokens[7]));
            currentOrder.setMaterialCost(new BigDecimal(currentTokens[8]));
            currentOrder.setLaborCost(new BigDecimal(currentTokens[9]));
            currentOrder.setTax(new BigDecimal(currentTokens[10]));
            currentOrder.setTotal(new BigDecimal(currentTokens[11]));

            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        //When all finished, close the scanner.
        scanner.close();

    }

    private void loadStates() throws FlooringMasteryDaoException {

        //Loading the states file with the abbreviations 
        // And tax implements included 
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(new BufferedReader(new FileReader(STATES_FILE)));

        } catch (FileNotFoundException e) {
            throw new FlooringMasteryDaoException(
                    "-_- Could not load items data into memory.", e);
        }

        String currentLine;

        String[] currentTokens;

        while (scanner.hasNextLine()) {

            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            State currentState = new State();

            currentState.setStateAbb(currentTokens[0]);
            currentState.setTaxRate(new BigDecimal(currentTokens[1]));

            states.put(currentState.getStateAbb(), currentState);
        }
        //When all finished, close the scanner.
        scanner.close();
    }


}
