package app.univers7.ultra_instinct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alex on 03/04/2018.
 */

public class SQLiteDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.PLAYER1_NAME,
            MySQLiteHelper.PLAYER2_NAME,
            MySQLiteHelper.VICTORIES,
            MySQLiteHelper.LOSES,
            MySQLiteHelper.KOS,
            MySQLiteHelper.COLUMN_LAT,
            MySQLiteHelper.COLUMN_LNG};

    public SQLiteDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SQLiteMatch createMatch (String player1_name, String player2_name, int victories, int loses, int kos, Double lat, Double lng){
        ContentValues values = new ContentValues();
        values.put (MySQLiteHelper.PLAYER1_NAME, player1_name);
        values.put(MySQLiteHelper.PLAYER2_NAME, player2_name);
        values.put(MySQLiteHelper.VICTORIES, victories);
        values.put(MySQLiteHelper.LOSES, loses);
        values.put(MySQLiteHelper.KOS, kos);
        values.put(MySQLiteHelper.COLUMN_LAT, lat);
        values.put(MySQLiteHelper.COLUMN_LNG, lng);

        long insertId = database.insert(MySQLiteHelper.TABLE_RECENT_MATCHES, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_RECENT_MATCHES, allColumns, MySQLiteHelper.COLUMN_ID
                + " = " + insertId, null, null ,null ,null);
        cursor.moveToFirst();
        SQLiteMatch newMatchInfos = cursorMatch(cursor);
        cursor.close();

        return newMatchInfos;
    }

    public void deleteAllMatchs()
    {
        database.delete(MySQLiteHelper.TABLE_RECENT_MATCHES, null, null);
    }

    public SQLiteMatch cursorMatch(Cursor cursor){
        SQLiteMatch MatchInfos = new SQLiteMatch();

        MatchInfos.setId(cursor.getLong(0));
        MatchInfos.setPlayer1_name(cursor.getString(1));
        MatchInfos.setPlayer2_name(cursor.getString(2));
        MatchInfos.setVictories(cursor.getInt(3));
        MatchInfos.setLoses(cursor.getInt(4));
        MatchInfos.setKos(cursor.getInt(5));
        MatchInfos.setLat(cursor.getDouble(6));
        MatchInfos.setLng(cursor.getDouble(7));

        return MatchInfos;
    }


}
