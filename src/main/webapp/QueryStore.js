export class QueryStore {
	constructor(tableId) {
		const tableOptions = { autoWidth: false, dom: '<""lf>t<"d-flex"pi>' };
		this.table = $("#" + tableId).DataTable(tableOptions);
		this.tracker = {};
	}

	clear() {
		this.table.clear().draw();
		this.tracker = {};
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
