package net.c5h8no4na.sqllistener.rest;

import java.util.Map;

public class DetailsByKey {
	public final String key;
	public final Map<String, Integer> entries;

	public DetailsByKey(String key, Map<String, Integer> entries) {
		this.key = key;
		this.entries = entries;
	}
}
