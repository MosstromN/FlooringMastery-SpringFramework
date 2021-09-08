/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.ui;

import com.mostromn.fm.dto.Order;
import com.mostromn.fm.dto.Product;
import com.mostromn.fm.dto.State;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 *
 * @author nicholasmostrom
 */
public class FlooringMasteryView {
    
    private UserIO io;
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMDDYYYY");
    
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    public LocalDate getDate() {
        
        return io.readDate("\nPlease enter the date of the order(MM-DD-YYYY): ");
        
    }
    
    public Order determineEditedProperty(Order currentOrder) {
        if (currentOrder != null) {
            io.print("\nOrder Number: " + currentOrder.getOrderNumber()
                    + "\n[1]Name:  " + currentOrder.getCustomerName()
                    + "\n[2]State: " + currentOrder.getState().getStateAbb()
                    + "\n[3]Flooring Area: " + currentOrder.getArea()
                    + "\n[4]Type: " + currentOrder.getProduct().getType());
            
            switch (io.readInt("Enter value [] associated with the property you want to edit.", 1, 4)) {
                case 1:
                    String editedName = io.readString("Enter new name: ");
                    if (currentOrder.getCustomerName().equalsIgnoreCase(editedName)) {
                        currentOrder = null;
                    } else {
                        currentOrder.setCustomerName(editedName);
                    }
                    break;
                case 2:
                    String editedState = io.readString("Enter new state (ex: MN for Minnesota)");
                    if (currentOrder.getState().getStateAbb().equalsIgnoreCase(editedState)) {
                        currentOrder = null;
                    } else {
                        currentOrder.getState().setStateAbb(editedState);
                    }
                    break;
                case 3:
                    boolean valid = true;
                    do {
                        valid = true;
                        try {
                            BigDecimal editedArea = new BigDecimal(io.readString("Enter new area: "));
                            if (editedArea.equals(currentOrder.getArea())) {
                                currentOrder = null;
                            } else {
                                currentOrder.setArea(editedArea);
                            }
                        } catch (NumberFormatException e) {
                            io.print("Invalid number.");
                            valid = false;
                        }
                    } while (valid = false);
                    break;
                case 4:
                    String editedType = io.readString("Enter new type: ");
                    if (currentOrder.getProduct().getType().equalsIgnoreCase(editedType)) {
                        currentOrder = null;
                    } else {
                        currentOrder.getProduct().setType(editedType);
                    }
                    break;
                default:
                    currentOrder = null;
                    break;
            }
        }
        return currentOrder;
    }
    
    public String getDeleteAnswer(Order removeOrder) {
        return io.readString("(answer yes or no)Are you sure you want to delete Order: " + removeOrder.getOrderNumber());
    }
    
    public Integer getOrderNum() {
        
        return io.readInt("Enter the order number: ");
        
    }
    
    public void displayEditSuccess(Order currentOrder){
        io.print("Successfully edited order: " + currentOrder.getOrderNumber());
    }
    
    public void removeOrderSuccess(Order orderNum) {
        io.print("\nSuccessfully Removed Order: " + orderNum.getOrderNumber());
        
    }

    public void displayUnknownCommand() {
        io.print("\nUnknown Command, would you like to return to main menu? (yes/no)");
    }
    
    public String keepGoing() {
        return io.readString("\nWould you like to return to the Main Menu? (yes/no)");
    }
    
    public void displayExitMessage() {
        
        io.print("Thank You! Goodbye.");
        
    }
    
    public void displayErrorMessage(String errorMsg) {
        io.print("     _________ERROR________");
        io.print(errorMsg);
    }
    
    public void displayOrders(List<Order> orders) {
        
        for (Order currentOrder : orders) {
            
            io.print(currentOrder.getOrderNumber() + " - "
                    + currentOrder.getCustomerName() + " - "
                    + currentOrder.getState().getStateAbb() + " - "
                    + currentOrder.getState().getTaxRate() + " - "
                    + currentOrder.getProduct().getType() + " - "
                    + currentOrder.getProduct().getCostPerSqFt() + " - "
                    + currentOrder.getProduct().getLaborCostPerSqFt() + " - "
                    + currentOrder.getArea() + " - "
                    + currentOrder.getMaterialCost() + " - "
                    + currentOrder.getLaborCost() + " - "
                    + currentOrder.getTax() + " - "
                    + currentOrder.getTotal());
        }
    }
    
    public int printMenuAndGetSelection() {
        io.print("\n________Main Menu________");
        io.print("\n[1] List Orders By Date");
        io.print("[2] Add an Order");
        io.print("[3] Edit an Order");
        io.print("[4] Delete an Order");
        io.print("[5] Close");
        
        return io.readInt("\nPlease make a slection", 1, 5);
    }
    
    public Order getUserState(Order currentOrder) {
        State currentState = new State();
        currentOrder.setState(currentState);
        String state = io.readString("Next enter your State (Abbreviation ex: MN for Minnesota)");
        currentOrder.getState().setStateAbb(state);
        return currentOrder;
    }
    
    public Order getUserProduct(Order currentOrder, List<Product> products) {
        
        Product currentProduct = new Product();
        currentOrder.setProduct(currentProduct);
        
        io.print("\n________Flooring Options________");
        
        for (int i = 0; i < products.size(); i++) {
            
            io.print("[" + (i + 1) + "] Type: " + products.get(i).getType()
                    + " Cost/sqft: " + products.get(i).getCostPerSqFt()
                    + " Labor Cost/sqft: " + products.get(i).getLaborCostPerSqFt());
            
        }
        
        int getType = io.readInt("\n Please enter the number associated with the flooring you want.", 1, products.size());
        String type = products.get(getType - 1).getType();
        currentOrder.getProduct().setType(type);
        return currentOrder;
    }
    
    public Order getUserNameAndArea() {
        
        Order currentOrder = new Order();
        String customerName = io.readString("Please enter your first and last name:");
        
        try {
            BigDecimal area = new BigDecimal(io.readString("Area of the floor you want to cover: "));
            currentOrder.setArea(area);
        } catch (NumberFormatException e) {
            io.print("Invalid input please re-enter your name and Area");
            return null;
        }
        
        currentOrder.setCustomerName(customerName);
        return currentOrder;
    }
    
    public String placeOrder(Order currentOrder) {
        io.print("     ________Order Overview________"
                + "\nName:  " + currentOrder.getCustomerName()
                + "\nState: " + currentOrder.getState().getStateAbb()
                + "\nFlooring Area: " + currentOrder.getArea()
                + "\nType: " + currentOrder.getProduct().getType());
        
        return io.readString("Sumbit this information? (yes/no)");
    }
    
    public void displayRemoveBanner() {
        io.print("     ________Remove Order________");
    }
    
    public void didNotSave() {
        io.readString("Did not save, please hit enter");
    }
    
    public void displayEditBanner() {
        io.print("     ________Edit Order________");
    }
    
    public void displayAddBanner() {
        io.print("     ________Create Order________");
    }
}
