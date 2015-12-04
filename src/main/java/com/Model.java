package com;

import java.util.List;

public interface Model {
	public void addLocation(double latitude, double longitude, int client_id);
	public List<Location> getLastLocationByUser();
	public boolean checkDanger(double latitude, double longitude);
}

