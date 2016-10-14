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
				switch (test) {
					case 1:
						throw new IllegalAppVersionException();
					break;
					case 2:
						throw new ComAlreadyInitialisedException();
					break;
					case 3:
						throw new ComNotInitialisedException();
					break;
					case 4:
						throw new IllegalIpAddressException();
					break;
				}
            	//myPayPoint.open("COM1", "v1.0.1");
            	callbackContext.success("Called open without errors");
            	return true;
            } catch (IllegalAppVersionException e) {
                callbackContext.error(e.toString());
                return true;
            } catch (ComAlreadyInitialisedException e) {
            	callbackContext.error(e.toString());
            	return true;
            } catch (ComNotInitialisedException e) {
                callbackContext.error(e.toString());
                return true;
            } catch (IllegalIpAddressException e) {
            	callbackContext.error(e.toString());
                return true;
            }
    	}
    	return false;
    }
}