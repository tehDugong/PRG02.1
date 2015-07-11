package org.cs160.prg021;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MobileReceiverService extends WearableListenerService {
    private static final String TAG = "MobileReceiverService";
    private static final String TEXT_SERVICE_PATH= "/text_message";

    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        Log.i(TAG, "Message successfully received!");
        Log.i(TAG, messageEvent.getPath());
    }

}
