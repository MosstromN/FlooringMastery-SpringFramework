/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.dao;

import com.mostromn.fm.dto.Order;
import com.mostromn.fm.dto.Product;
import com.mostromn.fm.dto.State;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author nicholasmostrom
 */
public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao {
    private Order onlyOrder;
    private List<Order> orderList = new ArrayList<>();
    private List<State> stateList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    
    //Implementing all the lists 
    //Setting an order to a basic numbering to test all results possible
    public FlooringMasteryDaoStubImpl() {
        onlyOrder = new Order();
        State onlyState = new State();
        Product onlyProduct = new Product();
        onlyOrder.setOrderNumber(1003);
        onlyOrder.setCustomerName("Nick Mostrom");
        onlyOrder.setArea(new BigDecimal("350.00"));
        onlyOrder.setState(onlyState);
        onlyOrder.getState().setStateAbb("MN");
        onlyOrder.getState().setTaxRate(new BigDecimal("6.88"));
        onlyOrder.setProduct(onlyProduct);
        onlyOrder.getProduct().setType("Tile");
        onlyOrder.getProduct().setCostPerSqFt(new BigDecimal("3.50"));
        onlyOrder.getProduct().setLaborCostPerSqFt(new BigDecimal("4.10"));
        onlyOrder.setMaterialCost(new BigDecimal("1000.00"));
        onlyOrder.setLaborCost(new BigDecimal("1200.00"));
        onlyOrder.setTax(new BigDecimal("120.00"));
        onlyOrder.setTotal(new BigDecimal("2320.00"));
        
        
        orderList.add(onlyOrder);
    }
    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws FlooringMasteryDaoException {
        return orderList;
    }
    //Using the list to test orders by date 

    @Override
    public List<State> loadStateInfo() throws FlooringMasteryDaoException {
        State mn = new State();
        mn.setStateAbb("MN");
        stateList.add(mn);
        return stateList;
    }
    //Setting state info ahead to MN
    
    @Override
    public void removeOrder(Order currentOrder, LocalDate date) throws FlooringMasteryDaoException {
       
    }

    @Override
    public List<Product> loadProductInfo() throws FlooringMasteryDaoException {
        Product tile = new Product();
        tile.setType("tile");
        productList.add(tile);
        return productList;
    }

    @Override
    public Order addToFile(Order order, LocalDate date) throws FlooringMasteryDaoException {
    return null;    
    }

    @Override
    public String openConfig() throws FlooringMasteryDaoException {
        
        return "testing";
       
    }
    
}
