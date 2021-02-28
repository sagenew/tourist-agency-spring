package com.touristagency.entity.enums;

public enum OrderStatus {
    PENDING("Pending"),
    PAID("Paid"),
    DENIED("Denied");

    private final String simpleName;

    OrderStatus(String simpleName) {
        this.simpleName = simpleName;
    }

    @Override
    public String toString() {
        return simpleName;
    }
}
