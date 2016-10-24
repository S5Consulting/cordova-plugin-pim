package com.s5.plugins;

import org.apache.cordova.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.logging.Level;

import no.point.paypoint.*;

public class Pim extends CordovaPlugin implements PayPointListener {
    private IPayPoint pim;
    private AndroidLogger logger;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {

            try {
                initPIM("192.168.044.231");
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
        } else if (action.equals("trans")) {
            startTrans();
        } 
        return false;
    }

    private void initPIM(String ipAddress) {
        try {
            if (pim != null && pim.isOpen()) {
                pim.close();
            }
            if (logger == null) {
                logger = new AndroidLogger();
            }
            logger.setLogLevel(Level.ALL);
            logger.setDebugEnabled(true);

            pim = PayPointFactory.createPayPoint(logger);
            pim.open(ipAddress, 0, "v1.0.0", PayPoint.PROTOCOL_ETHERNET);
            pim.setEcrLanguage(PayPoint.LANG_ENG);
            //pim.startTestCom();
        }   
        catch (Exception e) {

        }
    }

    public void startTrans() {

        try {
            float amountFloat = Float.parseFloat("100");
            int amount = (int) (amountFloat * 100);
            pim.startTransaction(PayPoint.TRANS_CARD_PURCHASE, amount, 0, PayPoint.MODE_NORMAL);
        } catch (Exception e) {

        }
    }

    @Override
	public void getPayPointEvent(final PayPointEvent event) { 
        
        switch(event.getEventType()){
        	case PayPointEvent.STATUS_EVENT:

                PayPointStatusEvent statusEvent = (PayPointStatusEvent) event;
                if (statusEvent.getStatusType() == PayPointStatusEvent.STATUS_DISPLAY) {

                } else{

                } 
            break;
            case PayPointEvent.RESULT_EVENT:

                PayPointResultEvent resultEvent = (PayPointResultEvent) event;
                String receipt = resultEvent.getNormalPrint();
                if (resultEvent.getSignaturePrint() != null)
                    receipt = receipt + System.getProperty("line.separator")
                            + resultEvent.getSignaturePrint()
                            + System.getProperty("line.separator");

                String resultText = "Result:        " + resultEvent.getResult()
                        + System.getProperty("line.separator") + "Accumulator:   "
                        + resultEvent.getAccumulator()
                        + System.getProperty("line.separator") + "LocalModeData: "
                        + resultEvent.getLocalModeData()
                        + System.getProperty("line.separator") + "Receipt:       "
                        + System.getProperty("line.separator") + receipt
                        + System.getProperty("line.separator");
            break;
		}
	}
}










