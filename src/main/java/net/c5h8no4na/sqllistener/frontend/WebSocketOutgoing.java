package net.c5h8no4na.sqllistener.frontend;

import java.io.IOException;
import java.util.List;

import javax.websocket.Session;

import com.google.gson.Gson;

import net.c5h8no4na.sqllistener.PreparedStatementData;

public class WebSocketOutgoing {
	private Type type;
	private Object message;

	private static final Gson gson = new Gson();

	private WebSocketOutgoing() {}

	public static WebSocketOutgoing create(PreparedStatementData query) {
		WebSocketOutgoing a = new WebSocketOutgoing();
		a.setType(Type.SQL_ENTRY);
		a.setMessage(query);
		return a;
	}

	public static WebSocketOutgoing create(InitialData data) {
		WebSocketOutgoing a = new WebSocketOutgoing();
		a.setType(Type.INITIAL_DATA);
		a.setMessage(data);
		return a;
	}

	public static WebSocketOutgoing create(StatusReportType command) {
		WebSocketOutgoing a = new WebSocketOutgoing();
		a.setType(Type.STATUSREPORT);
		a.setMessage(command);
		return a;
	}

	public static WebSocketOutgoing create(Type t, DetailsByKey details) {
		WebSocketOutgoing a = new WebSocketOutgoing();
		a.setType(t);
		a.setMessage(details);
		return a;
	}

	public Type getType() {
		return type;
	}

	private void setType(Type type) {
		this.type = type;
	}

	public Object getMessage() {
		return message;
	}

	private void setMessage(Object message) {
		this.message = message;
	}

	public void send(Session session) {
		try {
			session.getBasicRemote().sendText(gson.toJson(this));
		} catch (IOException e) {
			// Something went wrong sending to a client, we can just ignore it
		}
	}

	public void sendToAll(List<Session> sessions) {
		for (Session session : sessions) {
			send(session);
		}
	}
}

enum Type {
	SQL_ENTRY,
	INITIAL_DATA,
	STATUSREPORT,
	DETAILS_BY_STACKFRAME,
	DETAILS_BY_SQL;
}

enum StatusReportType {
	LISTENER_ACTIVATED,
	LISTENER_DEACTIVATED,
	LISTENER_CLEARED
}
