package csc335.app;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public enum CalendarConverter {

    INSTANCE; // Singleton instance 

    public Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar months are 0-based index so we substract 1
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    public Calendar getCalendar() {
        // Use LocalDate for easier date manipulation
        Date currentDate = getLocalDate(0);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        return currentCalendar;
    }

    public Calendar getCalendar(int minusMonths) {
        Date newDate = getLocalDate(minusMonths);
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(newDate);
        return newCalendar;
    }

    private static Date getLocalDate(int minusMonths){
        LocalDate localDate = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        localDate = localDate.minusMonths(minusMonths);
        Date date = java.util.Date.from(localDate.atStartOfDay(zoneId).toInstant());
        return date;
    }

    public String getStringDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-indexed
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }
}
