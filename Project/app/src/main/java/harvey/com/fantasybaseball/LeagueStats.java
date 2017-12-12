package harvey.com.fantasybaseball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LeagueStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_stats);
    }


    public void setBA(){
        TextView bestBA = (TextView) findViewById(R.id.bestBA);
        //Get team name with best batting average
        //and set it to team name

        String teamName="Bob";
        String text= "The team with the best Batting Average: " + teamName +"having ";

        //bestBA.setText(text);

    }
    public void setHITS(){
        TextView bestHITS = (TextView) findViewById(R.id.bestHITS);
        //Get team name with most hits
        //and set it to team name

        String teamName="Bob";
        String text= "The team with the most Hits: " + teamName+ "with ";
        //bestHITS.setText(text);

    }
    public void setHR(){
        TextView bestHR = (TextView) findViewById(R.id.bestHR);
        //Get team name with most HR
        //and set it to team name

        String homeruns = "32";
        String teamName="Bob";
        String text= "The team with the most HRs: " + teamName +"with "+ homeruns;
        //bestHR.setText(text);

    }

    public void setWINS(){
        TextView bestWINS = (TextView) findViewById(R.id.bestWINS);
        //team name with the most wins
        //and set it to team name
        String wins = "12";
        String teamName="Bob";
        String text= "The team with the most WINS: " + teamName+ "with ";
        //bestWINS.setText(text);
    }
    public void setWHIP(){
        TextView bestWINS = (TextView) findViewById(R.id.bestWHIP);
        //team name with the best WHIP
        //and set it to team name
        String whip = "1.232";
        String teamName="Bob";
        String text= "The team with best WHIP: " + teamName +"having ";
        //bestWINS.setText(text);
    }
    public void setERA(){
        TextView bestERA = (TextView) findViewById(R.id.bestERA);
        //team name with the most wins
        //and set it to team name
        String era ="2.232";
        String teamName="Bob";
        String text= "The team with the best ERA: " + teamName +"having ";
        //bestERA.setText(text);
    }

}
