package org.zippert.domoticz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPrefUtils.getLoginInfo(this.getApplicationContext()) == null) {
            getFragmentManager().beginTransaction().addToBackStack(LoginInfoFragment.TAG)
                    .add(R.id.container, new LoginInfoFragment(), LoginInfoFragment.TAG)
                    .commit();
        } else {
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new SwitchesFragment(), SwitchesFragment.TAG)
                        .commit();
            }
        }
    }

    @Override
    protected void onResume() {
        if (SharedPrefUtils.getLoginInfo(this.getApplicationContext()) != null) {
            mLoginButton.setVisibility(View.GONE);
        }
        super.onResume();
    }
}
