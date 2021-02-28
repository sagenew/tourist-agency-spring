package com.touristagency.entity.enums;

public enum TourType {
    RECREATION("Recreation"),
    EXCURSION("Excursion"),
    SHOPPING("Shopping");

    private final String simpleName;

    TourType(String simpleName) {
        this.simpleName = simpleName;
    }

    @Override
    public String toString() {
        return simpleName;
    }
}
