package org.zippert.domoticz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zippert.domoticz.DemoticzConstants.RequestType;

/**
 * Created by 23054966 on 21/12/14.
 */
public class JsonParser {

    public static <T extends JsonType> T[] getArray(T type, String source) {
        try {
            JSONObject jsonResponse = new JSONObject(source);
            if (jsonResponse.has("status") && jsonResponse.getString("status")
                    .equalsIgnoreCase("ok")) {
                JSONArray array = jsonResponse.getJSONArray("result");
                return getArray(jsonResponse.getString("title"), array);
            } else {
                return null;
            }
        } catch (JSONException exception) {
            return null;
        }

    }

    private static <T extends JsonType> T[] getArray(String type, JSONArray array) {

        switch (RequestType.getEnum(type)) {
            case LIST_SWITCHES: {
                return (T[])new SwitchData().getArray(array);
            }
        }
        return null;
    }
}
