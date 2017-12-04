package harvey.com.fantasybaseball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class DraftTeam extends AppCompatActivity {


    int playersDrafted =0;

// activity used to draft a team
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_team);
    }

    public void insertIntoTeamsTable(){
        //insert phone number, team name... into table

        EditText teamName = (EditText) findViewById(R.id.teamName);
        EditText phoneNumber = (EditText) findViewById(R.id.phone);

        int phone= Integer.parseInt(phoneNumber.getText().toString());
        String team = teamName.getText().toString();

        //SQL INSERT LOGIC GOES BELOW

    }


    public void draftPlayerFromTable(){
        //update row in table with team ID of player to indicate its drafted
        //update listView
        EditText phoneNumber = (EditText) findViewById(R.id.phone);


        EditText playerID = (EditText) findViewById(R.id.playerID);
        int PID = Integer.parseInt(playerID.getText().toString());
        int phone= Integer.parseInt(phoneNumber.getText().toString());
        playersDrafted++;

        //update row in DB with the PID and add the phone to the UID cell to show it is drafted.


    }



}
