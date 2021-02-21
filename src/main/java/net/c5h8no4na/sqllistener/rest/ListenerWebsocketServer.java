package net.c5h8no4na.sqllistener.rest;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import net.c5h8no4na.sqllistener.GlassfishSQLTracer;

@ServerEndpoint("/websocket")
public class ListenerWebsocketServer {

	private static final Logger LOG = Logger.getLogger(ListenerWebsocketServer.class.getName());

	private static final Gson gson = new Gson();

	private static final List<Session> sessions = new CopyOnWriteArrayList<>();

	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);

		QueryGroupCounter counter = new QueryGroupCounter(GlassfishSQLTracer.getAll());
		InitialData data = new InitialData();
		data.setTickerEntries(GlassfishSQLTracer.getRecent());
		data.setCurrentQueryCount(GlassfishSQLTracer.getCurrentQueryCount());
		data.setGroupByStackFrame(counter.groupByStrackFrame());
		data.setGroupBySQL(counter.groupBySQL());
		data.setListenerStatus(getListenerStatus());

		WebSocketOutgoing.create(data).send(session);

		GlassfishSQLTracer.addListener(session.getId(), query -> {
			WebSocketOutgoing.create(query).send(session);
		});
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		GlassfishSQLTracer.removeListener(session.getId());
	}

	@OnMessage
	public void onMessage(String message) {
		WebSocketIncomingCommand command = gson.fromJson(message, WebSocketIncomingCommand.class);
		if (command.getType() == null) {
			LOG.info("Client sent invalid command: " + message);
			return;
		}
		switch (command.getType()) {
		case TOGGLE_LISTENER:
			GlassfishSQLTracer.toggle();
			WebSocketOutgoing.create(getListenerStatus()).sendToAll(sessions);
			break;
		case CLEAR_LISTENER:
			GlassfishSQLTracer.clear();
			WebSocketOutgoing.create(StatusReportType.LISTENER_CLEARED).sendToAll(sessions);
			break;
		default:
			LOG.info("Unhandled command: " + command.getType());
		}
	}

	private StatusReportType getListenerStatus() {
		if (GlassfishSQLTracer.isActive()) {
			return StatusReportType.LISTENER_ACTIVATED;
		} else {
			return StatusReportType.LISTENER_DEACTIVATED;
		}
	}
}
