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
    		callbackContext.success("Open called!");
    		myPayPoint.open("COM1", "v1.0.1");
    		return true;
    	} else {
    		callbackContext.error("Open not called!");
    		return false;
    	}
    }
}