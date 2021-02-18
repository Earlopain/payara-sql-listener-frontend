import { QueryStore } from "./QueryStore.js";
import { Ticker } from "./Ticker.js";

class WebsocketHandler {
	constructor() {
		//this.ws = new WebSocket("ws://server.lan:8080/sql-listener-rest-0.0.1-SNAPSHOT/websocket");
		this.ws = new WebSocket("ws://" + window.location.host + window.location.pathname + "websocket");
		this.ws.addEventListener("message", event => { this.handleIncoming(event) });
		this.statusDiv = document.getElementById("listener-status");
		this.queryCountDiv = document.getElementById("listener-total-queries");
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
			this.updateListenerStatus(message);
		} else if (json.type === "SQL_ENTRY") {
			this.ticker.add(json.message);
			this.queryStore.add(json.message);
			this.updateQueryCount();
		} else if (json.type === "SQL_ENTRY_LIST") {
			this.ticker.fillWithInitialData(json.message);
			this.queryStore.addAll(json.message);
			this.updateQueryCount();
		} else {
			console.log("Unhandled type: " + json.type)
		}
	}

	sendMessage(message) {
		this.ws.send(message);
	}

	clear() {
		this.sendMessage('clear_listener');
		this.ticker.clear();
		this.queryStore.clear();
		this.updateQueryCount();
	}

	updateQueryCount() {
		this.queryCountDiv.innerText = this.queryStore.getCount();
	}

	updateListenerStatus(message) {
		const e = this.statusDiv.querySelector("div");
		switch (message) {
			case "LISTENER_ACTIVATED":
				e.innerText = "Running";
				this.statusDiv.className = "btn btn-success";
				break;
			case "LISTENER_DEACTIVATED":
				e.innerText = "Stopped";
				this.statusDiv.className = "btn btn-warning";
				break;
		}
	}
}

document.addEventListener("DOMContentLoaded", () => {
	new WebsocketHandler();
});
