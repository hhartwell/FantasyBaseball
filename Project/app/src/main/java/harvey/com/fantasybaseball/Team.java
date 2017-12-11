package harvey.com.fantasybaseball;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Team extends AppCompatActivity {
// this class will display a user's team
    static final String TAG = "TEAM";
    ExcelToSQLite databaseHelper;
    Long phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        databaseHelper = new ExcelToSQLite(this);
        ListView listView = (ListView) (findViewById(R.id.roster));
        Intent recievedIntent = getIntent();
        phoneNumber = recievedIntent.getLongExtra("phone_number", 5099190888L);

        //query SQL to populate listView, default sort by name
        Log.d(TAG, String.valueOf(phoneNumber));

        //Select players ordered by their batting average
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getNewTeamByNumber(phoneNumber), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_4,
                cursor,
                new String[]{"player_name", "_id",  "ba", "era"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second},
                0);
        listView.setAdapter(cursorAdapter);
    }
/*
    public void sortByBA(View view){
        TextView header=(TextView) findViewById(R.id.stats);
        header.setText("Player                           PID           HITS         AVG         HR");
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their batting average
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getTeamByNumberQueryAndOrderBy(phoneNumber, "ba"), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_5,
                cursor,
                new String[]{"player_name", "_id",  "ba", "h", "hr"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                0);
        listView.setAdapter(cursorAdapter);
    }

    public void sortByHits(View view){
        TextView header=(TextView) findViewById(R.id.stats);
        header.setText("Player                           PID           HITS         AVG         HR");
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their hits
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getTeamByNumberQueryAndOrderBy(phoneNumber, "h"), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_5,
                cursor,
                new String[]{"player_name", "_id",  "ba", "h", "hr"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                0);
        listView.setAdapter(cursorAdapter);
    }
    public void sortByHR(View view){
        TextView header=(TextView) findViewById(R.id.stats);
        header.setText("Player                           PID           HITS         AVG         HR");
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their home runs
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getTeamByNumberQueryAndOrderBy(phoneNumber, "hr"), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_5,
                cursor,
                new String[]{"player_name", "_id",  "ba", "h", "hr"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                0);
        listView.setAdapter(cursorAdapter);
    }
    public void sortByW(View view){
        TextView header=(TextView) findViewById(R.id.stats);
        header.setText("Player                           PID           WINS         ERA         WHIP");
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their wins
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getTeamByNumberQueryAndOrderBy(phoneNumber, "w"), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_5,
                cursor,
                new String[]{"player_name", "_id",  "w", "era", "whip"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                0);
        listView.setAdapter(cursorAdapter);
    }
    public void sortByERA(View view){
        TextView header=(TextView) findViewById(R.id.stats);
        header.setText("Player                           PID           WINS         ERA         WHIP");
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their ERA
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getTeamByNumberQueryAndOrderBy(phoneNumber, "era"), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_5,
                cursor,
                new String[]{"player_name", "_id",  "w", "era", "whip"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                0);
        listView.setAdapter(cursorAdapter);
    }
    public void sortByWHIP(View view){
        TextView header=(TextView) findViewById(R.id.stats);
        header.setText("Player                           PID           WINS         ERA         WHIP");
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their WHIP
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getTeamByNumberQueryAndOrderBy(phoneNumber, "whip"), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_5,
                cursor,
                new String[]{"player_name", "_id",  "w", "era", "whip"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                0);
        listView.setAdapter(cursorAdapter);
    }
    */
}


