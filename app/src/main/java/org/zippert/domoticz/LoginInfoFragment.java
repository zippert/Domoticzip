package org.zippert.domoticz;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginInfoFragment extends Fragment {
    public static String TAG = "LOGIN_INFO";
    private EditText mLoginText, mPasswordText, mUrlText;
    private Button mOKButton;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.settings_main, null);
        mLoginText = (EditText)mView.findViewById(R.id.username);
        mPasswordText = (EditText)mView.findViewById(R.id.password);
        mUrlText = (EditText)mView.findViewById(R.id.url);
        mOKButton = (Button)mView.findViewById(R.id.ok_button);

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity().getApplicationContext();
                SharedPrefUtils
                        .setWebserviceAddress(context, mUrlText.getText().toString().trim());
                SharedPrefUtils.setLoginInfo(context, Base64.encodeToString(
                        (mLoginText.getText().toString().trim() + ":" + mPasswordText.getText()
                                .toString().trim()).getBytes(), Base64.DEFAULT));
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new SwitchesFragment(), SwitchesFragment.TAG)
                        .commit();
            }
        });
        return mView;
    }
}
