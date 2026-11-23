package com.recycling.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RecyclingTransaction {

    private final String transactionId;
    private final int residentId;
    private final Integer materialId;
    private final String transactionType;
    private final BigDecimal weightKg;
    private final int points;
    private final Timestamp transactionDate;

    public RecyclingTransaction(
            String transactionId,
            int residentId,
            Integer materialId,
            String transactionType,
            BigDecimal weightKg,
            int points,
            Timestamp transactionDate) {

        this.transactionId = transactionId;
        this.residentId = residentId;
        this.materialId = materialId;
        this.transactionType = transactionType;
        this.weightKg = weightKg;
        this.points = points;
        this.transactionDate = transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public int getResidentId() {
        return residentId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public int getPoints() {
        return points;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }
}