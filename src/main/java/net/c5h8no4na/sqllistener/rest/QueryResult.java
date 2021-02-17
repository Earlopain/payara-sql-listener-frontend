package net.c5h8no4na.sqllistener.rest;

import java.util.ArrayList;
import java.util.List;

public class QueryResult {
	private String poolName;
	private List<Query> queries = new ArrayList<>();

	public QueryResult(String poolName) {
		this.poolName = poolName;
	}

	public void addQuery(Query q) {
		queries.add(q);
	}
}
