package org.zippert.domoticz;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView info = (TextView)rootView.findViewById(R.id.info);
            new DoRequestAsyncTask(info).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    "http://zippert.asuscomm.com:8080/json" + ".htm?type=command&param=getlightswitches",
                    Base64.encodeToString((USERNAME + ":" + PASSWORD).getBytes(), Base64.DEFAULT));
            return rootView;
        }

        static class DoRequestAsyncTask extends AsyncTask<String, Void, String> {
            private TextView mTextView;

            public DoRequestAsyncTask(TextView output) {
                mTextView = output;
            }

            @Override
            protected String doInBackground(String... params) {
                StringBuffer response = null;
                try {
                    String url = params[0];

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection)obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("Authorization", "Basic " + params[1]);

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'GET' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    //print result
                    System.out.println(response.toString());

                } catch (ClientProtocolException cpe) {
                    return cpe.toString();

                } catch (IOException ioe) {
                    return ioe.toString();
                }

                return response.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                mTextView.setText(s);
            }
        }
    }
}
