package org.zippert.domoticz;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

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
