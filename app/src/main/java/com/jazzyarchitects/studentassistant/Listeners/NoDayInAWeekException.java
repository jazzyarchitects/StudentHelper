package com.jazzyarchitects.studentassistant.Listeners;

/**
 * Created by Jibin_ism on 19-Jul-15.
 */
public class NoDayInAWeekException extends RuntimeException {
    private static final long serialVersionUID = 234122996006190596L;

    /**
     * Constructs a new {@code IndexOutOfBoundsException} that includes the
     * current stack trace.
     */
    public NoDayInAWeekException() {
        super();
    }

    /**
     * Constructs a new {@code IndexOutOfBoundsException} with the current stack
     * trace and the specified detail message.
     *
     * @param detailMessage
     *            the detail message for this exception.
     */
    public NoDayInAWeekException(String detailMessage) {
        super(detailMessage);
    }
}
