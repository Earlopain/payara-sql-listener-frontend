export class Ticker {

	constructor() {
		this.template = document.getElementById("accordion-template");
		this.tickerElement = document.getElementById("ticker");
		this.idCounter = 0;
        this.maxEntries = 100;
	}

	clear() {
		this.tickerElement.innerHTML = "";
	}

	fillWithInitialData(queryArray) {
		for (const query of queryArray) {
			this.add(query);
		}
	}

	add(query) {
		const timestamp = new Date(query.timestamp).toISOString().slice(0, 19);
		const triggerId = "accordion_" + this.idCounter++;

		const template = this.template.cloneNode(true);
		template.removeAttribute("id");

		const button = template.querySelector(".accordion-button");
		button.setAttribute("data-bs-target", "#" + triggerId);
		template.querySelector(".accordion-title-timestamp").innerHTML = timestamp;
		template.querySelector(".accordion-title-pool").innerHTML = query.poolName;
		template.querySelector(".accordion-title-stacktrace").innerText = query.stackTrace[0];
		template.querySelector(".accordion-title-sql").innerHTML = query.sqlNoQuestionmarks;

		const collapse = template.querySelector(".accordion-collapse");
		collapse.id = triggerId;

		template.querySelector(".sql-content").innerText = query.sqlNoQuestionmarks;
		template.querySelector(".stacktrace-content").innerText = query.stackTrace.join("\n");

		this.tickerElement.prepend(template);
        this.ensureCapacity();
	}

    ensureCapacity() {
        while(this.tickerElement.childElementCount > this.maxEntries) {
            this.tickerElement.removeChild(this.tickerElement.lastChild);
        }
    }
}
