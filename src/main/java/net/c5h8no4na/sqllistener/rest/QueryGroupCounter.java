package net.c5h8no4na.sqllistener.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.c5h8no4na.sqllistener.ExecutedSQLInfos;
import net.c5h8no4na.sqllistener.SQLInfoStructure;
import net.c5h8no4na.sqllistener.SQLQuery;

public class QueryGroupCounter {

	private final Collection<SQLInfoStructure> input;

	QueryGroupCounter(Collection<SQLInfoStructure> input) {
		this.input = input;
	}

	public Map<String, Integer> groupByStrackFrame() {
		Map<String, Integer> result = new HashMap<>();

		for (SQLInfoStructure sqlInfoStructure : input) {
			for (SQLQuery infos : sqlInfoStructure.getQueries().values()) {
				for (ExecutedSQLInfos query : infos.getInfos()) {
					Integer count = result.computeIfAbsent(query.getStackTrace().get(0), key -> Integer.valueOf(0));
					result.put(query.getStackTrace().get(0), count + 1);
				}
			}
		}

		return result;
	}

	public Map<String, Integer> groupBySQL() {
		Map<String, Integer> result = new HashMap<>();

		for (SQLInfoStructure sqlInfoStructure : input) {
			for (SQLQuery infos : sqlInfoStructure.getQueries().values()) {
				Integer count = result.computeIfAbsent(infos.getSql(), key -> Integer.valueOf(0));
				result.put(infos.getSql(), count + infos.getInfos().size());

			}
		}

		return result;
	}
}