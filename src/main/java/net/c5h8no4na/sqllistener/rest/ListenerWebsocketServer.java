package net.c5h8no4na.sqllistener.rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import net.c5h8no4na.sqllistener.GlassfishSQLTracer;

@ApplicationScoped
@ServerEndpoint("/websocket")
public class ListenerWebsocketServer {

	private Gson gson = new Gson();

	@OnOpen
	public void onOpen(Session session) {
		Logger.getGlobal().info("Session " + session.getId() + " opened");
		GlassfishSQLTracer.addListener(session.getId(), query -> {
			Logger.getGlobal().info("Trying to send to session " + session.getId());
			try {
				session.getBasicRemote().sendText(gson.toJson(query));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	@OnClose
	public void onClose(Session session) {
		GlassfishSQLTracer.removeListener(session.getId());
	}
}
