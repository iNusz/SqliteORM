package com.seunghoshin.android.sqliteorm_2;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by SeungHoShin on 2017. 6. 11..
 */

public class MemoDao {

    DBHelper helper;
    Dao<Memo, Integer> dao;

    private static MemoDao instance = null;

    public static MemoDao getInstance(Context context) {
        if (instance == null) {
            instance = new MemoDao(context);
        }
        return instance;
    }


    private MemoDao(Context context) {
        // 데이터베이스 연결
        helper = DBHelper.getInstance(context);
        try {
            dao = helper.getDao(Memo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


// DBHelper을 통해서 database를 엑세스하기위해 4개의 메소드를 만든다  ( C R U D )

    // Create - 데이터를 입력  , 안에 인자 값은 클래스로 넣어준다
    public void create(Memo memo) {
        try {
            // 1. 테이블에 연결
            // 메모클래스의 테이블에 연결하는 메소드 , Data access object 의 약자 ,
            // 여기서 <> 제네릭 안에 넣어줘야 하는것은 이게 어느곳의 테이블인지 모르게 떄문에 넣어주는것이고 , Integer은 primaryKey 타입이 int라 넣어준것
//            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2. 데이터를 입력
            dao.create(memo);
            // 이 과정이 insert query를 한 과정이다
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ReadAll
    // 여기서 리턴 타입은 리스트를 반환해야함으로 List<Memo>이다
    public List<Memo> readAll() {
        //return 을 받기위해 전역변수로 빼주었다
        List<Memo> datas = null;
        try {
            // 1. 테이블에 연결
//            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2. 데이터를 전체 읽어오기 , List형태로 읽어온다 따라서 리턴타입도 List<Memo>
            datas = dao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    // read one   ,  id값을 인자로 넣어둬야하고 리턴타입은 Memo객체 하나를 넘겨줘야한다 위에는 전부라서 List<>다받아온것이다
    public Memo read(int memoid) {
        Memo memo = null;
        try {
            // 1. 테이블에 연결
//            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2.1 데이터를 한개 읽어오기
            memo = dao.queryForId(memoid);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memo;
    }

    // search - 데이터 검색하기
    public List<Memo> search(String word) {
        //return 을 받기위해 전역변수로 빼주었다
        List<Memo> datas = null;
        try {
            // 1. 테이블에 연결
//            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2 데이터 검색하기 , List형태로 읽어온다 따라서 리턴타입도 List<Memo>
            // TODO  특수문자를 줘서 SQLite 가 +word+를 번역하게 하는 방법알아보기
            // 아래식 때문에 쿼리를 알면 편하게 식을 사용할 수 있다 , Orm tool에 Raw쿼리를 날리는것 , 쿼리를 마음대로 만들어서 검색조건을 만들어서 쓸 수 있다
            String query = "select * from memo where content like '%" + word + "%'";
            // queryRaw 를 자세히 보면 GenericRawResults 형태로 되어있는걸 알 수 있다
            GenericRawResults<Memo> temps = dao.queryRaw(query, dao.getRawRowMapper());
            // 제너릭을 달아줌으로써 Memo 컬랙션으로 리턴이 된다
            datas = temps.getResults();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    // Update
    public void update(Memo memo) {
        try {
            // 1. 테이블에 연결
//            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2. 데이터를 수정
            dao.update(memo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Object
    public void delete(Memo memo) {
        try {
            // 1. 테이블에 연결
//            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2. 데이터를 삭제
            dao.delete(memo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete By Id   @Overloading
    public void delete(int id) {
        try {
            // 1. 테이블에 연결
//            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2. 데이터를 삭제
            dao.deleteById(id);
            // * 참고 : 아이디로 삭제
            // 이때는 인자를 Memo memo로 받는게 아니라 int 로 받아야한다 . 오버로드를 한다
            // dao.deleteById(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
