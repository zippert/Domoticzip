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
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SwitchesFragment extends Fragment {
    private static final String URL_SUFFIX_GETSWITCHES = "/json" + ".htm?type=command&param=getlightswitches";
    private static final String URL_SUFFIX_TOGGLESWITCH = "/json" + ".htm?type=command&param=switchlight&idx=%d&switchcmd=%s&level=0";
    private static final String URL_SUFFIX_SWITCHDATA = "/json.htm?type=devices&rid=%d";
    public static String TAG = "start";
    private SwitchesAdapter mSwitchAdapter;

    public static SwitchesFragment newInstance() {
        return new SwitchesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView infoText = (TextView)rootView.findViewById(R.id.info);
        ListView listView = (ListView)rootView.findViewById(R.id.list);
        mSwitchAdapter = new SwitchesAdapter(getActivity().getApplicationContext());
        listView.setAdapter(mSwitchAdapter);

        new GetSwitchDataAsyncTask(context, mSwitchAdapter, infoText)
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            if (convertView == null) {
                view = View.inflate(mContext, R.layout.switch_layout, null);
            } else {
                view = convertView;
            }
            Switch status = (Switch)view.findViewById(R.id.status);
            TextView text2 = (TextView)view.findViewById(R.id.text2);
            SwitchesData data = mData[position];
            status.setChecked(data.isOn());
            text2.setText(data.getName());

            status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SwitchesData switchesData = (SwitchesData)getItem(position);
                    new ToggleSwitchAsyncTask(mContext, switchesData, view)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
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
            if (Debug.DOLOGGING) {
                Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
            }
            ((Switch)mView.findViewById(R.id.status)).setChecked(mSwitchesData.isOn());
        }
    }

    static class GetSwitchDataAsyncTask extends AsyncTask<String, Void, SwitchesData[]> {
        private Context mContext;
        private BaseAdapter mAdapter;
        private TextView mInfoText;

        public GetSwitchDataAsyncTask(Context context, BaseAdapter adapter,
                                      TextView infoTextView) {
            mContext = context;
            mAdapter = adapter;
            mInfoText = infoTextView;
        }

        @Override
        protected void onPreExecute() {
            mInfoText.setText(R.string.loadingData);
            mInfoText.setVisibility(View.VISIBLE);
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
                mInfoText.setVisibility(View.GONE);
                ((SwitchesAdapter)mAdapter).swapData(switchesData);
            } else {
                mInfoText.setText(R.string.loadingFailed);
                Toast.makeText(mContext, "Error when requesting data", Toast.LENGTH_LONG).show();
            }
        }
    }
}