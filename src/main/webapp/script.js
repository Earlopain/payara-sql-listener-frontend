import { QueryStore } from "./QueryStore.js";
import { Ticker } from "./Ticker.js";

class WebsocketHandler {
	constructor() {
		this.ws = new WebSocket("ws://server.lan:8080/sql-listener-rest-0.0.1-SNAPSHOT/websocket");
		this.ws.addEventListener("message", event => { this.handleIncoming(event) });
		this.toast = new bootstrap.Toast(document.querySelector('.toast'));
		this.statusDiv = document.getElementById("listener-status");
		this.ticker = new Ticker();
		this.queryStore = new QueryStore();

		document.getElementById("button-listener-toggle").addEventListener("click", () => {
			this.sendMessage("toggle_listener");
		});
		document.getElementById("button-listener-clear").addEventListener("click", () => {
			this.clear();
		})
	}

	handleIncoming(event) {
		const json = JSON.parse(event.data);
		if (json.type === "STATUSREPORT") {
			const message = json.message;
			this.createToast(message);
			this.updateListenerStatus(message);
		} else if (json.type === "SQL_ENTRY") {
			this.ticker.add(json.message);
			this.queryStore.add(json.message);
		} else if (json.type === "SQL_ENTRY_LIST") {
			this.ticker.fillWithInitialData(json.message);
			this.queryStore.addAll(json.message);
		} else {
			console.log("Unhandled type: " + json.type)
		}
	}

	sendMessage(message) {
		this.ws.send(message);
	}

	createToast(message) {
		this.toast._element.querySelector(".toast-body").innerText = message;
		this.toast.show();
	}

	clear() {
		this.sendMessage('clear_listener');
		this.ticker.clear();
		this.queryStore.clear();
	}

	updateListenerStatus(message) {
		switch (message) {
			case "LISTENER_ACTIVATED":
				this.statusDiv.innerText = "Listener is running";
				this.statusDiv.className = "btn btn-success";
				break;
			case "LISTENER_DEACTIVATED":
				this.statusDiv.innerText = "Listener is inactive";
				this.statusDiv.className = "btn btn-warning";
				break;
		}
	}
}

document.addEventListener("DOMContentLoaded", () => {
	new WebsocketHandler();
});
