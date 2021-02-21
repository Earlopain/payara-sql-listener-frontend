export class QueryStore {
	constructor(tableId, callbackOnClick) {
		const tableOptions = {
			autoWidth: false,
			dom: '<""lf>t<"d-flex"pi>',
			order: [[0, "desc"]]
		};
		this.table = $("#" + tableId).DataTable(tableOptions);
		this.table.on("click", "tbody tr", event => {
			if(event.target.cellIndex == 1) {
				callbackOnClick(event.target.innerHTML);
			}
		  });
		this.tracker = {};
		this.callbackOnClick = callbackOnClick;
	}

	clear() {
		this.table.clear().draw();
		this.tracker = {};
	}

	init(data) {
		for (const key of Object.keys(data)) {
			this.tracker[key] = {
				count: data[key],
				row: this.table.row.add([data[key], key])
			}
		}
		this.table.draw(false);
	}

	add(key) {
		if (this.tracker[key] === undefined) {
			this.tracker[key] = {
				count: 0,
				row: this.table.row.add([0, key])
			}
		}
		const value = this.tracker[key];
		value.count++;
		value.row.data([value.count, key]).draw(false);
	}
}
