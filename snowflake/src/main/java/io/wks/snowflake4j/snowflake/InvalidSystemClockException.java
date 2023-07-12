package io.wks.snowflake4j.snowflake;

public class InvalidSystemClockException extends RuntimeException {

    public InvalidSystemClockException(String message) {
        super(message);
    }
}
