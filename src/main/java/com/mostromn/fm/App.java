/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm;

import com.mostromn.fm.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author nicholasmostrom
 */
public class App {

    public static void main(String[] args) {

        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringMasteryController controller
                = ctx.getBean("controller", FlooringMasteryController.class);
        controller.run();
    }
}

//The App class was origionally constructed with dependency beofre putting in the 
//Spring frame work through the applicationContext file, using dependency within there