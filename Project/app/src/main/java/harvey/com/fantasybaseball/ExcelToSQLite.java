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

public class ExcelToSQLite extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "fantasy_baseball_DB";
    static final int DATABASE_VERSION = 1;

    // teams table and all its attributes
    static final String TABLE_TEAMS = "teams";
    static final String Id = "_id";
    static final String TEAM_NAME = "team_name";
    static final String USER_ID = "_id";

    // player table and attr names
    static final String PHONE_NUMBER = "user_id";
    static final String TABLE_PLAYERS = "players";
    static final String PID = "pid";

    static final String TAG = "EXCEL TO SQLITE";
    private Context context;

    public ExcelToSQLite(Context context) {
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
        try {
            populateTeamsTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String dropTable(String table) {
        return "DROP TABLE IF EXISTS " + table + ";";
    }

    public String createTeamsTable() {
        String createTable = "";
        // team name
        // user id
        createTable += "CREATE TABLE IF NOT EXISTS teams ( " +
                "team_name TEXT, " +
                "_id INTEGER, " +
                "PRIMARY KEY (_id), " +
                "FOREIGN KEY (_id) REFERENCES players (user_id) " +
                "ON DELETE CASCADE ON UPDATE NO ACTION" +
                ");";
        return createTable;
    }

    private String createPlayerTable() {
        String createTable = "";
        createTable += "CREATE TABLE IF NOT EXISTS players IF ( " +
                "user_id INTEGER, " +
                "player_name TEXT, " +
                "_id INTEGER, " +
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
                "PRIMARY KEY (_id));";
        Log.d(TAG, "createPlayer table: " + createTable);
        return createTable;
    }

    /**
     * this function is meant to be used as a parameter to a preparedStatment where there
     * are 3 argments
     * 1 - team_name
     * 2 - user_id
     * 3 - user_name
     *
     * @return string that takes 3 parameters to be used in a prepared statment
     */
    public String insertIntoTeamsTable() {
        String querey = "";
        querey += "INSERT INTO " + TABLE_TEAMS + " (team_name, _id) VALUES(?, ?);";
        return querey;
    }

    public void populateTeamsTable() throws IOException {
        InputStream file = context.getResources().openRawResource(R.raw.player_db);
        BufferedReader br = new BufferedReader(new InputStreamReader(file, "UTF-8"));

        String line = "";
        String columns = "player_name, _id, ab, r, h, hr, rbi, ba, obp, pitcher, w, era, bb, whip";
        String header = "INSERT INTO " + TABLE_PLAYERS + " (" + columns + ") VALUES (";
        String closing = ");";

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        while ((line = br.readLine()) != null) {
            StringBuilder sb = new StringBuilder(header);
            String[] str = line.split(",");
            sb.append("'" + str[0] + "',");
            for (int i = 1; i < 13; i++) {
                sb.append("'" + str[i] + "',");
            }
            sb.append("'" + str[str.length - 1] + "'");
            sb.append(closing);
            db.execSQL(sb.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public List<PlayerObject> getSelectAllPlayersList() {
        // debugging method;
        List<PlayerObject> players = new ArrayList<PlayerObject>();
        Cursor cursor = getSelectAllPlayerObjectsCursor();
        while (cursor.moveToNext()) {
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

    public List<String> getSelectAllTeamsList() {
        List<String> teams = new ArrayList<>();
        Cursor cursor = getSelectAllTeamsCursor();
        while (cursor.moveToNext()) {
            String team = "{" + cursor.getString(0) + ", ";
            team += cursor.getLong(1) + "}";
            teams.add(team);
        }
        return teams;
    }

    public Cursor getSelectAllTeamsCursor() {
        String query =
                "SELECT * " + "" +
                "FROM " + TABLE_TEAMS + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    /**
     * selcects attr. from a sepecified table. meant to be used in prepared stmt
     * where prepared val is the attr.
     * if ordered is true, then there is a prepared field for attr to be ordered by.
     *
     * @param tableName
     * @param ordered
     * @return
     */
    public String getSelectPreparedQuery(String tableName, boolean ordered) {
        String query = "";
        query += "SELECT ? " +
                "FROM " + tableName;
        if (ordered) {
            query += " ORDER BY ?";
        }
        query += ";";
        Log.d(TAG, "getSelectOnePreparedQuery: " + query);
        return query;
    }
    public Cursor execSelectStmt(SQLiteStatement stmt, String... args) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(stmt.toString(), args);
        return cursor;
    }

    /**
     * returns string for query.
     * example : getOrderByQuery("ba");
     * @param orderBy
     * @return
     */
    public String getOrderByQuery(String orderBy){
        String query = "";
        query += "SELECT * FROM " + TABLE_PLAYERS;
        query += " ORDER BY "+ orderBy +" DESC ;";
        return query;
    }
    public String getUserTeamQuery(){
        String query = "";
        query +=
                "SELECT * " +
                "FROM " + this.TABLE_TEAMS + ", " + this.TABLE_PLAYERS +
                " WHERE " + TABLE_TEAMS + "." + USER_ID + " = " + TABLE_PLAYERS + "." + USER_ID + ";";
        return query;
    }
    // functions to be used in draftTeams activity

    /**
     * returns a query string for drafting batters
     * @return
     */
    public String getQueryBatters(){
        String query = "";
        String selectFields = "player_name, _id, ba, h, hr";
        query = "SELECT " + selectFields + " From " + TABLE_PLAYERS + " WHERE user_id IS NULL AND pitcher = 0;";
        return query;
    }

    /**
     * returns a query string for drafting picthers
     * @return
     */
    public String getQueryPitchers(){
        String query = "";
        String selectFields = "player_name, _id, w, era, whip";
        query = "SELECT " + selectFields + " FROM " + TABLE_PLAYERS + " WHERE user_id IS NULL AND pitcher = 1;";
        return query;
    }

    /**
     * called when a player is drafted and assigned a team
     * @param pid player id
     * @param num phone number
     * @return
     */
    public String getPlayerDraftedQuery(int pid, Long num){
        String query = "";
        query += "UPDATE " + TABLE_PLAYERS + " SET "+ PHONE_NUMBER + " = " + num +" WHERE _id = " + pid + ";";
        Log.d(TAG, "update: " + query);
        return query;
    }

    /**
     * used to retreive team names and phone numbers from team table
     */
    public String getTeamNameQuery(){
        String query ="";
        query += "SELECT " + TEAM_NAME + ", " + USER_ID +
                " FROM " + TABLE_TEAMS + ";";
        return query;
    }
    public String getTeamByNumberQueryAndOrderBy(Long phone_number, String orderBy){
        String query = "";
        query += "SELECT * FROM " + TABLE_PLAYERS;
        query += " WHERE user_id = "+ phone_number;
        query += " ORDER BY "+ orderBy +" DESC ;";
        return query;
    }
}