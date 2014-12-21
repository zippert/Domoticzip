package org.zippert.domoticz;

/**
 * Created by 23054966 on 21/12/14.
 */
public class DemoticzConstants {

    public enum RequestType {
        LIST_SWITCHES("GetLightSwitches");

        private String mValue;

        RequestType(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }

        public static RequestType getEnum(String value) {
            for(RequestType v : values())
                if(v.mValue.equalsIgnoreCase(value)){
                    return v;
                }
            throw new IllegalArgumentException();
        }
    }
}
