package com.example.makabaka.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.makabaka.utils.Constants;

public class DBHelper  extends SQLiteOpenHelper {

    public DBHelper(Context context) {

        super(context, Constants.DB_NAME, null, Constants.DB_VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        //订阅相关字段
        String subTbSql="create table "+Constants.SUB_TB_NAME+"(" +
                ""+Constants.SUB_ID+" INTEGER primary key autoincrement," +
                ""+Constants.SUB_COVER_URL+" VARCHAR," +
                ""+Constants.SUB_TITLE+" VARCHAR," +
                ""+Constants.SUB_DESCRIPTION+" VARCHAR," +
                ""+Constants.SUB_TRACKS_COUNT+" INTEGER," +
                ""+Constants.SUB_PLAY_COUNT+" INTEGER," +
                ""+Constants.SUB_AUTHOR_NAME+" VARCHAR," +
                ""+Constants.SUB_ALBUM_ID+" INTEGER" +
                ")";
        db.execSQL(subTbSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
