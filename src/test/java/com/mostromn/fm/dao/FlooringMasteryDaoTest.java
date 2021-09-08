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
import java.time.format.DateTimeFormatter;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author nicholasmostrom
 */
public class FlooringMasteryDaoTest {
    
    private FlooringMasteryDao dao = new FlooringMasteryDaoImpl();
    
    public FlooringMasteryDaoTest() {
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
     * Test of getOrdersByDate method, of class FlooringMasteryDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOrdersByDate() throws Exception {
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<Order> orders = dao.getOrdersByDate(LocalDate.parse("09-06-2021", formatted));
        assertEquals(dao.getOrdersByDate(LocalDate.parse("09-06-2021", formatted)).size(), orders.size());
    }

    /**
     * Test of loadStateInfo method, of class FlooringMasteryDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testLoadStateInfo() throws Exception {
        
        assertEquals(50, dao.loadStateInfo().size());
    }

    /**
     * Test of removeOrder method, of class FlooringMasteryDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testRemoveOrder() throws Exception {
    

       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
       List<Order> orders = dao.getOrdersByDate(LocalDate.parse("09-06-2021", formatter));
       Order removeOrder = orders.get(0);
       dao.removeOrder(removeOrder, LocalDate.parse("09-06-2021", formatter));
       List<Order> updatedOrders = dao.getOrdersByDate(LocalDate.parse("09-06-2021", formatter));
       
       assertEquals(orders.size(), updatedOrders.size() + 1);
    }
    @Test
    public void testLoadProductInfo() throws Exception {
        
        assertNotEquals(0, dao.loadProductInfo().size());
    }

    //Testing all the information added to a fake file
    @Test
    public void testAddToFile() throws Exception {
        
        Order order = new Order();
        State state = new State();
        Product product = new Product();
        order.setOrderNumber(1003);
        order.setCustomerName("Nick Mostrom");
        order.setArea(new BigDecimal("350.00"));
        order.setState(state);
        order.getState().setStateAbb("MN");
        order.getState().setTaxRate(new BigDecimal("6.88"));
        order.setProduct(product);
        order.getProduct().setType("Tile");
        order.getProduct().setCostPerSqFt(new BigDecimal("3.50"));
        order.getProduct().setLaborCostPerSqFt(new BigDecimal("4.10"));
        order.setMaterialCost(new BigDecimal("1000.00"));
        order.setLaborCost(new BigDecimal("1200.00"));
        order.setTax(new BigDecimal("120.00"));
        order.setTotal(new BigDecimal("2320.00"));
        order.setOrderNumber(1012);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
       List<Order> before = dao.getOrdersByDate(LocalDate.parse("09-06-2021", formatter));
       dao.addToFile(order, LocalDate.parse("09-06-2021", formatter));
       List<Order> after = dao.getOrdersByDate(LocalDate.parse("09-06-2021", formatter));
       
       assertEquals(before.size(), after.size() - 1);
        
        
    }

   
    
}
