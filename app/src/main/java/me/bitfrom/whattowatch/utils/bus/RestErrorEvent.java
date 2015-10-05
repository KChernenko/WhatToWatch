package me.bitfrom.whattowatch.utils.bus;

/**
 * Created by Constantine on 29.07.2015.
 */
public class RestErrorEvent {

    private final String message;

    public RestErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
