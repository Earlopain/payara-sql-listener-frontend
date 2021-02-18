class WebsocketHandler {
	constructor() {
		this.ws = new WebSocket("ws://server.lan:8080/sql-listener-rest-0.0.1-SNAPSHOT/websocket");
		this.ws.addEventListener("message", event => { this.handleIncoming(event) });
		this.toast = new bootstrap.Toast(document.querySelector('.toast'));
		this.ticker = document.getElementById("ticker");
		this.tickerCounter = 0;
	}

	handleIncoming(event) {
		const json = JSON.parse(event.data);
		if (json.type === "STATUSREPORT") {
			const message = json.message;
			this.createToast(message);
		} else if (json.type === "SQL_ENTRY") {
			this.addToTicker(json.message);
		} else {
			let messageArea = document.getElementById("messages");
			messageArea.value += event.data + "\r\n";
		}
	}

	sendMessage(message) {
		this.ws.send(message);
	}

	createToast(message) {
		this.toast._element.querySelector(".toast-body").innerText = message;
		this.toast.show();
	}

	addToTicker(query) {
		const timestamp = new Date(query.timestamp).toISOString().slice(0, 19);
		const title = `Pool: ${query.poolName} Timestamp: ${timestamp}`;
		const triggerId = "accordion_" + this.tickerCounter++;

		const template = document.getElementById("accordion-template").cloneNode(true);

		const button = template.querySelector(".accordion-button");
		button.setAttribute("data-bs-target", "#" + triggerId);
		button.innerText = title;

		const collapse = template.querySelector(".accordion-collapse");
		collapse.id = triggerId;

		const body = template.querySelector(".accordion-body");
		body.innerText = query.sql;

		this.ticker.prepend(template);
		console.log(query);
	}
}

let handle;
document.addEventListener("DOMContentLoaded", () => {
	handle = new WebsocketHandler();
});
