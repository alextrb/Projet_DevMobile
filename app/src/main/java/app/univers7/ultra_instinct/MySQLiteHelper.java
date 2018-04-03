package app.univers7.ultra_instinct;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_RECENT_MATCHES = "recent_matches";
    public static final String COLUMN_ID = "_id";
    public static final String PLAYER1_NAME ="player1_name";
    public static final String PLAYER2_NAME ="player2_name";
    public static final String VICTORIES ="victories";
    public static final String LOSES ="loses";
    public static final String KOS ="kos";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";

    private static final String DATABASE_NAME = "recent_matches.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + TABLE_RECENT_MATCHES
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + PLAYER1_NAME + " text, "
            + PLAYER2_NAME + " text, "
            + VICTORIES + " integer, "
            + LOSES + " integer, "
            + KOS + " integer, "
            + COLUMN_LAT + " integer, "
            + COLUMN_LNG + " integer);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "upgrading database from version " + oldVersion
                        + "to " + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_MATCHES);
        onCreate(sqLiteDatabase);
    }
}
