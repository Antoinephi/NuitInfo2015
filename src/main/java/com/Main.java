package com;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import com.google.gson.Gson;

import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;


public class Main {
	
    private static Gson gson = new Gson();


    public static void main(final String[] args) throws SQLException {
    	
    	String dbHost = "172.28.1.137";
    	String dbPort = "5432";
    	String dbName = "nuitinfo";
    	String dbUser = "postgres";
    	String dbPass = "postgres";

    	new PostgresQuirks();
        Spark.staticFileLocation("/public");
        Spark.port(8080);
        
        Sql2o sql2o = new Sql2o("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName, dbUser, dbPass, new PostgresQuirks() {
            {
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new AppModel(sql2o);
        
        Spark.get( "/", ( request , response ) -> {
        	Map<String, Object> map = new HashMap<>();
    		return new ModelAndView(map, "main.ftl");
        }, new FreeMarkerEngine());
        
        
        Spark.get( "/test", ( request , response ) -> {
        	Map<String, Object> map = new HashMap<>();
        	map.put("page_title", "Timeline");
    		return new ModelAndView(map, "main.ftl");
        }, new FreeMarkerEngine());
        
        Spark.post("/location", "application/json",( request , response ) -> {
        	double longitude = Double.parseDouble(request.queryParams("longitude"));
        	double latitude = Double.parseDouble(request.queryParams("latitude"));
        	System.out.println("x : " + latitude + " y : " + longitude);
        	model.addLocation(latitude, longitude, 1);     	
        	response.status(200);
        	return "";
        });
        
        Spark.get("/location", (request, response) -> {
        	model.getLastLocationByUser();
        	return gson.toJson(model.getLastLocationByUser());
        	//{'locations':[{'client_id':'', 'latitude':'', 'longitude':''}, {'client_id':'', 'latitude':'', 'longitude':''}]};
    	});
        
        Spark.get("/check", (request, response) -> {
        	System.out.println("CHECK");
        	double longitude = Double.parseDouble(request.queryParams("longitude"));
        	double latitude = Double.parseDouble(request.queryParams("latitude"));
        	;
        	response.status(200);
        	return model.checkDanger(longitude, latitude) ? "1" : "0";
    	});
        
    }
    

}
