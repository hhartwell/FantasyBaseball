package harvey.com.fantasybaseball;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harvey on 12/3/2017.
 */

public class ExcelToSQLite extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "fantasy_baseball_DB";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_TEAMS = "teams";
    static final String TABLE_PLAYERS = "players";
    static final String TAG = "EXCEL TO SQLITE";
    private Context context;

    public ExcelToSQLite(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlPlayerTable = createPlayerTable();
        String sqlTeamTable = createTeamsTable();
        db.execSQL(dropTable(TABLE_TEAMS));
        db.execSQL(dropTable(TABLE_PLAYERS));

        db.execSQL(sqlPlayerTable);
        db.execSQL(sqlTeamTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private String dropTable(String table){
        return "DROP TABLE IF EXISTS " + table + ";";
    }

    public String createTeamsTable(){
        String createTable = "";
        // team name
        // user id
        // username
        createTable += "CREATE TABLE teams ( " +
                "team_name TEXT, " +
                "user_id INTEGER, " +
                "user_name TEXT, " +
                "PRIMARY KEY (user_id), " +
                "FOREIGN KEY (user_id) REFERENCES players (user_id) " +
                "ON DELETE CASCADE ON UPDATE NO ACTION" +
                ");";
        return createTable;
    }

    private String createPlayerTable(){
        String createTable = "";
        createTable += "CREATE TABLE players ( " +
                "user_id INTEGER, " +
                "player_name TEXT, " +
                "pid INTEGER, " +
                "ab INTEGER, " +
                "r INTEGER, " +
                "h INTEGER, " +
                "hr INTEGER, " +
                "rbi INTEGER, " +
                "ba REAL, " +
                "obp REAL, " +
                "pitcher INTEGER, " +
                "w REAL, " +
                "era REAL, " +
                "bb REAL, " +
                "whip REAL, " +
                "PRIMARY KEY (pid));";
        Log.d(TAG, "createPlayer table: " + createTable);
        return createTable;
    }

    /**
     * this function is meant to be used as a parameter to a preparedStatment where there
     * are 3 argments
     * 1 - team_name
     * 2 - user_id
     * 3 - user_name
     * @return string that takes 3 parameters to be used in a prepared statment
     */
    public String insertIntoTeamsTable(){
        String querey = "";
        querey += "INSERT INTO " + TABLE_TEAMS + " (team_name, user_id, user_name) VALUES(?, ?, ?);";
        return querey;
    }

    public void populateTeamsTable() throws IOException {
        InputStream file = context.getResources().openRawResource(R.raw.player_db);
        BufferedReader br = new BufferedReader(new InputStreamReader(file, "UTF-8"));

        String line = "";
        String columns = "player_name, pid, ab, r, h, hr, rbi, ba, obp, pitcher, w, era, bb, whip";
        String header = "INSERT INTO " + TABLE_PLAYERS + " (" + columns + ") VALUES (";
        String closing = ");";

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        while ((line = br.readLine()) != null){
            StringBuilder sb = new StringBuilder(header);
            String[] str = line.split(",");
            sb.append("'" + str[0] + "',");
            for (int i = 1; i < 13; i++){
                sb.append("'" + str[i] + "',");
            }
            sb.append("'" + str[str.length-1] + "'");
            sb.append(closing);
            db.execSQL(sb.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public List<PlayerObject> getSelectAllPlayersList(){
        // debugging method;
        List<PlayerObject> players = new ArrayList<PlayerObject>();
        Cursor cursor = getSelectAllPlayerObjectsCursor();
        while(cursor.moveToNext()){
            String name = cursor.getString(1);
            int pid = cursor.getInt(2);
            players.add(new PlayerObject(name, pid));
        }
        return players;
    }

    private Cursor getSelectAllPlayerObjectsCursor() {
        String query =
                "SELECT * " +
                "From " + TABLE_PLAYERS + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public List<String> getSelectAllTeamsList(){
        List<String> teams = new ArrayList<>();
        Cursor cursor = getSelectAllTeamsCursor();
        while (cursor.moveToNext()){
            String team = "{" + cursor.getString(0) + ", ";
            team += cursor.getLong(1) + ", ";
            team += cursor.getString(2) + "}";
            teams.add(team);
        }
        return teams;
    }
    private Cursor getSelectAllTeamsCursor(){
        String query =
                "SELECT * " + "" +
                "FROM " + TABLE_TEAMS +";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
