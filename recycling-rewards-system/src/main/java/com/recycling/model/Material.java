package com.recycling.model;

import java.math.BigDecimal;

public class Material {

    private final int materialId;
    private final String materialName;
    private final BigDecimal pointsPerKg;

    public Material(int materialId, String materialName, BigDecimal pointsPerKg) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.pointsPerKg = pointsPerKg;
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public BigDecimal getPointsPerKg() {
        return pointsPerKg;
    }
}