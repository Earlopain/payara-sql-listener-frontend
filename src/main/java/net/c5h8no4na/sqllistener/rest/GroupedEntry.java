package net.c5h8no4na.sqllistener.rest;

public class GroupedEntry {
	public final long timestamp;
	public final String value;

	public GroupedEntry(long timestamp, String value) {
		this.timestamp = timestamp;
		this.value = value;
	}
}
