package cn.tasays.www.july.Receiver;

import org.json.JSONException;
import org.json.JSONObject;


import cn.jpush.android.api.JPushInterface;
import cn.tasays.www.july.R;
import cn.tasays.www.july.activity.MainActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MyReceiver extends BroadcastReceiver {


    private static final String TAG = "JPush";


    /**
     * @param
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle));
        // 注册ID的广播这个比较重要，因为所有的推送服务都必须，注册才可以额接收消息
        // 注册是在后台自动完成的，如果不能注册成功，那么所有的推送方法都无法正常进行
        // 这个注册的消息，可以发送给自己的业务服务器上。也就是在用户登录的时候，给自己的服务器发送
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            // 所有自定义的消息才会进入这个方法里
            Log.d(TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);


        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            // 所有的普通推送，都会进入到这个部分，并且Jpush自己会进行 Notification的显示
            // 我们只要把notificationId 存起来，或者保存到本地，用于列表的排序.
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {


            // notification点击打开，主要针对是普通的推送消息
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");


            // 打开自定义的Activity
            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(bundle);
            // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..


        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    // send msg to MainActivity

    /**
     * 可能经常用到的一点，获取附加的自定义的字段
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        // if (MainActivity.isForeground) {//检查当前软件是否在前台
        // 利用JPushInterface.EXTRA_MESSAGE 机械能推送消息的获取
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }

}