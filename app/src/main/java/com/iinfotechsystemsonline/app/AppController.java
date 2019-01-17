package com.iinfotechsystemsonline.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by root on 7/8/17.
 */

public class AppController extends Application {
    private static AppController singleton;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public static final String TAG = AppController.class.getSimpleName();
    // The Realm file will be located in Context.getFilesDir() with name "default.realm"

    public static AppController getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

//        JobManager.create(this).addJobCreator(new DemoJobCreator());
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .name("RealmTrial1.realm")
//                .build();
        final RealmConfiguration config = new RealmConfiguration.Builder()
                .name("sample.realm")
                .deleteRealmIfMigrationNeeded() // danger
//                .schemaVersion(16)
//                .migration(new RealmMigrations())
                    .build();
        Realm.setDefaultConfiguration(config);
        init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void init() {
        singleton = this;
    }

    // VOLLEY
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }
}
