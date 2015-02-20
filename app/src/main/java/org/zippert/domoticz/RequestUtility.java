package org.zippert.domoticz;

import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 23054966 on 05/02/15.
 */
public class RequestUtility {

    public static String getJSONResponse(String url, String userId){

        StringBuffer response = null;
        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Authorization", "Basic " + userId);

            int responseCode = con.getResponseCode();
            Debug.debug("\nSending 'GET' request to URL : " + url);
            Debug.debug("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            Debug.debug(response.toString());

        } catch (ClientProtocolException cpe) {
            return cpe.toString();

        } catch (IOException ioe) {
            return ioe.toString();
        }

        return response.toString();
    }
}
