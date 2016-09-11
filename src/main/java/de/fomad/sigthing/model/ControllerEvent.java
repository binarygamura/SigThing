package de.fomad.sigthing.model;

/**
 *
 * @author binary gamura
 */
public class ControllerEvent<T> {

    private final T payload;

    private final EventType type;

    public ControllerEvent(T payload, EventType type) {
	this.payload = payload;
	this.type = type;
    }

    public T getPayload() {
	return payload;
    }

    public EventType getType() {
	return type;
    }

    public static enum EventType {

	ERROR,
	GOT_TOKEN,
	GOT_CHARACTER_INFO,
	GOT_CHARACTER,
	SOLAR_SYSTEM_CHANGE,
        NEW_SIGNATURES
    }
}
