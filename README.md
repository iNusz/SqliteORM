# SQLiteORM



## Query




<br/>

#### DDL(Data Definition Language)



<br/>



- 테이블 생성 쿼리
create table 테이블이름 (컬럼이름1 컴럼속성, 컬럼이름2 속성...);



```
int 숫자
char(250)        // 문자를 10자를 넣으면 그래도 공간이 250만큼 채워진다 싹다 사용 , 거의안씀
varchar(250) // 내가 사용할수 있는 최대 공간 250 , 10자를 넣으면 10자만 사용한다  , 250byte , 많이쓰인다
text // 대용량의 데이터를 넣을 수 있다
```

<br/>



#### DML (Data Definition Manipulation)


- 데이터 입력 쿼리




```
insert into 테이블이름 (컬럼이름1, 컬럼이름2....) values(숫자값,'문자값'...);
insert into memo(memoid, title, content) values(1, '제목', '내용입니다');
```




- 데이터 조회 쿼리




```
select 컬럼이름1, 컬럼이름2... from 테이블이름 where 조건절;
select memoid, title from memo; // 조건절이 없으면 전체 데이터를 가져온다
select * from memo; // 컬럼이름 대신에 *를 사용하면 전체 컬럼을 가져온다
```




- 일반 조건절



```
select memoid,title,content from memo where memoid=1;  // 제일많이쓴다   , 마지막에 자기가원하는거 가져오는것
```



- 문자열 중간 검색



```
'%검색문자열%3' --> 검색문자열만 검색한다     / 가장 많이쓰이는 쿼리이다   
'검색문자열%' -->첫글자가 검색문자열있는것만검색 , '%검색문자열' --> 마지막글자가 검색문자열인걸 찾는다
select * from memo where content like '%내용3%';
```


- 데이터 수정 쿼리


```
update 테이블이름 set 컬럼이름 = 숫자값, 컬럼이름 = '문자값' where 조건절;     // 조건절안쓰면 큰일남 !!
update memo set content='수정되었습니다' , title='제목이 수정되었습니다' where memoid=3;
```



- 데이터 삭제 쿼리



```
delete from 테이블이름 where 조건절     // 조건절안쓰면 다날라감
delete from memo where memoid=2;
```



- 테이블 자동 증가 값 설정



쿼리를 보내주면 memoid의 값을 증가시켜서 넣어주게된다
```
create table memo(memoid integer primary key, title varchar(250), content text);

insert into memo(title, content) values('제목','내용입니다');
```



<br/>




## OrmLite

#### Gradle에 라이브러리 추가



- https://mvnrepository.com/artifact/com.j256.ormlite/ormlite-android 에 접속해서 Compile Dependencies를 참고해서 Gradle에 추가 시킨다.



- Gradle에는 '그룹.아키텍트.버전'순서로 추가 시켜서 쓰면 된다
ex) 17년 6월 10일 기준 현재 버전은 5.0



- DBHelper에서 OrmLiteSqliteOpenHelper을 상속받는다

```Gradle
compile 'com.j256.ormlite:ormlite-core:5.0'
compile 'com.j256.ormlite:ormlite-android:5.0'
```


## DBHelper



#### DBHelper 생성자



생성자 내의 인자를 상수로 정의 함으로써 ???????????????????????????
```java

   public static final String DATABASE_NAME = "database.db";
   public static final int DATABASE_VERSION = 1;

   // 최초 호출될때 database.db 파일을 /data/data/패키지명/database/디렉토리 ( database.db ) 아래에 파일을 생성하준다
   public DBHelper(Context context) {
       // super은 바꿀수 없다
       super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }
```


#### onCreate


최초에 생성이 되면 버전체크를 해서 onCreate 를 호출한다

TableUtils.createTable(connectionSource, ***테이블이 있는 클래스*** ); 를 이용함으로써 우리는 테이블을 생성할수 있다


```java
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
```



#### onUpgrade


데이터 베이스 버전이 업그레이 되면 호출된다.



**만약에 컬럼만 변경되었을 경우에는 임시테이블을 생성하고 데이터를 백업을 한 뒤 원래 있던 테이블을 삭제하고 백업된 데이터를 다시 입력한다**



```java
@Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // Memo 테이블의 특정 컬럼만 변경되었을 경우
            // 1. 기존 테이블을 백업하기 위한 임시테이블을 생성하고 데이터를 담아둔다
            //  예) create table temp_memo,   <- 기존데이터
            // 2. Memo 테이블을 삭제하고 다시 생성한다
            // 3. 백업된 데이터를 다시 입력한다
            // 4. 임시 테이블 삭제

            // 업그레이드 테이블
            TableUtils.createTable(connectionSource, Bbs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
```



#### create


데이터를 입력 하는 메소드


```java
public void create(Memo memo){
        try {
            // 1. 테이블에 연결
            // 메모클래스의 테이블에 연결하는 메소드 , Data access object 의 약자 ,
            // 여기서 <> 제네릭 안에 넣어줘야 하는것은 이게 어느곳의 테이블인지 모르게 떄문에 넣어주는것이고 , Integer은 primaryKey 타입이 int라 넣어준것
           Dao<Memo,Integer> dao = getDao(Memo.class);
            // 2. 데이터를 입력
            dao.create(memo);
            // 이 과정이 insert query를 한 과정이다
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
```


#### readAll


데이터 전부를 읽어 오는 메소드


```java
// 여기서 리턴 타입은 리스트를 반환해야함으로 List<Memo>이다
    public List<Memo> readAll(){
        //return 을 받기위해 전역변수로 빼주었다
        List<Memo> datas = null;
        try {
            // 1. 테이블에 연결
            Dao<Memo,Integer> dao = getDao(Memo.class);
            // 2.1 데이터를 전체 읽어오기 , List형태로 읽어온다 따라서 리턴타입도 List<Memo>
            datas =  dao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }
```


#### read


데이터 한개를 읽어 오는 메소드



```java
// id값을 인자로 넣어둬야하고 리턴타입은 Memo객체 하나를 넘겨줘야한다 위에는 전부라서 List<>다받아온것이다
    public Memo read(int memoid){
        Memo memo = null;
        try {
            // 1. 테이블에 연결
            Dao<Memo,Integer> dao = getDao(Memo.class);
            // 2.1 데이터를 한개 읽어오기
            memo =  dao.queryForId(memoid);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memo;
    }
```



#### search



데이터 검색하기



```java
public List<Memo> search(String word) {
        //return 을 받기위해 전역변수로 빼주었다
        List<Memo> datas = null;
        try {
            // 1. 테이블에 연결
            Dao<Memo, Integer> dao = getDao(Memo.class);
            // 2 데이터 검색하기 , List형태로 읽어온다 따라서 리턴타입도 List<Memo>
            // TODO  특수문자를 줘서 SQLite 가 +word+를 번역하게 하는 방법도 있다
            // 아래식 때문에 쿼리를 알면 편하게 식을 사용할 수 있다 , Orm tool에 Raw쿼리를 날리는것 , 쿼리를 마음대로 만들어서 검색조건을 만들어서 쓸 수 있다
            String query = "select * from memo where content like '%"+word+"%'";
            // queryRaw 를 자세히 보면 GenericRawResults 형태로 되어있는걸 알 수 있다
            GenericRawResults<Memo> temps = dao.queryRaw(query, dao.getRawRowMapper());
            // 제너릭을 달아줌으로써 Memo 컬랙션으로 리턴이 된다
            datas = temps.getResults();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }
```
