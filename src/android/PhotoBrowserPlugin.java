package com.creedon.cordova.plugin.photobrowser;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.creedon.cordova.plugin.photobrowser.data.Datum;
import com.creedon.cordova.plugin.photobrowser.data.PhotoData;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.apache.cordova.PluginResult.Status.OK;

/**
 * This class echoes a string called from JavaScript.
 */
public class PhotoBrowserPlugin extends CordovaPlugin {

    public  static final String KEY_CAPTION = "caption";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ACTION_SEND = "send";
    public static final String KEY_ACTION  = "action";

    private static final String KEY_NAME = "name";
    public static final String DEFAULT_ACTION_RENAME = "rename";
    private static final String DEFAULT_ACTION_EDITCAPTION = "editCaption";
    private CallbackContext callbackContext;
    private PhotoData photoData;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("showGallery")) {
            JSONObject options = args.getJSONObject(0);
            photoData = PhotoData.getInstance(options);
            photoData.setOnCaptionChangeListener(new PhotoData.PhotoDataListener(){

                @Override
                public boolean onCaptionChanged(Datum datum, String caption, String id, String type) {
                    JSONObject res = new JSONObject();
                    try {
                        res.put(KEY_PHOTO,datum.toJSON());
                        res.put(KEY_CAPTION,caption);
                        res.put(KEY_ACTION,DEFAULT_ACTION_EDITCAPTION);
                        res.put(KEY_ID,id);
                        res.put(KEY_TYPE,type);
                        res.put(KEY_DESCRIPTION,"edit caption of photo");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                    PluginResult result = new PluginResult(OK,res);
                    result.setKeepCallback(true);
                    PhotoBrowserPlugin.this.callbackContext.sendPluginResult(result);

                    return true;
                }

                @Override
                public void onSetName(String s, String id, String type) {
                    JSONObject res = new JSONObject();
                    try {
                        
                        res.put(KEY_ACTION,DEFAULT_ACTION_RENAME);
                        res.put(KEY_ID,id);
                        res.put(KEY_TYPE,type);
                        res.put(KEY_NAME,s);
                        res.put(KEY_DESCRIPTION,"edit album name");

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    PluginResult result = new PluginResult(OK,res);
                    result.setKeepCallback(true);
                    PhotoBrowserPlugin.this.callbackContext.sendPluginResult(result);
                }
            });

            this.showGallery(options, callbackContext);
            return true;
        }
        if (action.equals("showBrowser")) {
            String message = args.getString(0);
            this.showBrowser(message, callbackContext);
            return true;
        }
        return false;
    }

    private void showGallery(JSONObject options, CallbackContext callbackContext) {
        if (options != null && options.length() > 0) {


            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            long totalMegs = mi.totalMem / 1048576L;
            System.out.println("[NIX] totalMegs: " + totalMegs);

            Intent intent = new Intent(cordova.getActivity(), PhotoBrowserPluginActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("options",options.toString());
            this.cordova.startActivityForResult(this, intent, 0);

        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void showBrowser(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Constants.RESULT_ADD_PHOTO){

        }
        else if (resultCode == Activity.RESULT_OK && data != null) {

            String result = data.getStringExtra(Constants.RESULT);
            if(result != null) {
                JSONObject res = null;
                try {
                    res = new JSONObject(result);
                    this.callbackContext.success(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{



                PluginResult res = new PluginResult(OK);
                res.setKeepCallback(false);
                PhotoBrowserPlugin.this.callbackContext.sendPluginResult(res);
            }

        } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
            String error = data.getStringExtra("ERRORMESSAGE");
            if (error == null)
                this.callbackContext.error("Error");
            this.callbackContext.error(error);
        } else if (resultCode == Activity.RESULT_CANCELED) {
            JSONObject res = new JSONObject();
            if(this.callbackContext != null )
                this.callbackContext.error(res);

        } else {
            JSONObject res = new JSONObject();
            if(this.callbackContext != null )
                this.callbackContext.error(res);
        }

    }


}
