package de.snackaholic.rss.model;

public enum Day {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String value;

    Day(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Day fromString(String text) {
        for (Day day : Day.values()) {
            if (day.value.equalsIgnoreCase(text)) {
                return day;
            }
        }
        return null;
    }
}
