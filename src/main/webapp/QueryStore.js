export class QueryStore {
	constructor() {
		this.bySQL = {};
		this.byFirstTraceFrame = {};
		this.totalCount = 0;

		this.tableByFirstTraceFrame = $("#table-by-stackframe").DataTable({ autoWidth: false , dom: '<""lf>t<"d-flex"pi>'});
		this.tableBySQL = $("#table-by-sql").DataTable({ autoWidth: false, dom: '<""lf>t<"d-flex"pi>'});
	}

	clear() {
		this.tableBySQL.clear().draw();
		this.tableByFirstTraceFrame.clear().draw();
		this.bySQL = {};
		this.byFirstTraceFrame = {};
		this.totalCount = 0;
	}

	add(query) {
		this.totalCount++;
		this.addToTracker(this.bySQL, this.tableBySQL, query.sqlSortable, query);
		this.addToTracker(this.byFirstTraceFrame, this.tableByFirstTraceFrame, query.stackTrace[0], query);
	}

	addAll(queryArray) {
		for (const query of queryArray) {
			this.add(query);
		}
	}

	addToTracker(tracker, table, key, query) {
		if (tracker[key] === undefined) {
			tracker[key] = {
				count: 0,
				row: table.row.add([0, key])
			}
		}
		const value = tracker[key];
		value.count++;

		tracker[key].row.data([value.count, key]).draw(false);
	}

	getCount() {
		return this.totalCount;
	}
}
