package com.uber.demo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.uber.demo.utils.OkHttpStack;
import java.util.concurrent.TimeUnit;

/**
 * Created by nate on 3/26/15.
 */
public class App extends Application {

    private static App sInstance;

    private RequestQueue mRequestQueue;

    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // OKHttp provides a more consistent transport layer across devices
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // init Volley
        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack(client));
    }

    public static App get() {
        return sInstance;
    }

    public static RequestQueue getQueue() {
        return App.get().mRequestQueue;
    }
}