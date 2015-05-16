package org.apache.cordova.core;

import com.parse.ParsePushBroadcastReceiver;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import com.parse.ManifestInfo;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseNotificationManager;
import com.parse.TaskStackBuilderHelper;
import com.parse.NotificationCompat.Builder;
import com.parse.NotificationCompat.Builder.BigTextStyle;

import java.lang.Integer;
import java.lang.Override;
import java.util.Locale;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomReceiver extends ParsePushBroadcastReceiver {
    private static Integer lastNotificationId = Integer.valueOf(System.currentTimeMillis());

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        JSONObject pushData = null;

        try {
            pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
        } catch (JSONException var7) {
            Parse.logE("com.parse.ParsePushReceiver", "Unexpected JSONException when receiving push data: ", var7);
        }

        String action = null;
        if(pushData != null) {
            action = pushData.optString("action", (String)null);
        }

        if(action != null) {
            Bundle notification = intent.getExtras();
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtras(notification);
            broadcastIntent.setAction(action);
            broadcastIntent.setPackage(context.getPackageName());
            context.sendBroadcast(broadcastIntent);
        }

        Notification notification1 = this.getNotification(context, intent);
        if(notification1 != null) {
            showNotification(context, notification1);
        }
    }

    protected void showNotification(Context context, Notification notification) {
        if(context != null && notification != null) {
            NotificationManager nm = (NotificationManager)context.getSystemService("notification");

            try {
                nm.notify(lastNotificationId.intValue(), notification);
            } catch (SecurityException var6) {
                notification.defaults = 5;
                nm.notify(lastNotificationId.intValue(), notification);
            }
        }

    }
}