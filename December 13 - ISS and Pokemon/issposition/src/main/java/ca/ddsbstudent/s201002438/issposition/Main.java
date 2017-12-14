package ca.ddsbstudent.s201002438.issposition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        try {

            JsonParser parser = new JsonParser();

            /*
            * This is the URL for getting the lat and long of the ISS.
            */
            HttpURLConnection posURLConnection = (HttpURLConnection) new URL("http://api.open-notify.org/iss-now.json").openConnection();
            JsonObject root = parser.parse(new InputStreamReader(posURLConnection.getInputStream())).getAsJsonObject();

            /*
            * Get the position stuff, from our root object.
            */
            JsonObject position = root.get("iss_position").getAsJsonObject();

            float latitude = position.get("latitude").getAsFloat();
            float longitude = position.get("longitude").getAsFloat();

            System.out.println("The ISS is at " + latitude + ", " + longitude);

            /*
            * The latitude and longitude of our school.
            */
            String passLat = "43.872647";
            String passLon = "-78.945192";

            HttpURLConnection passURLConnection = (HttpURLConnection) new URL("http://api.open-notify.org/iss-pass.json?lat=" + passLat + "&lon=" + passLon).openConnection();
            JsonArray responses = parser.parse(new InputStreamReader(passURLConnection.getInputStream())).getAsJsonObject().get("response").getAsJsonArray();

            /*
            * This loops through the response array. Now we can get the duration and risetime.
            */
            for(JsonElement resp : responses) {
                JsonObject response = resp.getAsJsonObject();
                long risetime = response.get("risetime").getAsLong();
                int duration = response.get("duration").getAsInt();
                /*
                * If you print a date object it shows up as Fri Dec 15 13:14:47 EST 2017
                *   If you want to you could also format this in some format like yyyy-mm-dd or dd-mm-yyyy
                *
                * The "risetime" was in seconds, new Date(long) uses milliseconds since the unix epoch (Jan. 1, 1970).
                *   1 second = 1000 milliseconds
                * So just multiply by 1000.
                */
                System.out.println("The ISS will pass Henry Street at " + new Date(risetime * 1000) + " for " + duration + " seconds.");
            }
        } catch (IOException e) {
            /*
            * This means an error happened.
            */
            e.printStackTrace();
        }
    }
}
