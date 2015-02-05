package org.zippert.domoticz;

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

public class SwitchesFragment extends Fragment {
    private static final String URL_SUFFIX_GETSWITCHES = "/json" + ".htm?type=command&param=getlightswitches";
    private static final String URL_SUFFIX_TOGGLESWITCH = "/json" + ".htm?type=command&param=switchlight&idx=%d&switchcmd=%s&level=0";
    private static final String URL_SUFFIX_SWITCHDATA = "/json.htm?type=devices&rid=%d";
    public static String TAG = "start";
    private SwitchesAdapter mSwitchAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView info = (TextView)rootView.findViewById(R.id.info);
        ListView listView = (ListView)rootView.findViewById(R.id.list);
        mSwitchAdapter = new SwitchesAdapter(getActivity().getApplicationContext());
        listView.setAdapter(mSwitchAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                SwitchesData switchesData = (SwitchesData)mSwitchAdapter.getItem(index);
                new ToggleSwitchAsyncTask(context, switchesData, view)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        new GetSwitchDataAsyncTask(context, mSwitchAdapter)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return rootView;
    }

    private static class SwitchesAdapter extends BaseAdapter {
        private Context mContext;
        private SwitchesData[] mData = new SwitchesData[0];

        public SwitchesAdapter(Context context) {
            mContext = context;
        }

        void swapData(SwitchesData[] switchesData) {
            mData = switchesData;
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
            SwitchesData data = mData[position];

            status.setBackgroundResource(
                    data.isOn() ? R.drawable.status_on : R.drawable.status_off);
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

        private static final String ON = "On";
        private static final String OFF = "Off";
        private Context mContext;
        private SwitchesData mSwitchesData;
        private View mView;

        public ToggleSwitchAsyncTask(Context context, SwitchesData switchData, View view) {
            mContext = context;
            mSwitchesData = switchData;
            mView = view;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://" + SharedPrefUtils.getWebserviceAddress(mContext) + String
                    .format(URL_SUFFIX_TOGGLESWITCH, mSwitchesData.getIdx(),
                            mSwitchesData.isOn() ? OFF : ON);
            //TODO: Verify success
            mSwitchesData.setIsOn(!mSwitchesData.isOn());
            return RequestUtility.getJSONResponse(url, SharedPrefUtils.getLoginInfo(mContext));
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
            mView.findViewById(R.id.status).setBackgroundResource(
                    mSwitchesData.isOn() ? R.drawable.status_on : R.drawable.status_off);
        }
    }

    static class GetSwitchDataAsyncTask extends AsyncTask<String, Void, SwitchesData[]> {
        private Context mContext;
        private BaseAdapter mAdapter;

        public GetSwitchDataAsyncTask(Context context, BaseAdapter adapter) {
            mContext = context;
            mAdapter = adapter;
        }

        @Override
        protected SwitchesData[] doInBackground(String... params) {
            SwitchesData[] switches;
            try {
                String urlSwitches = "http://" + SharedPrefUtils
                        .getWebserviceAddress(mContext) + URL_SUFFIX_GETSWITCHES;
                String urlSwitchData = "http://" + SharedPrefUtils.getWebserviceAddress(mContext) +
                        URL_SUFFIX_SWITCHDATA;
                JSONObject main = new JSONObject(RequestUtility
                        .getJSONResponse(urlSwitches, SharedPrefUtils.getLoginInfo(mContext)));
                switches = SwitchesData.parse(main);
                for (SwitchesData data : switches) {
                    int idx = data.getIdx();
                    SwitchData sData = SwitchData.parse(new JSONObject(RequestUtility
                            .getJSONResponse(String.format(urlSwitchData, idx),
                                    SharedPrefUtils.getLoginInfo(mContext))));
                    data.setIsOn(sData.isOn());
                }
            } catch (JSONException e) {
                return null;
            }
            return switches;
        }

        @Override
        protected void onPostExecute(SwitchesData[] switchesData) {
            if (switchesData != null) {
                ((SwitchesAdapter)mAdapter).swapData(switchesData);
            } else {
                Toast.makeText(mContext, "Error when requesting data", Toast.LENGTH_LONG).show();
            }


        }
    }
}