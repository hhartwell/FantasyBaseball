package harvey.com.fantasybaseball;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MAIN ACTIVITY";
    // still need to create the listView
    int weekCount =0;
    int team1Wins = 0;
    int team2Wins = 0;
    int team3Wins = 0;
    int team4Wins = 0;
    int team5Wins = 0;
    int team6Wins = 0;


    ListView listView;
    ExcelToSQLite databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "IN ONCREATE");
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // create database
        databaseHelper = new ExcelToSQLite(this);
        
        //initListView();
        populateTeamsList();
        addListViewItemClickListener();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menue, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.createTeamAction){
            // needs to launch the Team activity
            Intent i = new Intent(this, DraftTeam.class);
            startActivity(i);
        }
        else if (menuId == R.id.sendScoreAction){
            // sends the winning team's score to all players
            try {
                simulateWeek();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Winning Scores sent", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * initializes the list view with the dummy list
     * creates and attatches an array adapter
     */
    private void initListView(){
        listView = (ListView) findViewById(R.id.teams_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                createDummyList()
        );
        listView.setAdapter(arrayAdapter);
    }

    private void addListViewItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startTeamActivity();
            }
        });
    }
    private void startTeamActivity(){
        Intent i = new Intent(this, Team.class);
        startActivity(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void simulateWeek() throws IOException, InterruptedException {

        //Will pull these numbers from the DB


        //Becuase there is no live data currently and pheasibility issues, we are going to script the season

        ArrayList<String> teamNameArr = new ArrayList<String>();
        ArrayList<String> teamPhoneArr = new ArrayList<String>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getTeamNameQuery(), null);
        while (cursor.moveToNext()){
            teamNameArr.add(cursor.getString(0));
            teamPhoneArr.add(String.valueOf(cursor.getLong(1)));
        }
        String team1Name = "Bob";
        String team2Name = "jon";
        String team3Name = "rob";
        String team4Name = "dan";
        String team5Name = "ron";
        String team6Name = "zak";

        String team1Phone="9497421750";
        String team2Phone="9497421750";
        String team3Phone="9497421750";
        String team4Phone="9497421750";
        String team5Phone="9497421750";
        String team6Phone="9497421750";

        if (weekCount==0){
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Season Simulator");
            dialog.setMessage("Week 1 has been simulated!");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            sendScores(teamPhoneArr.get(0), ("You beat "+teamNameArr.get(1)+" by 3 points!"));

            sendScores(teamPhoneArr.get(1), ("You beat "+teamNameArr.get(2)+" by 3 points!"));

            team1Wins++;
            sendScores(teamPhoneArr.get(2), ("You beat "+teamNameArr.get(3)+" by 7 points!"));
            sendScores(teamPhoneArr.get(3), ("You beat "+teamNameArr.get(4)+" by 7 points!"));
            team3Wins++;
            sendScores(teamPhoneArr.get(4), ("You beat "+teamNameArr.get(5)+" by 2 points!"));
            sendScores(teamPhoneArr.get(5), ("You beat "+teamNameArr.get(1)+" by 2 points!"));
            team5Wins++;
            weekCount++;

            return;

        }
        if (weekCount==1){
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Season Simulator");
            dialog.setMessage("Week 2 has been simulated!");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            sendScores(team1Phone, ("You beat "+team3Name+" by 4 points!"));
            sendScores(team3Phone, ("You Lost to "+team1Name+"by 4 points!"));
            team1Wins++;
            sendScores(team2Phone, ("You beat "+team5Name+" by 1 point!"));
            sendScores(team5Phone, ("You Lost to "+team2Name+" by 1 point!"));
            team2Wins++;
            sendScores(team4Phone, ("You beat "+team6Name+" by 1 point!"));
            sendScores(team6Phone, ("You Lost to "+team4Name+" by 1 point!"));
            team4Wins++;
            weekCount++;


            return;

        }
        if (weekCount==2){
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Season Simulator");
            dialog.setMessage("Week 3 has been simulated,!"+ "The winner is " + team1Name+"! The season has ended");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            sendScores(team1Phone, ("You beat "+team6Name+" by 2 points!"));
            sendScores(team6Phone, ("You Lost to "+team1Name+"by 2 points!"));
            team1Wins++;
            sendScores(team2Phone, ("You beat "+team2Name+" by 11points!"));
            sendScores(team5Phone, ("You Lost to "+team5Name+"by 11 points!"));
            team2Wins++;
            sendScores(team3Phone, ("You beat "+team3Name+" by 3 points!"));
            sendScores(team4Phone, ("You Lost to "+team4Name+"by 3 points!"));
            team4Wins++;
            weekCount++;


            sendScores(team1Phone, "The winner is " + team1Name+"! The season has ended");
            sendScores(team2Phone, "The winner is " + team1Name+"! The season has ended");
            sendScores(team3Phone, "The winner is " + team1Name+"! The season has ended");
            sendScores(team4Phone, "The winner is " + team1Name+"! The season has ended");
            sendScores(team5Phone, "The winner is " + team1Name+"! The season has ended");
            sendScores(team6Phone, "The winner is " + team1Name+"! The season has ended");
            return;
        }


    }


    /**
     * sends out the winning team via text via Message360 REST API
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendScores(String sNumber, String sBody) throws IOException {


        /**
         * Fun Fact about carriers:
         *
         * When using a long code (normal 10 digit number), the carriers do not like when you send more
         * than one SMS per second, otherwise, the sending number may be blocked.
         *
         * I have put in Thread.sleep with a generous time to buffer the time between messages being sent so as to prevent numbers
         * from being blocked.
         */

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        URL url = new URL("https://api.message360.com/api/v3/sms/sendsms.xml");
        String userPassword = "2f50b196-af9c-dcc7-1228-e08b62128704:51a9ae603a079aea1eb394661770ff92"; //Message360 Account SID and Password

        HttpsURLConnection httpCon = (HttpsURLConnection) url.openConnection();
        httpCon.setDoInput(true);
        httpCon.setDoOutput(true);
        String encoded = Base64.getEncoder().encodeToString((userPassword).getBytes(StandardCharsets.UTF_8));  //required to send HTTPS POST
        httpCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"); //The API requires a User agent
        httpCon.setRequestProperty("Authorization", "Basic " + encoded);
        httpCon.setRequestMethod("POST");
        OutputStream os = httpCon.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        String to=sNumber;
        String body = sBody;
        String from = "5092609698"; //Number owned in Message360

        String nTo="&to=" +to;
        String nFrom  = "&from=+1"+from; //number being sent from, Must be owned in Message360 account. adds required +1 before from
        String method = "&method=POST";
        String nBody ="&body="+body;
        String smartSMS="&smartsms=false";
        String parameters= nTo + nFrom + method + nBody + smartSMS ;

        writer.write(parameters); //parameters for the POST request required by Message360
        writer.flush();
        writer.close();
        os.close();
        httpCon.getResponseCode();
        httpCon.connect();
        httpCon.disconnect();
        Log.d("MESSAGE SENT", (to +" content:   "+body));


    }

    /**
     * function used for testing purposes only.
     * creates a super simple list as a place holder
     * @return
     */
    private ArrayList<String> createDummyList(){
        ArrayList<String> dummyList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            dummyList.add("team: " + i);
        }
        return dummyList;
    }
    private void populateTeamsList(){
        listView = (ListView) findViewById(R.id.teams_list_view);
        Cursor cursor = databaseHelper.getSelectAllTeamsCursor();
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_expandable_list_item_1,
                cursor,
                new String[] {ExcelToSQLite.TEAM_NAME},
                new int[] {android.R.id.text1},
                0);
        listView.setAdapter(cursorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "IN ON RESUME");
        populateTeamsList();
    }
}
