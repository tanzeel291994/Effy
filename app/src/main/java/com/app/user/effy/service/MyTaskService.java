package com.app.user.effy.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.user.effy.App.Config;
import com.app.user.effy.MainActivity;
import com.app.user.effy.QuoteActivity;
import com.app.user.effy.R;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MyTaskService extends GcmTaskService {

    public static final String ACTION_DONE = "GcmTaskService#ACTION_DONE";
    public static final String EXTRA_TAG = "extra_tag";
    public static final String EXTRA_RESULT = "extra_result";
    private static final String TAG ="MyTaskService" ;
    private OkHttpClient mClient = new OkHttpClient();

    public MyTaskService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        String tag = taskParams.getTag();

        // Default result is success.
        int result = GcmNetworkManager.RESULT_SUCCESS;

        // Choose method based on the tag.
        if (MainActivity.TASK_TAG_WIFI.equals(tag)) {
            result = doWifiTask();}
            Intent intent = new Intent();
            intent.setAction(ACTION_DONE);
            intent.putExtra(EXTRA_TAG, tag);
            intent.putExtra(EXTRA_RESULT, result);

            // Send local broadcast, running Activities will be notified about the task.
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            manager.sendBroadcast(intent);

            // Return one of RESULT_SUCCESS, RESULT_FAILURE, or RESULT_RESCHEDULE
            return result;

    }

    private int doWifiTask() {
        String url = "http://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=json";
        return fetchUrl(mClient, url);
    }
    private int fetchUrl(OkHttpClient client, String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

                String remoteResponse=response.body().string();
                Log.d(TAG, "fetchUrl:response:" + remoteResponse);

            JSONObject obj=new JSONObject(remoteResponse);

            Intent intent = new Intent(this, QuoteActivity.class);
            intent.putExtra("quoteText",obj.get("quoteText").toString());
            intent.putExtra("quoteAuthor",obj.get("quoteAuthor").toString());
// use System.currentTimeMillis() to have a unique ID for the pending intent
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

            Notification n  = new Notification.Builder(this)
                    .setContentTitle("Effy")
                    .setContentText(obj.get("quoteText").toString())
                    .setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.ic_add_circle_white_24px)
                    .setAutoCancel(true).build();


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);

            if (response.code() != 200) {
                return GcmNetworkManager.RESULT_FAILURE;
            }
        } catch (IOException e) {
            Log.e(TAG, "fetchUrl:error" + e.toString());
            return GcmNetworkManager.RESULT_FAILURE;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
