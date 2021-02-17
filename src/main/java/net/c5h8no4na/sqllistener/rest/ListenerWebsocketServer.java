package net.c5h8no4na.sqllistener.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import net.c5h8no4na.sqllistener.GlassfishSQLTracer;
import net.c5h8no4na.sqllistener.SingleSQLQuery;

@ApplicationScoped
@ServerEndpoint("/websocket")
public class ListenerWebsocketServer {

	private static final Logger LOG = Logger.getLogger(ListenerWebsocketServer.class.getName());

	private Gson gson = new Gson();

	List<Session> sessions = new ArrayList<>();

	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);
		List<SingleSQLQuery> initialEntries = GlassfishSQLTracer.getAll();
		send(session, WebSocketOutgoing.create(initialEntries));
		GlassfishSQLTracer.addListener(session.getId(), query -> {
			send(session, WebSocketOutgoing.create(query));
		});

	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		GlassfishSQLTracer.removeListener(session.getId());
	}

	@OnMessage
	public void onMessage(String message) {
		Optional<WebSocketIncomingCommand> command = WebSocketIncomingCommand.fromString(message);
		if (command.isEmpty()) {
			LOG.info("Client sent invalid command: " + message);
			return;
		}
		switch (command.get()) {
		case ACTIVATE:
			GlassfishSQLTracer.activate();
			sendToAll(StatusReportType.LISTENER_ACTIVATED);
			break;
		case DEACTIVATE:
			GlassfishSQLTracer.deactivate();
			sendToAll(StatusReportType.LISTENER_DEACTIVATED);
			break;
		case CLEAR:
			GlassfishSQLTracer.clear();
			sendToAll(StatusReportType.LISTENER_CLEARED);
			break;
		default:
			throw new IllegalArgumentException("Unknown message " + message);
		}
	}

	private void send(Session session, WebSocketOutgoing data) {
		try {
			session.getBasicRemote().sendText(gson.toJson(data));
		} catch (IOException e) {
			// Something went wrong sending to a client, we can just ignore it
		}
	}

	private void sendToAll(StatusReportType status) {
		for (Session session : sessions) {
			send(session, WebSocketOutgoing.create(status));
		}
	}
}
