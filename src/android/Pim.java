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
            	myPayPoint.open("COM1", "v1.0.1");
            	callbackContext.success("Called open without errors");
            	return true;
            } catch (IllegalAppVersionException e) {
                callbackContext.error(e.toString());
                return false;
            } catch (ComAlreadyInitialisedException e) {
            	callbackContext.error(e.toString());
            	return false;
            } catch (ComNotInitialisedException e) {
                callbackContext.error(e.toString());
                return false;
            } catch (IllegalIpAddressException e) {
            	callbackContext.error(e.toString());
                return false;
            }
    	}
    }
}