package org.zippert.domoticz;

/**
 * Created by 23054966 on 20/02/15.
 */
public class Debug {

    public static final boolean DOLOGGING = BuildConfig.DEBUG;

    public static void debug(String debugString){
        if(DOLOGGING){
            System.out.println(debugString);
        }
    }
}
