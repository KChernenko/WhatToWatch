package me.bitfrom.whattowatch.utils.bus;

/**
 * Created by Constantine on 07.10.2015.
 */
public class ConnectionUnsuccessEvent {

    private String message;

    public ConnectionUnsuccessEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
