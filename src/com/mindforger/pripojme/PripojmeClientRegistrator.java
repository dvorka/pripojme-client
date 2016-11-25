package com.mindforger.pripojme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class PripojmeClientRegistrator {
	
	private static final String NODE_BASE_URL = "http://localhost:3000/api/aq-detectors";
	
	private Random rand;
	
	public PripojmeClientRegistrator() {
		rand = new Random();
	}

	/*
		{
		    "0004A30B001A1CC7": {
		        "latitude": 49.7709579,
		        "longitude": 15.8470745
		    },
		    "0004A30B001A82A8": {
		        "latitude": 50.09,
		        "longitude": 14.5
		    }
		}	 
	 */
	private static final String EUD_PREFIX = "A16EEEEEEEE";
	private static final String JT = "0004A30B001A82A8";
	private static final String LUKY = "0004A30B001A1CC7";
	
	private void register(String eud, String lat, String log) throws Exception {
		String message = "{\""+eud+"\":{\"latitude\":"
				+lat+
				",\"longitude\":"
				+log+
				"}}";
		doHttpPost(message);
	}
	
	private void registerAll() throws Exception {
		int sensorCount = 50;

		String eud;
		String lat=randomInteger(49, 51)+"."+randomInteger(10, 99)+"09579";
		String log=randomInteger(14, 15)+"."+randomInteger(10, 99)+"70745";
		register(JT,lat,log);
		register(LUKY,lat,log);
		
		for(int i=1;i<=sensorCount;i++) {
			eud=EUD_PREFIX+String.format("%1$05d", i);
			lat=randomInteger(49, 51)+"."+randomInteger(10, 99)+"09579";
			log=randomInteger(14, 15)+"."+randomInteger(10, 99)+"70745";
			register(eud,lat,log);
		}
	}
	
	public  void doHttpPost(String message) throws Exception {
		URL url = new URL(NODE_BASE_URL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
	    conn.setDoOutput(true);
	    //conn.setRequestProperty ("Authorization", encodedCredentials);

	    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

	    writer.write(message);
	    writer.flush();
	    String line;
	    BufferedReader reader = new BufferedReader(new 
	                                     InputStreamReader(conn.getInputStream()));
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	    }
	    writer.close();
	    reader.close();
	}
	
	public int randomInteger(int min, int max) {
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static void main(String[] args) throws Exception {
		PripojmeClientRegistrator registrator = new PripojmeClientRegistrator();
		registrator.registerAll();
	}
}
