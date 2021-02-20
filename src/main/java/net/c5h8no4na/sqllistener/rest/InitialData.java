package net.c5h8no4na.sqllistener.rest;

import java.util.Map;
import java.util.Queue;

import net.c5h8no4na.sqllistener.PreparedStatementData;

public class InitialData {
	private Queue<PreparedStatementData> tickerEntries;
	private int currentQueryCount;
	private Map<String, Integer> groupByStackFrame;
	private Map<String, Integer> groupBySQL;
	private StatusReportType listenerStatus;

	public Queue<PreparedStatementData> getTickerEntries() {
		return tickerEntries;
	}

	public void setTickerEntries(Queue<PreparedStatementData> tickerEntries) {
		this.tickerEntries = tickerEntries;
	}

	public int getCurrentQueryCount() {
		return currentQueryCount;
	}

	public void setCurrentQueryCount(int currentQueryCount) {
		this.currentQueryCount = currentQueryCount;
	}

	public Map<String, Integer> getGroupByStackFrame() {
		return groupByStackFrame;
	}

	public void setGroupByStackFrame(Map<String, Integer> groupByStackFrame) {
		this.groupByStackFrame = groupByStackFrame;
	}

	public Map<String, Integer> getGroupBySQL() {
		return groupBySQL;
	}

	public void setGroupBySQL(Map<String, Integer> groupBySQL) {
		this.groupBySQL = groupBySQL;
	}

	public StatusReportType getListenerStatus() {
		return listenerStatus;
	}

	public void setListenerStatus(StatusReportType listenerStatus) {
		this.listenerStatus = listenerStatus;
	}

}
