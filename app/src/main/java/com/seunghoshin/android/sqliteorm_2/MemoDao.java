package com.seunghoshin.android.sqliteorm_2;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by SeungHoShin on 2017. 6. 11..
 */

public class MemoDao {

    DBHelper helper;
    Dao<Memo,Integer> dao;


    private  MemoDao(Context context){
        // 데이터베이스 연결
        helper = DBHelper.getInstance(context);
        try {
            dao = helper.getDao(Memo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
