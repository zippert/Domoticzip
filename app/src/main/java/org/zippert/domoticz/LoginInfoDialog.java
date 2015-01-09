package org.zippert.domoticz;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Mattias Zippert on 2015-01-05.
 */
public class LoginInfoDialog extends AlertDialog {
    private EditText mLoginText, mPasswordText, mUrlText;
    private Button mOKButton;
    private final View.OnClickListener mOKListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    protected LoginInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_info);
        mLoginText = (EditText) findViewById(R.id.username);
        mPasswordText = (EditText) findViewById(R.id.password);
        mUrlText = (EditText) findViewById(R.id.url);
        mOKButton = (Button) findViewById(R.id.ok_button);

        mOKButton.setOnClickListener(mOKListener);
    }
}
