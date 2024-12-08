package csc335.app.utils;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Utility class for handling date and calendar conversions.
 * Provides methods to generate and manipulate Calendar instances, format dates as strings,
 * and retrieve a list of recent months.
 * 
 * File: CalendarConverter.java
 * Course: CSC 335 (Fall 2024)
 * @author Genesis Benedith
 */
public enum CalendarConverter {

    INSTANCE; //Singleton instance of the CalendarConverter utility 

    /**
     * Creates a Calendar instance for the specified year, month, and day.
     * 
     * @param year  the year of the date
     * @param month the month of the date (1-based index, e.g., January = 1)
     * @param day   the day of the date
     * @return a Calendar instance representing the specified date
     */
    public Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar months are 0-based index so we substract 1
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    /**
     * Gets the current date as a Calendar instance.
     * 
     * @return a Calendar instance representing the current date
     */
    public Calendar getCalendar() {
        // Use LocalDate for easier date manipulation
        Date currentDate = getLocalDate(0);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        return currentCalendar;
    }

    /**
     * Gets a Calendar instance adjusted by a specified number of months from the current date.
     * 
     * @param minusMonths the number of months to subtract from the current date
     * @return a Calendar instance representing the adjusted date
     */
    public Calendar getCalendar(int minusMonths) {
        Date newDate = getLocalDate(minusMonths);
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(newDate);
        return newCalendar;
    }

    /**
     * Gets a Date instance adjusted by a specified number of months from the current date.
     * 
     * @param minusMonths the number of months to subtract from the current date
     * @return a Date instance representing the adjusted date
     */
    private static Date getLocalDate(int minusMonths){
        LocalDate localDate = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        localDate = localDate.minusMonths(minusMonths);
        Date date = java.util.Date.from(localDate.atStartOfDay(zoneId).toInstant());
        return date;
    }

    /**
     * Formats a Calendar instance as a string in the format "YYYY-MM-DD".
     * 
     * @param calendar the Calendar instance to format
     * @return a string representation of the date
     */
    public String getStringDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-indexed
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    /**
     * Retrieves a list of the short names of the last N months, starting from the current month.
     * 
     * @param numMonths the number of months to retrieve
     * @return a list of short month names
     */
    public List<String> getLastMonths(int numMonths) {
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(); // Current date

        for (int i = 0; i < numMonths; i++) {
            int monthIndex = calendar.get(Calendar.MONTH); // Get the current month (0-based index)
            String monthName = new DateFormatSymbols().getShortMonths()[monthIndex]; // Get the month name
            months.add(monthName);
            calendar.add(Calendar.MONTH, -1); // Move back one month
        }

        Collections.reverse(months);
        return months;
    }
}
