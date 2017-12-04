package harvey.com.fantasybaseball;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
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
    // still need to create the listView

    ListView listView;
    ExcelToSQLite databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // create database
        this.deleteDatabase(ExcelToSQLite.DATABASE_NAME);
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
                sendScores();
            } catch (IOException e) {
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

    /**
     * sends out the winning team via text
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendScores() throws IOException {
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

        String to="5099190888";
        String body = "SCORE REPORT TEST";
        String from = "5092609560";

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
                android.R.layout.simple_expandable_list_item_2,
                cursor,
                new String[] {ExcelToSQLite.TEAM_NAME, ExcelToSQLite.USER_NAME},
                new int[] {android.R.id.text1, android.R.id.text2},
                0);
        listView.setAdapter(cursorAdapter);
    }
}
