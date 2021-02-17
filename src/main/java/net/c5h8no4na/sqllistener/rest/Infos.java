package net.c5h8no4na.sqllistener.rest;

public class Infos {
	private String stackTrace;
	private long timestamp;

	public Infos(String stackTrace, long timestamp) {
		super();
		this.stackTrace = stackTrace;
		this.timestamp = timestamp;
	}
}
