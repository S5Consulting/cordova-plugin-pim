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

    private CallbackContext _eventCallback;
    private Exception _exception;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        PluginResult pluginResult = null;
        _exception = null;
        
        if (action.equals("open")) {

            if (initPIM(args.getString(0))) {
                pluginResult = new PluginResult(PluginResult.Status.OK, "OPEN");
                callbackContext.sendPluginResult(pluginResult);
                return true;
            } else {
                pluginResult = new PluginResult(PluginResult.Status.ERROR, _exception.getMessage());
                callbackContext.sendPluginResult(pluginResult);
                return false;
            } 

        } else if (action.equals("callback")) {

            _eventCallback = callbackContext;
            return true;

        } else if (action.equals("trans")) {

            if (startTrans(args.getString(0))) {
                pluginResult = new PluginResult(PluginResult.Status.OK, "TRANSACTION");
                callbackContext.sendPluginResult(pluginResult);
                return true;
            } else {
                pluginResult = new PluginResult(PluginResult.Status.ERROR, _exception.getMessage());
                callbackContext.sendPluginResult(pluginResult);
                return false;
            }

        } else if (action.equals("print") {

            if (printRec(args.getString(0))) {
                pluginResult = new PluginResult(PluginResult.Status.OK, "PRINT");
                callbackContext.sendPluginResult(pluginResult);
                return true;
            } else {
                pluginResult = new PluginResult(PluginResult.Status.ERROR, _exception.getMessage());
                callbackContext.sendPluginResult(pluginResult);
                return false;
            }

        } else {
            return false;
        }
    }

    private boolean initPIM(String ipAddress) {
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
            pim.setPayPointListener(this);
            pim.setEcrLanguage(PayPoint.LANG_ENG);
            return true;
        }   
        catch (Exception e) {
            _exception = e;
            return false;
        }
    }

    public boolean startTrans(String value) {

        try {
            float amountFloat = Float.parseFloat(value);
            int amount = (int) (amountFloat * 100);
            pim.startTransaction(PayPoint.TRANS_CARD_PURCHASE, amount, 0, PayPoint.MODE_NORMAL);
            return true;
        } catch (Exception e) {
            _exception = e;
            return false;
        }
    }

    public boolean printRec(String print) {

        try {
            pim.setAdminData(print, PayPoint.ADM_DATA_PRINT);
            pim.startAdmin(PayPoint.ADM_TERM_PRINT);
            return true;
        } catch (Exception e) {
            _exception = e;
            return false;
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
                    _eventCallback.sendPluginResult(pluginResult);
                } else{                    
                    pluginResult = new PluginResult(PluginResult.Status.OK, event.getEventType());
                    pluginResult.setKeepCallback(true);
                    _eventCallback.sendPluginResult(pluginResult);
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

                    pluginResult = new PluginResult(PluginResult.Status.OK, resultText);
                    pluginResult.setKeepCallback(true);
                    _eventCallback.sendPluginResult(pluginResult);
            break;
        }
    }
}