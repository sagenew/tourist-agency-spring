package com.touristagency.entity.enums;

public enum HotelType {
    APARTMENTS("Apartments"),
    LODGE("Lodge"),
    RESORT("Resort");

    private final String simpleName;

    HotelType(String simpleName) {
        this.simpleName = simpleName;
    }

    @Override
    public String toString() {
        return simpleName;
    }
}
