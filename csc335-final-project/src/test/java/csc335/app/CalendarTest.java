package csc335.app;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.beanutils.converters.CalendarConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalendarTest {

    private CalendarConverter converter;

    @BeforeEach
    public void setUp() {
        converter = CalendarConverter.INSTANCE;
    }

    @Test
    public void testGetCalendar_WithSpecificDate() {
        int year = 2024;
        int month = 12; // December
        int day = 24;

        Calendar calendar = converter.getCalendar(year, month, day);

        assertEquals(2024, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH)); // 0-based index for months
        assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
    }

     @Test
    public void testGetCalendar_CurrentDate() {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar testCalendar = converter.getCalendar();

        assertEquals(currentCalendar.get(Calendar.YEAR), testCalendar.get(Calendar.YEAR));
        assertEquals(currentCalendar.get(Calendar.MONTH), testCalendar.get(Calendar.MONTH));
        assertEquals(currentCalendar.get(Calendar.DAY_OF_MONTH), testCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testGetCalendar_WithMinusMonths() {
        int minusMonths = 2;
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.MONTH, -minusMonths);

        Calendar testCalendar = converter.getCalendar(minusMonths);

        assertEquals(currentCalendar.get(Calendar.YEAR), testCalendar.get(Calendar.YEAR));
        assertEquals(currentCalendar.get(Calendar.MONTH), testCalendar.get(Calendar.MONTH));
        assertEquals(currentCalendar.get(Calendar.DAY_OF_MONTH), testCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testGetStringDate() {
        Calendar calendar = converter.getCalendar(2024, 12, 24);
        String expectedDate = "2024-12-24";
        
        assertEquals(expectedDate, converter.getStringDate(calendar));
    }

    @Test
    public void testGetLastMonths() {
        List<String> lastMonths = CalendarConverter.getLastMonths(3);

        assertEquals(3, lastMonths.size());
        assertNotNull(lastMonths.get(0)); // Ensure the month name is not null
    }

    @Test
    public void testGetLastMonths_CorrectOrder() {
        List<String> lastMonths = CalendarConverter.getLastMonths(3);
        Calendar calendar = Calendar.getInstance();

        for (int i = 2; i >= 0; i--) {
            int expectedMonthIndex = calendar.get(Calendar.MONTH);
            String expectedMonthName = new java.text.DateFormatSymbols().getShortMonths()[expectedMonthIndex];

            assertEquals(expectedMonthName, lastMonths.get(i));

            calendar.add(Calendar.MONTH, -1); // Go back a month
        }
    }
}
