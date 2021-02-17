package net.c5h8no4na.sqllistener.rest;

import java.util.Optional;
import java.util.stream.Stream;

public enum WebSocketIncomingCommand {
	ACTIVATE,
	DEACTIVATE,
	CLEAR;

	public static Optional<WebSocketIncomingCommand> fromString(String input) {
		return Stream.of(WebSocketIncomingCommand.values()).filter(e -> e.name().equalsIgnoreCase(input)).findFirst();
	}
}
