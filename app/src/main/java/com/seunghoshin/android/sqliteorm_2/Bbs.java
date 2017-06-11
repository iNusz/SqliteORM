package com.seunghoshin.android.sqliteorm_2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by SeungHoShin on 2017. 6. 10..
 */


@DatabaseTable(tableName = "bbs")
public class Bbs {

    @DatabaseField
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String content;
    @DatabaseField
    private Date date;

    public Bbs(){
        setDate();
    }

    public Bbs(String title, String content){
        this.title = title;
        this.content = content;
        setDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setDate() {
        Date date = new Date(System.currentTimeMillis());
        this.date = date;
    }
}


