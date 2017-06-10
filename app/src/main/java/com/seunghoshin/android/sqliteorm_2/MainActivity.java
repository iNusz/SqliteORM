package com.seunghoshin.android.sqliteorm_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 데이터 베이스 사용
        DBHelper helper = new DBHelper(this);
        // 1. 데이터 입력
//        for (int i = 0; i < 10; i++) {
//            Memo memo = new Memo();
//            //memo.setId();  없어도 되는 이유는 우리가 primarykey로 자동 생성해줬기 때문이다
//            //memo.setDate(); 없어도 되는 이유는 Memo가 실행될때에 넣어줬기 떄문이다
//            memo.setTitle("제목");
//            memo.setContent("내용");
//            helper.create(memo);
//        }
//
//        // 2. 데이터 한개 읽어오기
//        Memo one = helper.read(3);
//        Log.i("Memo",one.getId()+"  : title"+one.getTitle()+" , content = "+one.getContent());

        // 3. 데이터 전체 읽어오기
//        List<Memo> datas =  helper.readAll();
//        for(Memo one : datas){
//        Log.i("Memo",one.getId()+"  : title"+one.getTitle()+" , content = "+one.getContent());
//        }

        // 4. 데이터 검색 하기
        // 기본 데이터 넣기


//        Memo memo = new Memo();
//        memo.setTitle("제목");
//        memo.setContent("내용");
//        helper.create(memo);

        // 생성자를 만들면 위의 코드가 필요가 없게되는데 다음과같이 변한다
        helper.create(new Memo("제목1","내용1"));
        helper.create(new Memo("제목2","내용2"));
        helper.create(new Memo("제목3","내용3"));
        helper.create(new Memo("제목4","내용4"));



        // 검색하기2
        List<Memo> datas = helper.search("내용3");
        for(Memo one : datas) {
            Log.i("Memo", one.getId() + " : title=" + one.getTitle() + ", content=" + one.getContent());
        }

    }
}
