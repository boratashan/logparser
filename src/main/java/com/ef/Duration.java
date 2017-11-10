package com.ef;
/**
 * Enum of command line duration parameter.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public enum Duration {
    HOURLY("hourly"),
    DAILY("daily");

    private String textValue;

    Duration(String textValue) {
        this.textValue = textValue;
    }

    public static Duration fromString(String value) {
        for (Duration d : Duration.values()) {
            if (d.textValue.equalsIgnoreCase(value))
                return d;
        }
        throw new EnumConstantNotPresentException(Duration.class, String.format("%s is not member of the enum", value));
    }

}
