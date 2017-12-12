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

    // batter table and attr names
    static final String TABLE_BATTERS = "batters";
    static final String _ID = "_id";
    static final String BA = "ba";
    static final String H = "h";
    static final String HR = "hr";

    //pitcher table and attr names
    static final String TABLE_PITCHERS = "pitchers";
    static final String ID = "_id";
    static final String W = "w";
    static final String ERA = "era";
    static final String WHIP = "whip";


    static final String TAG = "EXCEL TO SQLITE";
    private Context context;
    boolean alreadyPopulated = false;

    public ExcelToSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlPlayerTable = createNewPlayerTable();
        String sqlBatterTable = createBatterStatsTable();
        String sqlPitcherTable = createPitcherStatsTable();
        String sqlTeamTable = createTeamsTable();

        db.execSQL(sqlPlayerTable);
        db.execSQL(sqlBatterTable);
        db.execSQL(sqlPitcherTable);
        db.execSQL(sqlTeamTable);

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

    /**
     * old table that only created one players table. all instances of this function should be gone by turn in
     *
     * ** TO BE DELETED LATER **
     *
     * @return
     */
    private String createPlayerTable() {
        String createTable = "";
        createTable += "CREATE TABLE IF NOT EXISTS players ( " +
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

    private String createNewPlayerTable(){
        String query = "CREATE TABLE IF NOT EXISTS players (" +
                "user_id Integer, " + //when drafted the userID will have the team owners phone #
                "player_name TEXT," +
                "_id INTEGER," + //playerID
                "PRIMARY KEY (_id));";
        return query;
    }
    private String createBatterStatsTable() {
        String query = "CREATE TABLE IF NOT EXISTS batters ( ";
        query += "" +
                "_id INTEGER, " +
                "ba REAL, " +
                "h REAL, " +
                "hr REAL, " +
                "PRIMARY KEY (_id)," +
                "FOREIGN KEY (_id) REFERENCES players (_id)" +
                ");";
        return query;
    }

    private String createPitcherStatsTable(){
        String query = "CREATE TABLE IF NOT EXISTS pitchers ( ";
        query += "" +
                "_id INTEGER, " +
                "w REAL, " +
                "era REAL, " +
                "whip REAL, " +
                "PRIMARY KEY (_id)," +
                "FOREIGN KEY (_id) REFERENCES players (_id)" +
                ");";
        return query;
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
        if(alreadyPopulated){
            Log.d(TAG, "already POPULATED");
            return;
        }
        Log.d(TAG, "INSIDE POPULATE TEAMS TABLE");
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
        alreadyPopulated = true;
    }

    /**
     * new populate teams table that populates a players batters and pitchers table
     * @throws IOException
     */
    public void populateNewTeamsTable()throws IOException{
        // index 9 is pitcher
        Log.d(TAG, "INSIDE POPULATE TEAMS TABLE");
        InputStream file = context.getResources().openRawResource(R.raw.player_db);
        BufferedReader br = new BufferedReader(new InputStreamReader(file, "UTF-8"));

        String line = "";
        String playerColumns = "player_name, _id";
        String playerHeader = "INSERT INTO " + TABLE_PLAYERS + " (" + playerColumns + ") VALUES (";
        String closing = ");";

        String batterColumns = "_id, ba, h, hr";
        String batterHeader = "INSERT INTO " + TABLE_BATTERS + " (" + batterColumns + ") VALUES (";

        String pitcherColumns = "_id, w, era, whip";
        String pitcherHeader = "INSERT INTO " + TABLE_PITCHERS + " (" + pitcherColumns + ") VALUES (";

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        while ((line = br.readLine()) != null) {
            String[] str = line.split(",");
            StringBuilder players = new StringBuilder(playerHeader);
            StringBuilder batters = new StringBuilder(batterHeader);
            StringBuilder pitchers = new StringBuilder(pitcherHeader);

            // players table only gets name
            players.append("'" + str[0] + "',");

            // id for all
            players.append("'" + str[1] + "'");
            batters.append("'" + str[1] + "',");
            pitchers.append("'" + str[1] + "',");

            // for batters
            batters.append("'" + str[7] + "',");
            batters.append("'" + str[4] + "',");
            batters.append("'" + str[5] + "'");

            // for pitchers
            pitchers.append("'" + str[10] + "',");
            pitchers.append("'" + str[11] + "',");
            pitchers.append("'" + str[12] + "'");

            players.append(closing);
            batters.append(closing);
            pitchers.append(closing);
            db.execSQL(players.toString());
            db.execSQL(batters.toString());
            db.execSQL(pitchers.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        alreadyPopulated = true;
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


    /**
     * depricated
     * @param phone_number
     * @param orderBy
     * @return
     */
    public String getTeamByNumberQueryAndOrderBy(Long phone_number, String orderBy){
        String query = "";
        query += "SELECT * FROM " + TABLE_PLAYERS;
        query += " WHERE user_id = "+ phone_number;
        query += " ORDER BY "+ orderBy +" DESC ;";
        return query;
    }
    public String getNewTeamByNumber(Long phone_number){
        String query = "SELECT p.player_name, p._id, b.ba, pit.era " +
                "FROM " + TABLE_PLAYERS + " p, " + TABLE_BATTERS + " b, " + TABLE_PITCHERS + " pit " +
                "WHERE p._id = b._id AND p._id = pit._id AND p.user_id = "+ phone_number +";";
        return query;
    }



    public String getTeamNameFromPhoneNumber(Long phone_number){
        String query = "";
        query += "SELECT " + TEAM_NAME;
        query += " FROM "+ TABLE_TEAMS;
        query += "WHERE _id = " + phone_number + ";";
        return query;
    }

    /**
     * find best batting avg from all players where they have had more than 50 hits
     * @return
     */
    public String getBestBattingAvg(){
        String query = "SELECT p.player_name, p._id, b.ba, b.h, b.hr " +
                "FROM "+TABLE_PLAYERS + " p, " + TABLE_BATTERS + " b " +
                "WHERE p._id = b._id AND b._id NOT IN (" +
                    "SELECT b._id " +
                    "FROM " + TABLE_BATTERS + " b " +
                    "WHERE b.h < 50) " +
                "AND p.user_id IS NULL;";
        return query;
    }

    /**
     * finds the team with the best attribute among average
     * @return
     */
    public String getBestAvgAttrOfTeamsForBatters(String attr){
        String query =
                "SELECT t.team_name " +
                "FROM " + TABLE_TEAMS + " t, " + TABLE_BATTERS + " b, " + TABLE_PITCHERS + " pit " +
                "WHERE p.user_id = t._id AND p._id = b._id ANDp._id = pit._id " +
                "GROUP BY t.team_name " +
                "HAVING AVG(b."+attr+") >= ALL IN (" +
                        "SELECT AVG(b."+attr+") " +
                        "FROM players p, batters b, teams t " +
                        "WHERE t._id = p.user_id AND p._id = b._id " +
                        "GROUP BY t.team_name);";

        return query;
    }

    /**
     * finds team with best attribute among pithcers
     * @param attr
     * @return
     */
    public String getBestAvgAttrOfTeamsForPitchers(String attr){
        String query =
                "SELECT t.team_name " +
                        "FROM " + TABLE_TEAMS + " t, " + TABLE_BATTERS + " b, " + TABLE_PITCHERS + " pit " +
                        "WHERE p.user_id = t._id AND b._id = p._id AND p._id = pit._id " +
                        "GROUP BY t.team_name " +
                        "HAVING AVG(pit."+attr+") >= ALL IN (" +
                        "SELECT AVG(pit."+attr+") " +
                        "FROM players p, batters b, teams t " +
                        "WHERE t._id = p.user_id AND p._id = b._id " +
                        "GROUP BY t.team_name);";

        return query;
    }
    /**
     * find best pitcher era from all pitchers with more than 10 wins
     * @return
     */
    public String getBestERAofPlayers(){
        String query = "SELECT p.player_name, p._id, pitcher.w, pitcher.era, pitcher.whip " +
                "FROM "+TABLE_PLAYERS + " p, " + TABLE_PITCHERS + " pitcher " +
                //"INNER JOIN " + TABLE_PLAYERS + " ON p._id = pitcher._id " +
                "WHERE p._id = pitcher._id AND p._id NOT IN (" +
                "SELECT pit._id " +
                "FROM " + TABLE_PITCHERS + " pit " +
                "WHERE pit.w < 5) " +
                "AND p.user_id IS NULL;";
        return query;
    }
}