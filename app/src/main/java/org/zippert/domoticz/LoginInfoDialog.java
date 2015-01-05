package org.zippert.domoticz;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Mattias Zippert on 2015-01-05.
 */
public class LoginInfoDialog extends AlertDialog {
    protected LoginInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_info);
    }
}
