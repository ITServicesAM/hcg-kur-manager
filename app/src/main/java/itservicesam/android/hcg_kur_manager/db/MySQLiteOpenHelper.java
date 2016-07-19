package itservicesam.android.hcg_kur_manager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import itservicesam.android.hcg_kur_manager.BodyDataDao;
import itservicesam.android.hcg_kur_manager.DaoMaster;

/**
 * Created by Growth on 2016/3/3.
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.getInstance().migrate(db, BodyDataDao.class);
    }
}