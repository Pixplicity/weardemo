package com.pixplicity.weardemo;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;
import com.pixplicity.weardemo.shared.Constants;

public class CameraListenerService extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
                String chatHistory = dataMapItem.getDataMap().getString(Constants.FIELD_CHAT_HISTORY);
                if (chatHistory != null) {
                    // TODO
                } else {
                    // TODO
                }
            }
        }
    }

}
