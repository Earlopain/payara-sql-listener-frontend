export class Ticker {

	constructor() {
		this.template = document.getElementById("accordion-template");
		this.tickerElement = document.getElementById("ticker");
		this.counter = 0;
	}

	clear() {
		this.tickerElement.innerHTML = "";
	}

	fillWithInitialData(queryArray) {
		const addToTicker = queryArray.sort((a, b) => a.timestamp < b.timestamp).slice(0, 10).reverse();
		for (const query of addToTicker) {
			this.add(query);
		}
	}

	add(query) {
		const timestamp = new Date(query.timestamp).toISOString().slice(0, 19);
		const triggerId = "accordion_" + this.counter++;

		const template = this.template.cloneNode(true);
		template.removeAttribute("id");

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

		this.tickerElement.prepend(template);
	}
}
