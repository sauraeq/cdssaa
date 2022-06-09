package com.equalinfotech.coffee.fcm

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.equalinfotech.learnorteach.notification.NotificationActivity
import com.equalinfotech.coffee.Activity.SplashActivity
import com.equalinfotech.coffee.SharedPreferenceUtils

import com.gogo.gogokull.utils.ConstantUtils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMsgService"

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        println("FCM Token $s")
        Log.d(TAG, "onNewToken: $s")
        SharedPreferenceUtils.getInstance(this).setValue(ConstantUtils.FCM_TOKEN, s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //Displaying data in log
        //It is optional
        //  ToastUtils.displayToast(this, "jsjssjsjsjs");
        Log.e(TAG, "From: " + remoteMessage.notification)
        Log.e(TAG, "From: " + remoteMessage.data)
        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                Map<String, String> data = remoteMessage.getData();
                if (data != null && data.containsKey("notification_type") && data.get("notification_type").equalsIgnoreCase("DATA")) {
                    handleDataPayload(data, remoteMessage.getNotification().getBody());
                } else {
                        handleNotificationPayload(remoteMessage.getNotification().getBody());
                 }

        }else */if (remoteMessage.data.size > 0) {
            val data = remoteMessage.data
            if (data != null && data.containsKey("identifier")) {
                val pushNotification: Intent
                val message = data["msg"]
                if (!TextUtils.isEmpty(
                        SharedPreferenceUtils.getInstance(this)
                            .getStringValue(ConstantUtils.USER_ID, "")
                    )
                ) {
                    pushNotification = Intent(applicationContext, NotificationActivity::class.java)
                    pushNotification.putExtra(ConstantUtils.isNotificationTap, true)
                    pushNotification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                } else {
                    pushNotification = Intent(applicationContext, SplashActivity::class.java)
                    pushNotification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    pushNotification.putExtra("message", message)
                }
                pushNotification.putExtra("message", message)
                val mNotificationManager = MyNotificationManager(
                    applicationContext
                )
                mNotificationManager.showSmallNotification(message, pushNotification)

//                handleDataPayload(data, remoteMessage.getNotification().getBody());
            }
        }
        if (remoteMessage.notification != null) {
            Log.e(
                TAG, "Message NotificationActivity Body: " + remoteMessage.notification!!
                    .body
            )
        }
    }


}