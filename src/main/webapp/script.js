document.addEventListener("DOMContentLoaded", () => {
	let ws = new WebSocket(build = "ws://" + window.location.host + window.location.pathname + "websocket");
	ws.addEventListener("message", event => {
		var messageArea = document.getElementById("messages");
		messageArea.value += event.data + "\r\n";
	});
});