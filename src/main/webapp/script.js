class WebsocketHandler {
	constructor() {
		this.ws = new WebSocket("ws://" + window.location.host + window.location.pathname + "websocket");
		this.ws.addEventListener("message", this.handleIncoming);
	}

	handleIncoming(event) {
		let messageArea = document.getElementById("messages");
		messageArea.value += event.data + "\r\n";
	}

	sendMessage(message) {
		this.ws.send(message);
	}
}

let handle;
document.addEventListener("DOMContentLoaded", () => {
	handle = new WebsocketHandler();
});