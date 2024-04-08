package com.balaji.calculator;

public class PhysicalAuditItem {
    String name;
    int piece;

    public PhysicalAuditItem(String name, int piece) {
        this.name = name;
        this.piece = piece;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }
}
