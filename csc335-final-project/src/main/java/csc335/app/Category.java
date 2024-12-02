package csc335.app;

import java.util.ArrayList;
import java.util.List;

// [ ] Complete file comment
/**
 * @author Genesis Benedith
 * Model enum that represents an expense category
 */

 // [ ] Complete class comment
public enum Category {
    FOOD,
    ENTERTAINMENT,
    TRANSPORTATION,
    UTILITIES,
    HEALTHCARE,
    OTHER;

    // [ ] Complete method comment
    /**
     * 
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }

    // [ ] Complete method comment
    public static List<String> allValues() {
        List<String> result = new ArrayList<>();
        for (Category category : values()) {
            result.add(category.toString());
        }
        return result;
    }
}
