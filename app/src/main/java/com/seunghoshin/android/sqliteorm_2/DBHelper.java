package com.seunghoshin.android.sqliteorm_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by SeungHoShin on 2017. 6. 10..
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    // todo 질문하기 VERSION = 숫자 변동 값
    // 내안에다가 정의를 한다
    public static final String DATABASE_NAME = "database.db";
    public static final int DATABASE_VERSION = 2;

    // 저장소를 static으로 DBHelper내부에 만들어 준다
    private static DBHelper instance = null;




    // Todo 싱글톤으로 구성해보자
    // DBHelper 를 메모리에 하나만 있게 해서 효율을 높이자 ,
    // DBHelper를 private로 바꾸면서 다른곳에서는 get 할수 있도록 getter을 만들어준다
    // 반환값은 해당값을 DBHelper로 넘겨줘야한다 , 우선은 void로 다 작성한뒤 고친다
    // 또 이 함수는 static이 되어야 하는데 그래야지 BbsDao에서 불러올수 있다
    public static DBHelper getInstance(Context context){

        // singleton pattern 이다
        // getInstance를 호출했는데 new가 계속 호출되기 떄문에 instance가 있으면 생성이 안되게 if문을 걸어준다
        if(instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    // 최초 호출될때 database.db 파일을 /data/data/패키지명/database/디렉토리 ( database.db ) 아래에 파일을 생성하준다
    private DBHelper(Context context) {
        // super은 바꿀수 없다
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 최초에 생성되면 버전체크를 해서 onCreate 를 호출한다
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        // 테이블 생성해야 한다
        try {
            // 최초 생성 테이블
            TableUtils.createTable(connectionSource, Memo.class);
            // 업그레이드 테이블
            TableUtils.createTable(connectionSource, Bbs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 데이터 베이스 버전이 업그레이드 되면 호출된다
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // Memo 테이블의 특정 컬럼만 변경되었을 경우
        // 1. 기존 테이블을 백업하기 위한 임시테이블을 생성하고 데이터를 담아둔다
        //  예) create table temp_memo,   <- 기존데이터
        // 2. Memo 테이블을 삭제하고 다시 생성한다
        // 3. 백업된 데이터를 다시 입력한다
        // 4. 임시 테이블 삭제

        // 업그레읻으 테이블
        try {
            TableUtils.createTable(connectionSource, Bbs.class);
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
            Dao<Memo, Integer> dao = getDao(Memo.class);
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
            Dao<Memo, Integer> dao = getDao(Memo.class);
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
            Dao<Memo, Integer> dao = getDao(Memo.class);
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
            Dao<Memo, Integer> dao = getDao(Memo.class);
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
            Dao<Memo, Integer> dao = getDao(Memo.class);
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
            Dao<Memo, Integer> dao = getDao(Memo.class);
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
            Dao<Memo, Integer> dao = getDao(Memo.class);
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
