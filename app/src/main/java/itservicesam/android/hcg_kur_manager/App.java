package itservicesam.android.hcg_kur_manager;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import itservicesam.android.hcg_kur_manager.db.MySQLiteOpenHelper;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "body-data-db");
        DaoMaster.OpenHelper helper = new MySQLiteOpenHelper(this, "body-data-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
