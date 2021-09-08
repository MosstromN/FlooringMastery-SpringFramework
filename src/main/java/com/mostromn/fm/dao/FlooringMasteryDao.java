/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.dao;

import com.mostromn.fm.dto.Order;
import com.mostromn.fm.dto.Product;
import com.mostromn.fm.dto.State;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author nicholasmostrom
 */
public interface FlooringMasteryDao {

    //All of the Dao Impl methods or CRUD methods put here 
    //Making sure parameteres and Exceptions line up from btoh sides of method 
    public String openConfig()
            throws FlooringMasteryDaoException;

    
    public List<Order> getOrdersByDate(LocalDate date)
            throws FlooringMasteryDaoException;

    
    public List<State> loadStateInfo()
            throws FlooringMasteryDaoException;

    
    public void removeOrder(Order currentOrder, LocalDate date)
            throws FlooringMasteryDaoException;

    
    public List<Product> loadProductInfo()
            throws FlooringMasteryDaoException;

    
    public Order addToFile(Order order, LocalDate date)
            throws FlooringMasteryDaoException;

}

