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
    boolean tableMade=false;

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


            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(databaseHelper.getBestAvgAttrOfTeamsForBatters("ba"),null);
            if (cursor.getCount()>0){
                cursor.moveToFirst();
                String teamName=cursor.getString(0);
                String text= "Current team with the best Batting Average: " + teamName;
                TextView bestBA = (TextView) findViewById(R.id.bestBA);
                bestBA.setText(text);
            }



            //SQL query batters and update tableview
            db = databaseHelper.getWritableDatabase();
            cursor = db.rawQuery(databaseHelper.getBestBattingAvg(), null);
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

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(databaseHelper.getBestAvgAttrOfTeamsForBatters("ba"),null);
            cursor.moveToFirst();
            String teamName=cursor.getString(0);
            String text= "Current team with the best Batting Average: " + teamName;
            TextView bestBA = (TextView) findViewById(R.id.bestBA);
            bestBA.setText(text);


            TextView header=(TextView) findViewById(R.id.header);
            header.setText("Player                           PID           WINS         ERA         WHIP");
            TextView textView = (TextView) findViewById(R.id.prompt);
            String message = "Please select a pitcher, you have "+ (15 - playersDrafted) + " pitchers remaining";
            textView.setText(message);
            //SQL query pitchers and update tableview
            EditText pid = (EditText) findViewById(R.id.playerID);
            pid.setText("");


            cursor = db.rawQuery(databaseHelper.getBestERAofPlayers(), null);
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

    public void draftPlayerFromTable(View view){
        // called when user presses draft team
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
            dialog.setMessage("Please enter a PID to draft a player");
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

        }
        updateViews(); //updates the table to show players availible and the textview to show how many players left.


        playersDrafted++;

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
}
