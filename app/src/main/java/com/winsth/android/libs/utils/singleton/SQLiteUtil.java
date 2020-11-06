package com.winsth.android.libs.utils.singleton;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class SQLiteUtil extends SQLiteOpenHelper {
    private SQLiteDatabase mSQLiteDatabase;

    private List<String> mSQLList = null;
    private List<String> mTableList = null;

    // 一般单例都是五种写法:懒汉、恶汉、双重校验锁、枚举和静态内部类，下面使用的是静态内部类的写法。
    private static SQLiteUtil instance;

    public static SQLiteUtil getInstance(Context context, String name, int version) {
        if (instance == null) {
            synchronized (SQLiteUtil.class) {
                if (instance == null) {
                    instance = new SQLiteUtil(context, name, version);
                }
            }
        }
        return instance;
    }

    /**
     * 构造函数
     *
     * @param context 上下文环境
     * @param name    数据库名字（例如：databse.db）
     * @param version 数据库版本（例如：1.0）
     */
    private SQLiteUtil(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String strmCreateTableSQL : mSQLList) {
            db.execSQL(strmCreateTableSQL);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            for (String strTableName : mTableList) {
                String strDeleteSQL = String.format("DROP TABLE IF EXISTS %s", strTableName);
                db.execSQL(strDeleteSQL);
            }
            onCreate(db);
        }
    }

    /**
     * 设置要创建的所有数据表的语句（必须设置）
     *
     * @param sqlList 要创建的所有数据表的语句
     */
    public void setSQLList(List<String> sqlList) {
        this.mSQLList = sqlList;
    }

    /**
     * 设置已创建的所有表的表名（必须设置）
     *
     * @param tableList 已创建的所有表的表名
     */
    public void setTableList(List<String> tableList) {
        this.mTableList = tableList;
    }

    /**
     * 获取数据库操作实例
     * @return 返回数据库操作实例
     */
    public SQLiteDatabase getDatabase() {
        if (mSQLiteDatabase == null) {
            mSQLiteDatabase = getWritableDatabase();
        }

        return mSQLiteDatabase;
    }

    /**
     * 销毁数据操作对象
     */
    public void close() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
    }
}
