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
public class Product {
    
    String type;
    BigDecimal costPerSqFt;
    BigDecimal laborCostPerSqFt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCostPerSqFt() {
        return costPerSqFt;
    }

    public void setCostPerSqFt(BigDecimal costPerSqFt) {
        this.costPerSqFt = costPerSqFt;
    }

    public BigDecimal getLaborCostPerSqFt() {
        return laborCostPerSqFt;
    }

    public void setLaborCostPerSqFt(BigDecimal laborCostPerSqFt) {
        this.laborCostPerSqFt = laborCostPerSqFt;
    }
    
}
