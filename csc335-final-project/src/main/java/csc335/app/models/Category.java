package csc335.app.models;

/**
 * Author(s): Genesis Benedith
 * File: Category.java
 * Description: Model enum that represents an expense category
 */

public enum Category {
    FOOD,
    ENTERTAINMENT,
    TRANSPORTATION,
    UTILITIES,
    HEALTHCARE,
    OTHER;

    /**
     * 
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
