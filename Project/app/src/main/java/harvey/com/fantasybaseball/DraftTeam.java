package harvey.com.fantasybaseball;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DraftTeam extends AppCompatActivity {


    int playersDrafted =0;
    boolean teamCreated;

    // activity used to draft a team
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_team);
        teamCreated =false;
        updateViews();
    }


    public void updateViews(){
        if (playersDrafted <=10){
            TextView textView = (TextView) findViewById(R.id.prompt);
            String message = "Please select a batter, you have "+ (10-playersDrafted) + " batters remaining";
            //query batters and update tableview


        }
        if (playersDrafted >=11 && playersDrafted <15){
            TextView textView = (TextView) findViewById(R.id.prompt);
            String message = "Please select a pitcher, you have "+ (5-playersDrafted) + " batters remaining";
            //query pitchers and update tableview
        }

    }

    public void draftPlayerFromTable(){ // called when user presses draft team
        //update row in table with team ID of player to indicate its drafted
        //update listView
        EditText teamName = (EditText) findViewById(R.id.teamName);
        EditText phoneNumber = (EditText) findViewById(R.id.phone);
        EditText PID = (EditText) findViewById(R.id.playerID);


        if (phoneNumber.getText().toString().toString()==null){
            //DIALOG "ENTER A PHONE NUMBER"

            AlertDialog.Builder dialog=new AlertDialog.Builder(DraftTeam.this);
            dialog.setTitle("Enter Number");
            dialog.setMessage("Please enter a phone number to draft a player");
            dialog.setPositiveButton("OK", null);
            dialog.show();

            return;
        }
        if (teamName.getText().toString()==null){
            //Dialog "Enter a team name"
            AlertDialog.Builder dialog=new AlertDialog.Builder(DraftTeam.this);
            dialog.setTitle("Enter Team Name");
            dialog.setMessage("Please enter a Team Name to draft a player");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            return;
        }
        if(PID.getText().toString()==""){
            //Dialog "You must enter the playerID that you want to draft

            AlertDialog.Builder dialog=new AlertDialog.Builder(DraftTeam.this);
            dialog.setTitle("Enter Team Name");
            dialog.setMessage("Please enter a Team Name to draft a player");
            dialog.setPositiveButton("OK", null);
            dialog.show();
            return;


        }

        int phone= Integer.parseInt(phoneNumber.getText().toString());
        String team = teamName.getText().toString();
        int playerID=Integer.parseInt(PID.getText().toString());

        if (teamCreated==false){

            teamCreated=true;

            //SQL INSERT LOGIC GOES BELOW to insert team in to teams table
        }
        updateViews(); //updates the table to show players availible and the textview to show how many players left.


        playersDrafted++;
        //update row in DB with the PID and add the phone to the UID cell to show it is drafted.


        if (playersDrafted==15){
            //return to main menu


        }


    }

}
