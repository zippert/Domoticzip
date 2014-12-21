package org.zippert.domoticz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 23054966 on 21/12/14.
 */
public class SwitchData implements JsonType {

    private boolean mIsDimmer;
    private String mName;
    private String mSubType;
    private String mType;
    private int mIdx;

    @Override
    public SwitchData[] getArray(JSONArray array) {
        SwitchData[] retVal = new SwitchData[array.length()];
        SwitchData data;
        JSONObject object;
        for (int i = 0; i < array.length(); i++) {
            try {
                data = new SwitchData();
                object = array.getJSONObject(i);
                data.mIsDimmer = object.getBoolean("IsDimmer");
                data.mIdx = object.getInt("idx");
                data.mName = object.getString("Name");
                data.mSubType = object.getString("SubType");
                data.mType = object.getString("Type");
                retVal[i] = data;
            } catch (JSONException e) {
                return null;
            }
        }
        return retVal;
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
