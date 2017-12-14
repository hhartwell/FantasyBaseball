package harvey.com.fantasybaseball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    CursorAdapter cursorAdapter;
    boolean playersPopulated=false;


    ListView listView;
    ExcelToSQLite databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "IN ONCREATE");
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPreferences= getSharedPreferences("playerPop", 0);
        playersPopulated= sharedPreferences.getBoolean("playersPopulated", false);
        // create database
        databaseHelper = new ExcelToSQLite(this);

        try {
            if (!playersPopulated){
                databaseHelper.populateNewTeamsTable();
                playersPopulated = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //initListView();
        populateTeamsList();
        addListViewItemClickListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences=getSharedPreferences("playerPop", 0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("playersPopulated", playersPopulated);
        editor.commit();
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
                Cursor cursor = cursorAdapter.getCursor();

                cursor.moveToPosition(position);
                if(!playersPopulated) {
                    populateTeamsList();
                    addListViewItemClickListener();
                    playersPopulated=true;
                }
                Intent i = new Intent(MainActivity.this, Team.class);
                i.putExtra("phone_number", cursor.getLong(1));
                startActivity(i);
            }
        });
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
        cursorAdapter = new SimpleCursorAdapter(
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
