package com.nutrition.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Allergy implements Serializable {
    private static final long serialVersionUID = 1L;

    private int allergyId;
    private String allergyName;
    private Timestamp createdAt;

    public Allergy() {}

    public Allergy(int allergyId, String allergyName) {
        this.allergyId = allergyId;
        this.allergyName = allergyName;
    }

    public int getAllergyId() { return allergyId; }
    public void setAllergyId(int allergyId) { this.allergyId = allergyId; }

    public String getAllergyName() { return allergyName; }
    public void setAllergyName(String allergyName) { this.allergyName = allergyName; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
