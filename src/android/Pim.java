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
				int test = 0;

            	//myPayPoint.open("COM1", "v1.0.1");
            	callbackContext.error("Called open without errors");
            	return true;
            } catch (Exception e) {

            }
    	}
    	return false;
    }
}










