package org.zippert.domoticz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 23054966 on 21/12/14.
 */
public class SwitchesData {

    private boolean mIsDimmer;
    private String mName;
    private String mSubType;
    private String mType;
    private int mIdx;
    private boolean mIsOn;

    public static SwitchesData[] parse(JSONObject jsonObject) {
        JSONArray array;
        SwitchesData[] retVal;
        SwitchesData data;
        JSONObject object;
        try {
            array = jsonObject.getJSONArray("result");
            retVal = new SwitchesData[array.length()];
            for (int i = 0; i < array.length(); i++) {
                data = new SwitchesData();
                object = array.getJSONObject(i);
                data.mIsDimmer = object.getBoolean("IsDimmer");
                data.mIdx = object.getInt("idx");
                data.mName = object.getString("Name");
                data.mSubType = object.getString("SubType");
                data.mType = object.getString("Type");
                retVal[i] = data;

            }
        } catch (JSONException e) {
            return null;
        }
        return retVal;
    }

    public void setIsOn(boolean isOn){
        mIsOn = isOn;
    }

    public boolean isOn() {
        return mIsOn;
    }

    @Override
    public String toString() {
        return mName + " " + mIdx;
    }

    public String getName() {
        return mName;
    }

    public String getSubType() {
        return mSubType;
    }

    public String getType() {
        return mType;
    }

    public int getIdx() {
        return mIdx;
    }

    public boolean isDimmer() {
        return mIsDimmer;
    }
}
