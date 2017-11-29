package harvey.com.fantasybaseball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // still need to create the listView

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListView();
        addListViewItemClickListener();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menue, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch(menuId){
            case R.id.createTeamAction:
                // needs to launch the Team activity
            case R.id.sendScoreAction:
                // sends the winning team's score to all players
                sendScores();
                Toast.makeText(this, "Winning Scores sent", Toast.LENGTH_SHORT).show();
            default:

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
    private void sendScores(){

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
}
