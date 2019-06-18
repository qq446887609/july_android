package cn.tasays.www.july.Receiver;

import org.json.JSONException;
import org.json.JSONObject;


import cn.jpush.android.api.JPushInterface;
import cn.tasays.www.july.R;
import cn.tasays.www.july.activity.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


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


        // 可能经常用到的一点，获取附加的自定义的字段、
        // 这个字符串就是Json的格式，用于自己的服务器给特定的客户端传递一些特定的属性和配置，
        // 例如显示一些数字、特定的事件，或者是访问特定的网址的时候，使用extras
        // 例如显示订单信息、特定的商品列表，特定的咨询网址
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);


        // 使用广播或者通知进行内容的显示
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentText(message).setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Message");
        builder.setDefaults(Notification.DEFAULT_SOUND);


        Log.i("Jpush", extras + "~~");

        int drawResId = R.drawable.ic_launcher;
        int num = 0;
        String title = "hello";
        int iconType = 0;

        /**
         * 自定义信息： 获取
         * */
        if (extras != null) {
            try {
                JSONObject object = new JSONObject(extras);
                num = object.optInt("num");
                title = object.optString("title", "hello");
                iconType = object.optInt("iconType");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        builder.setContentText(title);

        // 不同的节日不同的推送  图标
        switch (iconType) {
            case 0://推送图标1
                drawResId = R.drawable.ic_launcher;
                break;
            case 1://推送图标2
                drawResId = R.drawable.ic_launcher;
                break;


            default:
                break;
        }

        builder.setSmallIcon(drawResId);

        if (num > 0) {
            builder.setNumber(num);
        }


        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);


        manager.notify(1, notification);
        // 使用广播或通知进行内容的显示


        // Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        // msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        // if (!ExampleUtil.isEmpty(extras)) {
        // try {
        // JSONObject extraJson = new JSONObject(extras);
        // if (null != extraJson && extraJson.length() > 0) {
        // msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        // }
        // } catch (JSONException e) {
        //
        // }
        //
        // }
        // context.sendBroadcast(msgIntent);
        // }
    }

}