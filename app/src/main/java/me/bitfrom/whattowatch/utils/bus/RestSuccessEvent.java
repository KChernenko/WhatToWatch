package me.bitfrom.whattowatch.utils.bus;

/**
 * Created by Constantine on 05.10.2015.
 */
public class RestSuccessEvent {

    private String message;

    public RestSuccessEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
