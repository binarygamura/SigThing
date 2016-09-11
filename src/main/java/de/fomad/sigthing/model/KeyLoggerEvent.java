package de.fomad.sigthing.model;

/**
 *
 * @author binary
 */
public class KeyLoggerEvent {

    private String input;

    public KeyLoggerEvent(String input) {
	this.input = input;
    }

    public String getInput() {
	return input;
    }

    public void setInput(String input) {
	this.input = input;
    }
}
