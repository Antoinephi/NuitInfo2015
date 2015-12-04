package com;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class AppModel implements Model {
	
	private Sql2o sql2o;
	
	public AppModel(Sql2o sql2o) {
		this.sql2o = sql2o;
	}


	public void addLocation(double latitude, double longitude, int client_id) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into location(latitude, longitude, timestamp_location, client_id) VALUES (:latitude, :longitude, NOW(), :client_id)")
                    .addParameter("latitude", latitude)
                    .addParameter("longitude", longitude)
                    .addParameter("client_id", client_id)
                    .executeUpdate();
            conn.commit();
        }
	}


	@Override
	public List<Location> getLastLocationByUser() {
          
        try (Connection conn = sql2o.open()) {
            List<Location> locations = conn.createQuery("select location_id, latitude, longitude, max(timestamp_location) as timestamp_location, client_id from location GROUP BY location_id")
                    .executeAndFetch(Location.class);
            return locations;
        }
	}
	
	@Override
	public boolean checkDanger(double longitude, double latitude) {
        try (Connection conn = sql2o.beginTransaction()) {
            int n = conn.createQuery("SELECT COUNT(*) latitude FROM location "+
            				 "WHERE power((:lo)-longitude, 2)+power((:la)-latitude, 2) <= 0.001"
            					)
                    .addParameter("la", latitude)
                    .addParameter("lo", longitude)
                    .executeScalar(Integer.class);
            
            return n == 0;
        }

	}
}
	
