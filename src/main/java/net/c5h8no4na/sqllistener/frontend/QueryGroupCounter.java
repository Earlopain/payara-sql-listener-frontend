package net.c5h8no4na.sqllistener.frontend;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
				for (Entry<List<String>, Integer> stackTraces : infos.getStackTraces().entrySet()) {
					Integer count = result.computeIfAbsent(stackTraces.getKey().get(0), k -> Integer.valueOf(0));
					result.put(stackTraces.getKey().get(0), count + stackTraces.getValue());
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
				result.put(infos.getSql(), count + infos.getTotalCount());
			}
		}

		return result;
	}

	public Map<String, Integer> getByStackFrame(String firstFrame) {
		Map<String, Integer> result = new HashMap<>();

		for (SQLInfoStructure sqlInfoStructure : input) {
			for (SQLQuery infos : sqlInfoStructure.getQueries().values()) {
				for (Entry<List<String>, Integer> stackTrace : infos.getStackTraces().entrySet()) {
					if (firstFrame.equals(stackTrace.getKey().get(0))) {
						Integer count = result.computeIfAbsent(infos.getSql(), key -> Integer.valueOf(0));
						result.put(infos.getSql(), count + stackTrace.getValue());
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
					for (Entry<List<String>, Integer> stackTrace : infos.getStackTraces().entrySet()) {
						Integer count = result.computeIfAbsent(stackTrace.getKey().get(0), key -> Integer.valueOf(0));
						result.put(stackTrace.getKey().get(0), count + stackTrace.getValue());
					}
				}
			}
		}

		return result;
	}
}
