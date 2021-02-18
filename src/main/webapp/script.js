class WebsocketHandler {
	constructor() {
		this.ws = new WebSocket("ws://server.lan:8080/sql-listener-rest-0.0.1-SNAPSHOT/websocket");
		this.ws.addEventListener("message", event => { this.handleIncoming(event) });
		this.toast = new bootstrap.Toast(document.querySelector('.toast'));
		this.ticker = document.getElementById("ticker");
		this.tickerCounter = 0;
		this.statusDiv = document.getElementById("listener-status");
	}

	handleIncoming(event) {
		const json = JSON.parse(event.data);
		if (json.type === "STATUSREPORT") {
			const message = json.message;
			this.createToast(message);
			this.updateListenerStatus(message);
		} else if (json.type === "SQL_ENTRY") {
			this.addToTicker(json.message);
		} else if (json.type === "SQL_ENTRY_LIST") {
			this.fillTickerWithInitialData(json.message);
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
		this.ticker.innerHTML = "";
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

	fillTickerWithInitialData(queryArray) {
		console.log(queryArray);
		const addToTicker = queryArray.sort((a, b) => a.timestamp < b.timestamp).slice(0, 10).reverse();
		for (const query of addToTicker) {
			this.addToTicker(query);
		}
	}

	addToTicker(query) {
		const timestamp = new Date(query.timestamp).toISOString().slice(0, 19);
		const triggerId = "accordion_" + this.tickerCounter++;

		const template = document.getElementById("accordion-template").cloneNode(true);

		const button = template.querySelector(".accordion-button");
		button.setAttribute("data-bs-target", "#" + triggerId);
		template.querySelector(".accordion-title-timestamp").innerHTML = timestamp;
		template.querySelector(".accordion-title-pool").innerHTML = query.poolName;
		template.querySelector(".accordion-title-stacktrace").innerText = query.stackTrace[0];
		template.querySelector(".accordion-title-sql").innerHTML = query.sql;

		const collapse = template.querySelector(".accordion-collapse");
		collapse.id = triggerId;

		template.querySelector(".sql-content").innerText = query.sql;
		template.querySelector(".stacktrace-content").innerText = query.stackTrace.join("\n");

		this.ticker.prepend(template);
		console.log(query);
	}
}

let handle;
document.addEventListener("DOMContentLoaded", () => {
	handle = new WebsocketHandler();
});
