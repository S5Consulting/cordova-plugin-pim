package com.s5.plugins;

import org.apache.cordova.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import no.point.paypoint.*;

public class Pim extends CordovaPlugin {

	PayPoint myPayPoint;

	public Pim() {
		myPayPoint = new PayPoint();
	}

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("open")) {
        	try {
				myPayPoint.open("COM1", "v1.0.0");
				callbackContext.success("OK");
        	} catch (Exception e) {
				callbackContext.success("ERROR");
        	}
        	           
            return true;

        } else {
            
            return false;

        }
    }
}