package com.seunghoshin.android.sqliteorm_2;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by SeungHoShin on 2017. 6. 11..
 */

public class BbsDao {
    DBHelper helper;
    Dao<Bbs, Integer> dao;

    private static BbsDao instance = null;


    public static BbsDao getInstance(Context context) {

        if (instance == null) {
            instance = new BbsDao(context);
        }
        return instance;
    }


    // 처음부터 만들어질때 context를 인자를 받게끔 설계한다
    private BbsDao(Context context) {
        // 데이터 베이스 연결
        helper = DBHelper.getInstance(context);
        try {
            // 중복되기때문에 애초에 생성자에 넣어준다
            dao = helper.getDao(Bbs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void create(Bbs bbs) {
        try {
            dao.create(bbs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 모두 읽어오기
    public List<Bbs> read() {

        List<Bbs> datas = null;
        try {
            datas = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datas;
    }


    // 하나 읽어오기
    public Bbs read(int bbsid) {
        Bbs bbs = null;
        try {
            bbs = dao.queryForId(bbsid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bbs;
    }


    // 데이터 검색하기
    public List<Bbs> search(String word) {
        List<Bbs> datas = null;
        try {
            String query = "select * from memo where content like '%" + word + "%'";
            GenericRawResults<Bbs> temps = dao.queryRaw(query, dao.getRawRowMapper());
            datas = temps.getResults();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    // 수정
    public void update(Bbs bbs) {
        try {
            dao.update(bbs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 전체삭제
    public void delete(Bbs bbs) {
        try {
            dao.delete(bbs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 하나 삭제
    public void delete(int id) {
        try {
            dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
