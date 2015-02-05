package org.zippert.domoticz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 23054966 on 05/02/15.
 */
public class SwitchData {

    private boolean mIsOn;

    public static SwitchData parse(JSONObject jsonObject) {
        //TODO: Implement more data
        SwitchData data = new SwitchData();
        try {
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject object = result.getJSONObject(0);
            data.mIsOn = object.getString("Status").equalsIgnoreCase("On");
        } catch (JSONException e){
            return null;
        }
        return data;
    }

    public boolean isOn() {
        return mIsOn;
    }
}
