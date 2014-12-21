package org.zippert.domoticz;

import org.json.JSONArray;

/**
 * Created by 23054966 on 21/12/14.
 */
public abstract interface JsonType {
    public abstract JsonType[] getArray(JSONArray array);
}
