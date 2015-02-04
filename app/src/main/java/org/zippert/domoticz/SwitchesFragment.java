package org.zippert.domoticz;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SwitchesFragment extends Fragment {
    private static final String URL_SUFFIX_GETSWITCHES = "/json" + ".htm?type=command&param=getlightswitches";
    private static final String URL_SUFFIX_TOGGLESWITCH = "/json" + ".htm?type=command&param=switchlight&idx=%d&switchcmd=%s&level=0";
    private static final String ON = "On";
    private static final String OFF = "Off";
    public static String TAG = "start";
    private SwitchesAdapter mSwitchAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView info = (TextView)rootView.findViewById(R.id.info);
        ListView listView = (ListView)rootView.findViewById(R.id.list);
        mSwitchAdapter = new SwitchesAdapter(getActivity().getApplicationContext());
        listView.setAdapter(mSwitchAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                SwitchData switchData = (SwitchData)mSwitchAdapter.getItem(index);
                new ToggleSwitchAsyncTask(getActivity().getApplicationContext(), switchData.getIdx(),
                        !switchData.getStatus()).executeOnExecutor(AsyncTask
                        .THREAD_POOL_EXECUTOR);
            }
        });
        Context context = getActivity().getApplicationContext();

        new DoRequestAsyncTask(mSwitchAdapter, info)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://" + SharedPrefUtils
                                .getWebserviceAddress(context) + URL_SUFFIX_GETSWITCHES,
                        SharedPrefUtils.getLoginInfo(context));

        return rootView;
    }

    private static class SwitchesAdapter extends BaseAdapter {
        private Context mContext;
        private SwitchData[] mData = new SwitchData[0];

        public SwitchesAdapter(Context context) {
            mContext = context;
        }

        void swapData(SwitchData[] switchData) {
            mData = switchData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = View.inflate(mContext, R.layout.switch_layout, null);
            }
            ImageView status = (ImageView)view.findViewById(R.id.status);
            TextView text2 = (TextView)view.findViewById(R.id.text2);
            SwitchData data = mData[position];
            System.out.println("MZ: " + data.toString());

            //status.setBackground(data.);
            text2.setText(data.getName());
            return view;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return mData[position];
        }
    }

    static class ToggleSwitchAsyncTask extends AsyncTask<Void, Void, String> {

        private Context mContext;
        private int mIdx;
        private boolean mStatusOn;

        public ToggleSwitchAsyncTask(Context context, int idx, boolean statusOn){
            mContext = context;
            mIdx = idx;
            mStatusOn = statusOn;
        }
        @Override
        protected String doInBackground(Void... voids) {
            StringBuffer response = null;
            try {
                String url = "http://" + SharedPrefUtils
                        .getWebserviceAddress(mContext) + String.format(URL_SUFFIX_TOGGLESWITCH,
                        mIdx, mStatusOn ? ON : OFF);

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection)obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                //add request header
                con.setRequestProperty("Authorization", "Basic " + SharedPrefUtils.getLoginInfo
                        (mContext));

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
            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
        }
    }

    static class DoRequestAsyncTask extends AsyncTask<String, Void, String> {
        private TextView mTextView;
        private BaseAdapter mAdapter;

        public DoRequestAsyncTask(BaseAdapter adapter, TextView output) {
            mTextView = output;
            mAdapter = adapter;
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
            try {
                JSONObject main = new JSONObject(s);
                SwitchData[] switches = new SwitchData().getArray(main.getJSONArray("result"));
                ((SwitchesAdapter)mAdapter).swapData(switches);
            } catch (JSONException e) {
                mTextView.setText(e.toString());
            }
        }
    }
}