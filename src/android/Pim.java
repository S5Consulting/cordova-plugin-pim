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

    private CallbackContext eventCallback;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {

            try {
                initPIM(args.getString(0));
                callbackContext.success("OK");
                return true;
            } catch (Exception e) {
                callbackContext.error("Error");
                return false;
            }

        } else if (action.equals("trans")) {
            startTrans(args.getString(0));
            return true;
        } else if (action.equals("event.callback")) {
            eventCallback = callbackContext;
            return true;
        } else if (action.equals("print")) {

            return true;
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
            pim.open(ipAddress, 0, "pda001", PayPoint.PROTOCOL_ETHERNET);
            pim.setPayPointListener(this);
            pim.setEcrLanguage(PayPoint.LANG_ENG);
            //startTrans();
        }   
        catch (Exception e) {
            
        }
    }

    public void startTrans(String price) {

        try {
            float amountFloat = Float.parseFloat(price);
            int amount = (int) (amountFloat * 100);
            pim.startTransaction(PayPoint.TRANS_CARD_PURCHASE, amount, 0, PayPoint.MODE_NORMAL);
        } catch (Exception e) {

        }
    }

    public void printRec(String print) {
        try {
            
        } catch (Exception e) {

        }   
    }

    @Override
    public void getPayPointEvent(PayPointEvent event) {
        PluginResult pluginResult;

        switch(event.getEventType()){
            case PayPointEvent.STATUS_EVENT:
                PayPointStatusEvent statusEvent = (PayPointStatusEvent) event;
                if (statusEvent.getStatusType() == PayPointStatusEvent.STATUS_DISPLAY) {                
                    pluginResult = new PluginResult(PluginResult.Status.OK, statusEvent.getStatusData());
                    pluginResult.setKeepCallback(true);
                    eventCallback.sendPluginResult(pluginResult);
                } else{                    
                    pluginResult = new PluginResult(PluginResult.Status.OK, event.getEventType());
                    pluginResult.setKeepCallback(true);
                    eventCallback.sendPluginResult(pluginResult);
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

                    pluginResult = new PluginResult(PluginResult.Status.OK, resultType);
                    pluginResult.setKeepCallback(true);
                    eventCallback.sendPluginResult(pluginResult);
            break;
        }
    }
}