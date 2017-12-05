package harvey.com.fantasybaseball;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DraftTeam extends AppCompatActivity {
    static final String TAG = "DRAFT TEAMS";

    int playersDrafted;
    boolean teamCreated;
    ExcelToSQLite databaseHelper;
    boolean

    // activity used to draft a team
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playersDrafted = 0;
        databaseHelper = new ExcelToSQLite(this);
        setContentView(R.layout.activity_draft_team);
        teamCreated =false;
        updateViews();
    }


    public void updateViews(){
        if (playersDrafted <10){
            Log.d(TAG, "inside first if clause");
            TextView textView = (TextView) findViewById(R.id.prompt);
            String message = "Please select a batter, you have "+ (10-playersDrafted) + " batters remaining";
            textView.setText(message);
            EditText pid = (EditText) findViewById(R.id.playerID);
            pid.setText("");

            //SQL query batters and update tableview
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(databaseHelper.getQueryBatters(), null);
            Log.d(TAG, databaseHelper.getSelectAllPlayersList().toString());
            // need a cursor adapter
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.simple_list_item_5,
                    cursor,
                    new String[]{"player_name", "_id", "ba", "h", "hr"},
                    new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                    0);
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(cursorAdapter);
        }
        if (playersDrafted >=10 && playersDrafted <15){
            TextView header=(TextView) findViewById(R.id.header);
            header.setText("Player                           PID           WINS         ERA         WHIP");
            TextView textView = (TextView) findViewById(R.id.prompt);
            String message = "Please select a pitcher, you have "+ (15 - playersDrafted) + " pitchers remaining";
            textView.setText(message);
            //SQL query pitchers and update tableview
            EditText pid = (EditText) findViewById(R.id.playerID);
            pid.setText("");

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(databaseHelper.getQueryPitchers(), null);
            // need a cursor adapter
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.simple_list_item_5,
                    cursor,
                    new String[]{"player_name", "_id", "w", "era", "whip"},
                    new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second, R.id.player_third},
                    0);
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(cursorAdapter);
        }

    }

    public void draftPlayerFromTable(View view){ // called when user presses draft team

        //update row in table with team ID of player to indicate its drafted
        //update listView
        EditText teamName = (EditText) findViewById(R.id.teamName);
        EditText phoneNumber = (EditText) findViewById(R.id.phone);
        EditText PID = (EditText) findViewById(R.id.playerID);


        if (teamName.getText().toString().length()==0){
            //Dialog "Enter a team name"
            AlertDialog.Builder dialog=new AlertDialog.Builder(DraftTeam.this);
            dialog.setTitle("Enter Team Name");
            dialog.setMessage("Please enter a Team Name to draft a player");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            return;
        }
        if (phoneNumber.getText().toString().length()==0){
            //DIALOG "ENTER A PHONE NUMBER"

            AlertDialog.Builder dialog=new AlertDialog.Builder(DraftTeam.this);
            dialog.setTitle("Enter Number");
            dialog.setMessage("Please enter a phone number to draft a player");
            dialog.setPositiveButton("OK", null);
            dialog.show();

            return;
        }

        if(PID.getText().toString().length()==0){
            //Dialog "You must enter the playerID that you want to draft

            AlertDialog.Builder dialog=new AlertDialog.Builder(DraftTeam.this);
            dialog.setTitle("Enter Team Name");
            dialog.setMessage("Please enter a Team Name to draft a player");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            return;
        }


        String team = teamName.getText().toString();
        int playerID=Integer.parseInt(PID.getText().toString());
        long phone= Long.parseLong(phoneNumber.getText().toString());

        if (teamCreated==false){
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement(databaseHelper.insertIntoTeamsTable());
            stmt.bindString(1, team);
            stmt.bindLong(2, phone);
            stmt.execute();

            teamCreated=true;

            //SQL INSERT LOGIC GOES BELOW to insert team in to teams table


        }
        updateViews(); //updates the table to show players availible and the textview to show how many players left.


        playersDrafted++;
        /*
        AlertDialog.Builder dialog=new AlertDialog.Builder(DraftTeam.this);
        dialog.setTitle("Player Drafted");
        dialog.setMessage("Player has been drafted!");
        dialog.setPositiveButton("OK", null);
        dialog.show();
        */

        //update row in DB with the PID and add the phone to the UID cell to show it is drafted.
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(databaseHelper.getPlayerDraftedQuery(playerID, phone));
        stmt.execute();

        updateViews();
        if (playersDrafted==15){
            //return to main menu
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

    }
    public void sortByBA(View view){
        ListView listView = (ListView) (findViewById(R.id.listView));
        //Select players ordered by their batting average
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getOrderByQuery("ba"), null);
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
        ListView listView = (ListView) (findViewById(R.id.listView));
        //Select players ordered by their hits
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getOrderByQuery("h"), null);
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
        ListView listView = (ListView) (findViewById(R.id.listView));
        //Select players ordered by their home runs
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getOrderByQuery("hr"), null);
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
        ListView listView = (ListView) (findViewById(R.id.listView));
        //Select players ordered by their wins
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getOrderByQuery("w"), null);
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
        ListView listView = (ListView) (findViewById(R.id.listView));
        //Select players ordered by their ERA
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getOrderByQuery("era"), null);
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
        ListView listView = (ListView) (findViewById(R.id.listView));
        //Select players ordered by their WHIP
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getOrderByQuery("whip"), null);
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
}
