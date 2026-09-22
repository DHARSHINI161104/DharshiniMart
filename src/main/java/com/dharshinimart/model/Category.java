package com.dharshinimart.model;

public enum Category {
    FRUITS("Fruits"),
    VEGETABLES("Vegetables"),
    RICE_GRAINS("Rice & Grains"),
    DALS_PULSES("Dals & Pulses"),
    COOKING_ESSENTIALS("Cooking Essentials"),
    SPICES_MASALA("Spices & Masala"),
    DAIRY_EGGS("Dairy & Eggs"),
    BEVERAGES("Beverages"),
    SNACKS("Snacks"),
    PERSONAL_CARE("Personal Care"),
    HOUSEHOLD("Household");

    private final String displayName;
    Category(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
