import { QueryStore } from "./QueryStore.js";
import { Ticker } from "./Ticker.js";

class WebsocketHandler {
	constructor() {
		//this.ws = new WebSocket("ws://server.lan:8080/sql-listener-rest-0.0.1-SNAPSHOT/websocket");
		this.ws = new WebSocket("ws://" + window.location.host + window.location.pathname + "websocket");
		this.ws.addEventListener("message", event => { this.handleIncoming(event) });
		this.statusDiv = document.getElementById("listener-status");
		this.detailsTableKeyDiv = document.getElementById("details-content");
		this.queryCountDiv = document.getElementById("listener-total-queries");
		this.ticker = new Ticker();

		this.groupByStackFrame = new QueryStore("table-by-stackframe", key => {
			this.sendMessage("GET_DETAILS_BY_STACKFRAME", key);
		});
		this.groupBySQL = new QueryStore("table-by-sql", key => {
			this.sendMessage("GET_DETAILS_BY_SQL", key);
		});

		this.groupByDetails = new QueryStore("table-by-details", () => { });

		this.queryCount = 0;

		document.getElementById("button-listener-toggle").addEventListener("click", () => {
			this.sendMessage("TOGGLE_LISTENER");
		});
		document.getElementById("button-listener-clear").addEventListener("click", () => {
			this.clear();
		})
	}

	handleIncoming(event) {
		const json = JSON.parse(event.data);
		if (json.type === "STATUSREPORT") {
			this.updateListenerStatus(json.message);
		} else if (json.type === "SQL_ENTRY") {
			this.ticker.add(json.message);
			this.groupByStackFrame.add(json.message.stackTrace[0]);
			this.groupBySQL.add(json.message.sqlSortable);
			this.addToQueryCount(1);
		} else if (json.type === "INITIAL_DATA") {
			this.ticker.fillWithInitialData(json.message.tickerEntries);
			this.addToQueryCount(json.message.currentQueryCount);
			this.updateListenerStatus(json.message.listenerStatus);
			this.groupByStackFrame.init(json.message.groupByStackFrame);
			this.groupBySQL.init(json.message.groupBySQL);
		} else if (json.type === "DETAILS_BY_STACKFRAME" || json.type === "DETAILS_BY_SQL") {
			this.groupByDetails.clear();
			this.groupByDetails.init(json.message.entries);
			this.detailsTableKeyDiv.innerHTML = json.message.key;
			document.getElementById("nav-by-details-tab").click();
		} else {
			console.log("Unhandled type: " + json.type);
		}
	}

	sendMessage(type, extra = "") {
		this.ws.send(JSON.stringify({ type: type, extraData: extra }));
	}

	clear() {
		this.sendMessage("CLEAR_LISTENER");
		this.ticker.clear();
		this.groupBySQL.clear();
		this.groupByStackFrame.clear();
		this.queryCount = 0;
		this.addToQueryCount(0);
	}

	addToQueryCount(amount) {
		this.queryCount += amount;
		this.queryCountDiv.innerText = this.queryCount;
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
