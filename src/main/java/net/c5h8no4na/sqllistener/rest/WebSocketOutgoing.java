package net.c5h8no4na.sqllistener.rest;

import java.util.List;

import net.c5h8no4na.sqllistener.SingleSQLQuery;

public class WebSocketOutgoing {
	private Type type;
	private Object message;

	public static WebSocketOutgoing create(SingleSQLQuery query) {
		WebSocketOutgoing a = new WebSocketOutgoing();
		a.setType(Type.SQL_ENTRY);
		a.setMessage(query);
		return a;
	}

	public static WebSocketOutgoing create(List<SingleSQLQuery> queries) {
		WebSocketOutgoing a = new WebSocketOutgoing();
		a.setType(Type.SQL_ENTRY_LIST);
		a.setMessage(queries);
		return a;
	}

	public static WebSocketOutgoing create(StatusReportType command) {
		WebSocketOutgoing a = new WebSocketOutgoing();
		a.setType(Type.STATUSREPORT);
		a.setMessage(command);
		return a;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
}

enum Type {
	SQL_ENTRY,
	SQL_ENTRY_LIST,
	STATUSREPORT
}

enum StatusReportType {
	LISTENER_ACTIVATED,
	LISTENER_DEACTIVATED,
	LISTENER_CLEARED
}
