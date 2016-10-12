package com.s5.pim;

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

        	myPayPoint.open("COM1", "v.1.0.0");
            callbackContext.success("OK");

            return true;

        } else {
            
            return false;

        }
    }
}