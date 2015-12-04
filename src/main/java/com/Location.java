package com;

import java.util.Date;

import lombok.Data;

@Data
public class Location {
	private int location_id;
	private float latitude;
	private float longitude;
	private Date timestamp_location;
	private int client_id;
}
