package net.c5h8no4na.sqllistener.rest;

import java.util.ArrayList;
import java.util.List;

public class Query {
	private String sql;
	private int count;

	private List<Infos> infos = new ArrayList<>();

	public Query(String sql, int count) {
		super();
		this.sql = sql;
		this.count = count;
	}

	public void addInfo(Infos i) {
		infos.add(i);
	}

}
