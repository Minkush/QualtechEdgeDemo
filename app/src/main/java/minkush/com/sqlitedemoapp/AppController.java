package minkush.com.sqlitedemoapp;

import android.app.Application;

import minkush.com.sqlitedemoapp.database.SqliteController;

/**
 * Created by minkush on 20/02/18.
 */

public class AppController extends Application {

    SqliteController controller;
    public static AppController appController;

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
    }

    //app controller instance
    public static synchronized AppController getInstance() {
        return appController;
    }

    //sqlite controller instance
    public SqliteController getSqliteInstance(){
        if (controller == null) {
            controller = new SqliteController(this);
        }
        return controller;
    }


}
