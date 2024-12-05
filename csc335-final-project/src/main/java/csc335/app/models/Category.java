package csc335.app.models;

// [ ] Complete file comment
/**
 * Builds an enum to represent the categories 
 * of the expenses & budgets tracked by the personal
 * financial assistant app
 * 
 * @author Genesis Benedith
 * 
 */

 // [ ] Complete class comment
public enum Category {
    FOOD("#AFF8D8", "#DBFFD6"),
    ENTERTAINMENT("#A79AFF", "#DCD3FF"),
    TRANSPORTATION("#FF9CEE", "FFCCF9"),
    UTILITIES("#85E3FF", "#C4FAF8"),
    HEALTHCARE("#E7FFAC", "F3FFE3"),
    OTHER("#FFABAB", "#FFCBC1");

    /* Category colors for GUI purposes */
    private final String DEFAULT_COLOR;
    private final String HOVER_COLOR;

    /**
     * Sets a color as a hex code to each category value
     * 
     * @param color the color code 
     */
    private Category(String defaultColor, String hoverColor) {
        this.DEFAULT_COLOR = defaultColor; 
        this.HOVER_COLOR = hoverColor;
    }

    /**
     * Gets the default color assigned to the category
     * @return the color's hex code 
     */
    public String getDefaultColor() {
        return this.DEFAULT_COLOR;
    }

    /**
     * Gets the hover color assigned to the category
     * @return the color's hex code 
     */
    public String getHoverColor() {
        return this.HOVER_COLOR;
    }

    // [ ] Complete method comment
    /**
     * 
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}