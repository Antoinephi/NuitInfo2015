package com;

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

}
