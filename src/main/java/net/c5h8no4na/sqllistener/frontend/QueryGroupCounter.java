package net.c5h8no4na.sqllistener.frontend;

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

	public Map<String, Integer> getByStackFrame(String firstFrame) {
		Map<String, Integer> result = new HashMap<>();

		for (SQLInfoStructure sqlInfoStructure : input) {
			for (SQLQuery infos : sqlInfoStructure.getQueries().values()) {
				for (ExecutedSQLInfos query : infos.getInfos()) {
					if (firstFrame.equals(query.getStackTrace().get(0))) {
						Integer count = result.computeIfAbsent(infos.getSql(), key -> Integer.valueOf(0));
						result.put(infos.getSql(), count + 1);
					}
				}
			}
		}

		return result;
	}

	public Map<String, Integer> getBySQL(String sql) {
		Map<String, Integer> result = new HashMap<>();
		for (SQLInfoStructure sqlInfoStructure : input) {
			for (SQLQuery infos : sqlInfoStructure.getQueries().values()) {
				if (sql.equals(infos.getSql())) {
					for (ExecutedSQLInfos a : infos.getInfos()) {
						Integer count = result.computeIfAbsent(a.getStackTrace().get(0), key -> Integer.valueOf(0));
						result.put(a.getStackTrace().get(0), count + 1);
					}
				}
			}
		}

		return result;
	}
}
