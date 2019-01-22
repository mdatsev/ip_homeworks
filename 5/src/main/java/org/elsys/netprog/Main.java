package org.elsys.netprog;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
public class Main {
	
	private static String base = "http://localhost:8081/jersey-rest-homework/";
    private static JSONParser parser = new JSONParser();

	public static void main(String[] args) throws IOException, ParseException, NoSuchAlgorithmException {
		while(true) {
			long time = System.nanoTime();
			HttpURLConnection con1 = connect("info", "GET");
		    BufferedReader br = new BufferedReader(new InputStreamReader(con1.getInputStream()));
		    StringBuilder sb = new StringBuilder();
		    String output;
		    while ((output = br.readLine()) != null) {
		      sb.append(output);
		    }
		    String response = sb.toString();
		    JSONObject obj = (JSONObject) parser.parse(response);
		    String hash = (String) obj.get("HASH");
		    int length = ((Number) obj.get("LENGHT")).intValue();
		    //System.out.println("Got info: hash: " + hash + " length: " + length);
		    byte[] test = new byte[length];
		    String guessed_input = null, guessed_hash = null;
		    do {
		    	new Random().nextBytes(test);
				MessageDigest md = MessageDigest.getInstance("MD5");
			    md.update(test);
			    guessed_hash = Base64.getUrlEncoder().encodeToString(md.digest());
		    	guessed_input = new String(Base64.getUrlEncoder().encode(test));
		    } while(!guessed_hash.equals(hash));
		    HttpURLConnection con2 = connect("guess/" + guessed_hash + "/" + guessed_input, "POST");
		    int code = con2.getResponseCode();
		    if(code == 200) {
		    	System.out.println((System.nanoTime() - time));
		    	//System.out.println("Guessed correct! input: " + guessed_input + " time: " + (System.nanoTime() - time));
		    } else {
		    	//System.out.println(((Integer)code).toString() + "Guessed wrong! input: " + guessed_input);
		    }
		}
	}
	public static HttpURLConnection connect(String path, String method) throws ParseException, IOException {
		URL url = new URL(base + path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(method);
		con.connect();
		return con;
	}
	public static void testHashesPerSecond() throws NoSuchAlgorithmException {
		for(int i = 0; i < 5; i++) {
			long time = System.nanoTime();
			int length = (int) Math.pow(10, i);
			byte[] b = new byte[length];
			int hashes = 1000000;
			for(int j = 0; j < hashes; j++) {
				new Random().nextBytes(b);
				MessageDigest md = MessageDigest.getInstance("MD5");
			    md.update(b);
			    md.digest();
			}
			System.out.println("length: " + length + " hashes/second: " + (10e9 * hashes / (System.nanoTime() - time)));
		}
	}
}
