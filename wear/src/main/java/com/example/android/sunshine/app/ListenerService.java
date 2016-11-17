package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService{

    private static final String WEARABLE_DATA_PATH = "/wearable_data";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            Log.v("myTag", "DataMap received on watch: " + DataMapItem.fromDataItem(event.getDataItem()).getDataMap());
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_DATA_PATH)) {}
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                SharedPreferences settings = getSharedPreferences("favoritesArray", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                String highTemp = String.valueOf(dataMap.getDouble("high"));
                String lowtemp = String.valueOf(dataMap.getDouble("low"));
                String weatherSym = Integer.toString(dataMap.getInt("id"));
                String weatherInfo = highTemp.concat("|" + lowtemp.concat("|" + weatherSym));

                editor.putString("weatherInfo", weatherInfo);
                editor.apply();

                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("datamap", dataMap.toBundle());
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

            }
        }
    }
}
