package net.c5h8no4na.sqllistener.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import net.c5h8no4na.sqllistener.ExecutedSQLInfos;
import net.c5h8no4na.sqllistener.GlassfishSQLTracer;
import net.c5h8no4na.sqllistener.SQLInfoStructure;
import net.c5h8no4na.sqllistener.SQLQuery;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class SQLListenerService {

	@GET
	@Path("clear")
	public void clear() {
		GlassfishSQLTracer.clear();
	}

	@GET
	@Path("activate")
	public void activate() {
		GlassfishSQLTracer.activate();
	}

	@GET
	@Path("deactivate")
	public void deactivate() {
		GlassfishSQLTracer.deactivate();
	}

	@GET
	@Path("getAll")
	public String getAll() {
		List<QueryResult> result = new ArrayList<>();

		for (SQLInfoStructure entry : GlassfishSQLTracer.getAll().values()) {
			QueryResult q = new QueryResult(entry.getPoolName());

			for (SQLQuery query : entry.getQueries().values()) {
				final Query j = new Query(query.getSql(), query.getCount());
				query.getInfos().stream().forEach(infos -> {
					j.addInfo(new Infos(infos.getStackTrace(), infos.getTimestamp()));
				});
				q.addQuery(j);
			}
			result.add(q);
		}

		Gson gson = new Gson();
		return gson.toJson(result);
	}
}
