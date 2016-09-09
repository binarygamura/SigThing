package de.fomad.sigthing.model;

/**
 *
 * @author binary gamura
 */
public class DummyWebServerEvent<T> {

    private final T payload;

    private final EventType type;

    public DummyWebServerEvent(T payload, EventType type) {
	this.type = type;
	this.payload = payload;
    }

    public static enum EventType {
	ERROR,
	GOT_RESPONSE
    }

    public T getPayload() {
	return payload;
    }

    public EventType getType() {
	return type;
    }

}
