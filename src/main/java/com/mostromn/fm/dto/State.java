/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mostromn.fm.dto;

import java.math.BigDecimal;

/**
 *
 * @author nicholasmostrom
 */
public class State {
    
    String stateAbb;
    BigDecimal taxRate;

    public String getStateAbb() {
        return stateAbb;
    }

    public void setStateAbb(String stateAbb) {
        this.stateAbb = stateAbb;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
}