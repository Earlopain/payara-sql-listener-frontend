package net.c5h8no4na.sqllistener.rest;

public class WebSocketIncomingCommand {
	private IncomingCommandType type;
	private String extraData;

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public IncomingCommandType getType() {
		return type;
	}

	public void setType(IncomingCommandType type) {
		this.type = type;
	}
}

enum IncomingCommandType {
	TOGGLE_LISTENER,
	CLEAR_LISTENER;
}