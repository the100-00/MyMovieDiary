package ddwucom.mobile.finalproject.ma01_20180948;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {
	
	private final static String DB_NAME = "movie_db";
	public final static String TABLE_NAME = "movie_table";
	public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
	public final static String COL_ORIGINAL_TITLE = "original_title";
	public final static String COL_PLACE = "place";
	public final static String COL_DATE = "date";
    public final static String COL_REVIEW = "review";
    public final static String COL_POSTER = "poster";
	public final static String COL_RANK = "rank";

	public MovieDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
				+ COL_TITLE + " TEXT, " + COL_ORIGINAL_TITLE + " TEXT, " + COL_PLACE + " TEXT, " + COL_DATE + " TEXT, " + COL_REVIEW + " TEXT, " + COL_POSTER + " TEXT, " + COL_RANK + " TEXT);");

//		샘플 데이터
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (1, '오션스 8','Oceans Eight','신촌 메가박스','2020-02-20', '재미있었다', '/mbE7RoCiFLljulqOhYSlkrnt5b0.jpg','5');");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
	}

}
