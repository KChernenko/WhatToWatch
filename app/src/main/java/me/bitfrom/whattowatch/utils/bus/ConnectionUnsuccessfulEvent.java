package me.bitfrom.whattowatch.utils.bus;

/**
 * Created by Constantine on 07.10.2015.
 */
public class ConnectionUnsuccessfulEvent {

    private String message;

    public ConnectionUnsuccessfulEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
