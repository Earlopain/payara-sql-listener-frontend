class WebsocketHandler {
	constructor() {
		this.ws = new WebSocket("ws://" + window.location.host + window.location.pathname + "websocket");
		this.ws.addEventListener("message", event => { this.handleIncoming(event) });
		this.toast = new bootstrap.Toast(document.querySelector('.toast'));
	}

	handleIncoming(event) {
		const json = JSON.parse(event.data);
		if (json.type === "STATUSREPORT") {
			const message = json.message;
			this.createToast(message);
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
}

let handle;
document.addEventListener("DOMContentLoaded", () => {
	handle = new WebsocketHandler();
});
