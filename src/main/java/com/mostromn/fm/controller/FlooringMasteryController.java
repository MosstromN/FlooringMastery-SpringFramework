/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.controller;

import com.mostromn.fm.dao.FlooringMasteryDaoException;
import com.mostromn.fm.service.InvalidStateException;
import com.mostromn.fm.dto.Order;
import com.mostromn.fm.dto.Product;
import com.mostromn.fm.service.FlooringMasteryServiceLayer;
import com.mostromn.fm.service.InvalidDateException;
import com.mostromn.fm.service.InvalidProductException;
import com.mostromn.fm.ui.FlooringMasteryView;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author nicholasmostrom
 */
public class FlooringMasteryController {
//Class instantiation
    FlooringMasteryView view;
    FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }
//Making the run method fro the App class
    public void run() {

        boolean production = openConfig();

        do {

            switch (getMenuSelection()) {
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder(production);
                    break;
                case 3:
                    editOrder(production);
                    break;
                case 4:
                    removeOrder(production);
                    break;
                case 5:
                    exitMessage();
                    System.exit(0);
                default:
                    unknownCommand();
            }
//Each Case calling a new method written down below 
        } while (keepGoing());

        exitMessage();
    }


    private int getMenuSelection() {

        return view.printMenuAndGetSelection();
    }

    
    //Calling from the view class to see if its worth to keep going from the user 
    //then returning a service layer method to make sure teh answer is either "yes" or "no"
    private boolean keepGoing() {
        String answer = view.keepGoing();
        return service.validateAnswer(answer);
    }

    
    
    //Have Local date set to date, and accessing the get date method from the user in the view 
    //Then listing all the orders from that date called and displaying them
    //Catching the Dao Exception incase the date was wrongly entered 
    private void displayOrders() {
        try {
            LocalDate date = view.getDate();
            List<Order> orders = service.displayOrders(date);
            view.displayOrders(orders);

        } catch (FlooringMasteryDaoException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    
    
    //First display the add banner 
    //then setting the products list equal to products the name and to the method in the service layer return products
    //Then setting currentOrder to a new Order(); and setting a do-while loop around teh basis if current order is equal to null
    //if current order is equal to null, (which it is since its empty, then the question of name and area come up
    private void addOrder(boolean production) {
        view.displayAddBanner();
        try {
            
            List<Product> products = service.returnProducts();
            Order currentOrder = new Order();
            do {
                
                currentOrder = view.getUserNameAndArea();
            } while (currentOrder == null);
           
    //Setting a boolean of valid state to false , then they must get asked the same question until answered correctly 
    //Which is just asking for the state, getting the info from the file of the state (taxes etc..) and making sure its the correct Abrv
    //Catching the invalid state exception which would send the user right back to the beggining of the state question
            boolean validState = false;
            do {
                try {

                    currentOrder = view.getUserState(currentOrder);
                    service.getStateInfo(currentOrder);
                    validState = true;

                } catch (InvalidStateException e) {

                    view.displayErrorMessage(e.getMessage());

                }
            } while (validState == false);

    //Setting a boolean of valid product to false , then they must get asked the same question until answered correctly 
    //Which is just asking for the product, getting the info from the file of the product (cost,type etc..) and making sure its the correct selection number
    //Catching the invalid product exception which would send the user right back to the beggining of the product question
            boolean validProduct = false;

            do {
                try {

                    currentOrder = view.getUserProduct(currentOrder, products);
                    service.getProductInfo(currentOrder, products);
                    validProduct = true;

                } catch (InvalidProductException e) {

                    view.displayErrorMessage(e.getMessage());
                }

            } while (validProduct == false);

            
    //setting the answer to the service layer method validateAnswer to make sure user wants the purchase, and then placing the order 
    //if the answer is true, calculate cost method and order numnber methdo are clalled to give the user the following 
            boolean answer = service.validateAnswer(view.placeOrder(currentOrder));
            if (answer == true) {

                service.calculateCost(currentOrder);
                service.determineOrderNumber(currentOrder);
                if (production == true) {
    //if production is true, which is on the open config file so it has to be, then it will add the order to a new or existing file depending on the date 
    //if production isnt true, which it should be unless declined by the user, then the message from the view class will come up about nto saving 
    //catching the Dao Exception since there could be many spelling errors 
                    service.addOrderToFile(currentOrder);
                }
            } else {
                view.didNotSave();
            }

        } catch (FlooringMasteryDaoException e) {

            view.displayErrorMessage(e.getMessage());

        }
    }

    
    
    //First displaying the edit banner
    //Using a try - catch loop, well set LocalDate Time API to date, and get the date from the view asking the user 
    //and well ask the user for the order number which is an integer, and set it to int
    //Then using the service method to retrieve the order, and setting edited order to everything already listed plus the property they selected to edit
   
    private void editOrder(boolean production) {

        view.displayEditBanner();

        try {

            LocalDate date = view.getDate();
            int orderNum = view.getOrderNum();
            Order editOrder = service.getOrder(date, orderNum);
            Order editedOrder = view.determineEditedProperty(editOrder);
            //If edited order is not nothing, then all the calculations for the state, square footage, and price will change 
            //Since production should be true afetr asking the user, then the order will get added to the file through service layer method
            //Then displaying success banner
            //catching all the exceptions since the editing property involves all the aspects 
            if (editedOrder != null) {
                service.getStateInfo(editedOrder);
                List<Product> products = service.returnProducts();
                service.getProductInfo(editedOrder, products);
                service.calculateCost(editedOrder);
                if (production == true) {
                    service.addOrderToFile(editedOrder, date);
                    view.displayEditSuccess(editedOrder);
                }
            }
        } catch (InvalidDateException e) {

            view.displayErrorMessage(e.getMessage());

        } catch (FlooringMasteryDaoException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (InvalidProductException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (InvalidStateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    
    
    //First display the banner, then set orderNum to 0 to initialize
    //then a try-catch loop trying to get the date with service layer
    // then get the order number 
    //then get the exact order 
    //if the order is correct then it will take the order from the file using deleteAnswer
    //Then if production is true, which it alwsays should be then the remove order success banner and service layer method are used 
    //Only if "yes" is typed in 
    private void removeOrder(boolean production) {

        view.displayRemoveBanner();
        int orderNum = 0;

        try {
            LocalDate date = view.getDate();
            orderNum = view.getOrderNum();
            Order removeOrder = service.getOrder(date, orderNum);
            String answer = view.getDeleteAnswer(removeOrder);
            if ("yes".equals(answer)) {
                if(production == true){
                service.removeOrder(date, orderNum);
                view.removeOrderSuccess(removeOrder);
                }
            }

        } catch (InvalidDateException e) {

            view.displayErrorMessage(e.getMessage());

        }

    }

    private void unknownCommand() {

        view.displayUnknownCommand();

    }

    private void exitMessage() {

        view.displayExitMessage();

    }
    
    
        private boolean openConfig() {

        boolean production = false;
        try {

            production = service.getConfiguration();

        } catch (FlooringMasteryDaoException e) {
            view.displayErrorMessage(e.getMessage());

        }

        return production;
    }
}
