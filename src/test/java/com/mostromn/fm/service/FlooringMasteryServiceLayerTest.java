/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.service;

import com.mostromn.fm.dao.FlooringMasteryDao;
import com.mostromn.fm.dao.FlooringMasteryDaoException;
import com.mostromn.fm.dao.FlooringMasteryDaoStubImpl;
import com.mostromn.fm.dto.Order;
import com.mostromn.fm.dto.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author nicholasmostrom
 */
public class FlooringMasteryServiceLayerTest {

    FlooringMasteryServiceLayer service;
    FlooringMasteryDao dao;

    public FlooringMasteryServiceLayerTest() {
        dao = new FlooringMasteryDaoStubImpl();
        service = new FlooringMasteryServiceLayer(dao);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of returnProducts method, of class FlooringMasteryServiceLayer.
     *
     * @throws com.mostromn.fm.dao.FlooringMasteryDaoException
     */
    @Test
    public void testCalculateCost() throws FlooringMasteryDaoException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<Order> testOrders = dao.getOrdersByDate(LocalDate.parse("01-01-2000", formatter));
        testOrders.get(0).setArea(new BigDecimal("500.00"));
        Order testOrder = testOrders.get(0);
        service.calculateCost(testOrder);

        assertNotEquals(new BigDecimal("0"), testOrder.getTotal());
        assertEquals(new BigDecimal("1750.00"), testOrder.getMaterialCost());

    }

    @Test
    public void testValidateAnswer() {
        boolean answer = service.validateAnswer("yes");
        assertEquals(true, answer);
    }

    /**
     * Test of removeOrder method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testRemoveOrder() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<Order> orders = dao.getOrdersByDate(LocalDate.parse("01-01-2000", formatter));
        Order removeOrder = orders.get(0);
        int orderNum = removeOrder.getOrderNumber();
        service.removeOrder(LocalDate.parse("01-01-2018", formatter), orderNum);
        List<Order> updatedOrders = dao.getOrdersByDate(LocalDate.parse("01-01-2000", formatter));
        assertEquals(1, updatedOrders.size());
        
        try{
            service.removeOrder(LocalDate.parse("04-03-2018", formatter), 3456);
            fail("Expected InvalidDateException to be thrown");
        }catch(InvalidDateException e){
            
        }

    }

    @Test
    public void testDetermineOrderNumber() throws FlooringMasteryDaoException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<Order> currentOrder = dao.getOrdersByDate(LocalDate.parse("01-01-2018", formatter));
        Order resetOrderNum = currentOrder.get(0);
        resetOrderNum.setOrderNumber(0);
        service.determineOrderNumber(resetOrderNum);
        int newOrderNum = resetOrderNum.getOrderNumber();
        assertEquals(1, newOrderNum);
    }

    /**
     * Test of getOrder method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testGetOrder() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<Order> orders = dao.getOrdersByDate(LocalDate.parse("01-01-2000", formatter));
        try {
            service.getOrder(LocalDate.parse("01-01-2000", formatter), 1002);
            fail("should have thrown InvalidDateException");
        } catch (InvalidDateException e) {

        }

        try {
            service.getOrder(LocalDate.parse("01-01-2000", formatter), 1003);

        } catch (InvalidDateException e) {
            fail("Not expecting InvalidDateException");
        }

    }
   
     @Test
    public void testGetProductInfo() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<Order> orders = dao.getOrdersByDate(LocalDate.parse("01-01-2000", formatter));
        Order order = orders.get(0);
        List<Product> products = dao.loadProductInfo();
        try {
            service.getProductInfo(order, products);
            
        } catch (InvalidProductException e) {
            fail("not expecting InvalidDateException");
        }
        
        order.getProduct().setType("Aluminum");
        try {
            service.getProductInfo(order, products);
            fail("Should have thrown InvalidProductException");
        } catch (InvalidProductException e) {
            
        }
    }
    
     @Test
    public void testGetStateInfo() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<Order> orders = dao.getOrdersByDate(LocalDate.parse("01-01-2000", formatter));
        Order currentOrder = orders.get(0);
        try {
            service.getStateInfo(currentOrder);
           
        } catch (InvalidStateException e) {
             fail("not expecting InvalidStateException");
        }
        currentOrder.getState().setStateAbb("BB");
        try {
            service.getStateInfo(currentOrder);
             fail("Should have thrown InvalidStateException");
        } catch (InvalidStateException e) {
         
        }
    }

}
