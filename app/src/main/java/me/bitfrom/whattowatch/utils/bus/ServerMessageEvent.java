package me.bitfrom.whattowatch.utils.bus;

/**
 * Created by Constantine on 29.07.2015.
 */
public class ServerMessageEvent {

    private final String message;

    public ServerMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
