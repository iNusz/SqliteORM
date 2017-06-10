package com.seunghoshin.android.sqliteorm_2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by SeungHoShin on 2017. 6. 10..
 */


@DatabaseTable(tableName = "memo")            //이것은 데이터테이블로 쓸꺼고 이름이 ~~ 이란걸 알려준다
public class Memo {
    //속성을 정의해야한다
    //@을 사용해야하는데 , 테이블에서 각각에서 사용을 해야한다고 알려줘야 한다
    @DatabaseField(generatedId = true)     // (generatedId = true) 이건 자동증가 필드로 만들어주는 것이다
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String content;
    @DatabaseField
    private Date date;

    // 나만 알아보는 컬럼을 만들경우 @을 안쓰면 된다


    public Memo(){
        // OrmLite는 기본 생성자가 없으면 동작하지 않는다
        setDate();

    }

    // @ Override
    public Memo(String title, String content){
        this.title = title;
        this.content = content;
        setDate();
    }
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    private void setDate() {
        // 여기서 인자값을 그대로 Date date를 받는게 아니라 만들어줘야한다
        // 여기서 currnettumemillis혼자 못씀으로 생성자안에 넣어줘야한다
        Date date = new Date(System.currentTimeMillis());
        this.date = date;
    }
}
