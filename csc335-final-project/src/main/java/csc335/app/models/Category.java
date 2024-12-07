package csc335.app.models;

import java.util.Objects;

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
    FOOD("#AFF8D8", "#DBFFD6", "burger-solid"),
    ENTERTAINMENT("#A79AFF", "#DCD3FF", "tv-solid"),
    TRANSPORTATION("#FF9CEE", "FFCCF9", "car-solid"),
    UTILITIES("#85E3FF", "#C4FAF8", "lightbulb-solid"),
    HEALTHCARE("#E7FFAC", "F3FFE3", "suticase-medical-solid"),
    OTHER("#FFABAB", "#FFCBC1", "star-solid");

    /* Category colors for GUI purposes */
    private final String DEFAULT_COLOR;
    private final String HOVER_COLOR;
    private final String SVG_ICON;

    /**
     * Sets a color as a hex code to each category value
     * 
     * @param color the color code 
     */
    private Category(String defaultColor, String hoverColor, String svgIcon) {
        this.DEFAULT_COLOR = defaultColor; 
        this.HOVER_COLOR = hoverColor;
        this.SVG_ICON = svgIcon;
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

    public String getSvgIcon() {
        return this.SVG_ICON;
    }

    public String getSvgUrl() {
        String svgUrl = Objects.requireNonNull(getClass().getResource("/svg/" + this.SVG_ICON + ".svg")).toExternalForm();
        // String svgFile = new File(svgFilePath)
        // URL svgUrl = svgFile.toURI().toURL();
        return "../svg/" + this.SVG_ICON + ".svg";
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
