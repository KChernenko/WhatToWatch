package me.bitfrom.whattowatch.utils.bus;

/**
 * Created by Constantine on 05.10.2015.
 */
public class RestSuccessfulEvent {

    private String message;

    public RestSuccessfulEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
