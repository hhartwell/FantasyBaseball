package harvey.com.fantasybaseball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DraftTeam extends AppCompatActivity {
// activity used to draft a team
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_team);
    }
    public void insertIntoTeamsTable(){
        //insert phone number, team name... into table

    }
    public void draftPlayerFromTable(){
        //update row in table with team ID of player to indicate its drafted
        //update listView
    }


}
