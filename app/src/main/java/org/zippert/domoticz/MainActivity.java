package org.zippert.domoticz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPrefUtils.getLoginInfo(this.getApplicationContext()) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, ConnectFragment.newInstance(),
                            ConnectFragment.TAG).commit();
        } else if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, SwitchesFragment.newInstance(), SwitchesFragment.TAG)
                    .commit();

        }
    }
}
