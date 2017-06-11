package com.seunghoshin.android.sqliteorm_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

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
    public static DBHelper getInstance(Context context) {

        // singleton pattern 이다
        // getInstance를 호출했는데 new가 계속 호출되기 떄문에 instance가 있으면 생성이 안되게 if문을 걸어준다
        if (instance == null) {
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

        // 업그레이드 테이블
        try {
            TableUtils.createTable(connectionSource, Bbs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
