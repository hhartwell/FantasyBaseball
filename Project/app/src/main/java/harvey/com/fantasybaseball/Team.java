package harvey.com.fantasybaseball;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class Team extends AppCompatActivity {
// this class will display a user's team
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        ListView listView = (ListView) (findViewById(R.id.roster));
        //query SQL to populate listView, default sort by name
    }



    public void sortByBA(View view){
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their batting average
    }
    public void sortByHits(View view){
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their hits
    }
    public void sortByHR(View view){
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their home runs
    }public void sortByW(View view){
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their wins
    }
    public void sortByERA(View view){
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their ERA
    }
    public void sortByWHIP(View view){
        ListView listView = (ListView) (findViewById(R.id.roster));
        //Select players ordered by their WHIP
    }


}


