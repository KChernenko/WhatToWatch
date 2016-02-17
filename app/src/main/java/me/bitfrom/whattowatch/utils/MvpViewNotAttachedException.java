package me.bitfrom.whattowatch.utils;


public class MvpViewNotAttachedException extends RuntimeException {

    public MvpViewNotAttachedException() {
        super(ConstantsManager.EXCEPTION_MESSAGE);
    }
}