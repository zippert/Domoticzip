package org.zippert.domoticz;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectFragment extends Fragment {

    public static String TAG = "connect";
    private View mView;
    private Button mButton;

    public ConnectFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.connect_layout, null);
        mButton = (Button)mView.findViewById(R.id.connect_button);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(LoginInfoFragment.TAG)
                        .replace(R.id.container, new LoginInfoFragment(), LoginInfoFragment.TAG)
                        .commit();
            }
        });
        return mView;
    }


}
