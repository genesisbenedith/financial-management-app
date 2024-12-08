package csc335.app.models;

// ----------------------------------------------------------------------------
// File: Category.java
// Author: Genesis Benedith
// Course: CSC 335 (Fall 2024)
// Description: This file defines the Category enum, representing the various 
//              categories of expenses and budgets in the personal financial 
//              assistant app. Each category is associated with default and 
//              hover color codes for GUI purposes.
// ----------------------------------------------------------------------------

/**
 * Enum to represent the categories of expenses and budgets tracked by the 
 * personal financial assistant app. Each category is assigned default and 
 * hover color codes for use in the app's GUI.
 * 
 * @author Genesis Benedith
 */
public enum Category {
    FOOD("#AFF8D8", "#DBFFD6"),
    ENTERTAINMENT("#A79AFF", "#DCD3FF"),
    TRANSPORTATION("#FF9CEE", "#FFCCF9"),
    UTILITIES("#85E3FF", "#C4FAF8"),
    HEALTHCARE("#E7FFAC", "#F3FFE3"),
    OTHER("#FFABAB", "#FFCBC1");

    // ------------------------------ Instance Variables ------------------------------
    
    /** The default color assigned to the category for display purposes */
    private final String DEFAULT_COLOR;
    /** The hover color assigned to the category for display purposes */
    private final String HOVER_COLOR;

    // ------------------------------ Constructor ------------------------------

    /**
     * Constructs a Category enum value with specified default and hover colors.
     * 
     * @param defaultColor the default color code (in hex format)
     * @param hoverColor   the hover color code (in hex format)
     */
    private Category(String defaultColor, String hoverColor) {
        this.DEFAULT_COLOR = defaultColor; 
        this.HOVER_COLOR = hoverColor;
    }

    // ------------------------------ Getter Methods ------------------------------

    /**
     * Gets the default color assigned to the category.
     * 
     * @return the hex code of the default color
     */
    public String getDefaultColor() {
        return this.DEFAULT_COLOR;
    }

    /**
     * Gets the hover color assigned to the category.
     * 
     * @return the hex code of the hover color
     */
    public String getHoverColor() {
        return this.HOVER_COLOR;
    }

    // ------------------------------ Helper Methods ------------------------------

    /**
     * Returns a string representation of the category, formatted with the first 
     * letter capitalized and the rest in lowercase.
     * 
     * @return the formatted category name
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
