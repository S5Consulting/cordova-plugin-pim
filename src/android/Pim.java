package com.s5.plugins;

import org.apache.cordova.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import no.point.paypoint.*;

public class Pim extends CordovaPlugin implements PayPointListener {

	PayPoint myPayPoint;
	CallbackContext _callbackcontext;

	public Pim() {
		myPayPoint = new PayPoint();
	}

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    	_callbackcontext = callbackContext;
    	if (action.equals("open")) {

			try {
				myPayPoint.open("192.137.075.123:8448", 0, "v1.0.0", PayPoint.PROTOCOL_ETHERNET);
				callbackContext.success("OK");
				return true;
			} catch (IllegalAppVersionException e) {
				callbackContext.error("Error IllegalAppVersionException");
				return false;
			} catch (ComAlreadyInitialisedException e) {
				callbackContext.error("Error ComAlreadyInitialisedException");
				return false;
			} catch (ComNotInitialisedException e) {
				callbackContext.error("Error ComNotInitialisedException");
				return false;
			} catch (IllegalIpAddressException e) {
				callbackContext.error("Error IllegalIpAddressException");
				return false;
			}
			catch (NoClassDefFoundError e) {
				callbackContext.error("Error NoClassDefFoundError");
				return false;
			}
		}
		return false;
    }

    @Override
	public void getPayPointEvent(PayPointEvent event) {
  		PayPointResultEvent result;
        PayPointStatusEvent status;

        _callbackcontext.success("Whooho!");
 
        switch(event.getEventType()){
        	case PayPointEvent.STATUS_EVENT:
            		// Treat status event
                status = (PayPointStatusEvent)event;
                if(status.getStatusType()==PayPointStatusEvent.STATUS_DISPLAY){
                	// Treat display
                }else if(status.getStatusType()==PayPointStatusEvent.STATUS_CARD_INFO){
                	// Treat card info
                }else if(status.getStatusType()==PayPointStatusEvent.STATUS_READY_FOR_TRANS){
                    // Terminal ready for transaction
                }
            break;
            case PayPointEvent.RESULT_EVENT:
            	// Treat transaction result
                result = (PayPointResultEvent)event;
                // Check if result ok
                if(result.getResult()==PayPointResultEvent.RESULT_OK){
                	if(result.getAccumulator()==PayPointResultEvent.ACCU_APPROVED_ONLINE){
                    	// Financial transaction approved online
                    } else if(result.getAccumulator()==PayPointResultEvent.ACCU_APPROVED_OFFLINE){
                    	// Financial transaction approved offline
                    } else {
                    	// Administrative transaction ok
                    }
                } else {
                    	// Declined/cancelled transaction
                }
            break;
		}
	}
}










