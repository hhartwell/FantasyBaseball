package harvey.com.fantasybaseball;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LeagueStats extends AppCompatActivity {
    private ExcelToSQLite databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_stats);
        databaseHelper = new ExcelToSQLite(this);
        setBA();
        setERA();

    }


    public void setBA(){
        TextView bestBA = (TextView) findViewById(R.id.bestBA);
        //Get team name with best batting average
        //and set it to team name
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getBestAvgAttrOfTeamsForBatters("ba"),null);
        cursor.moveToFirst();
        String teamName=cursor.getString(0);
        String text= "The team with the best Batting Average: " + teamName;

        bestBA.setText(text);

    }

/*
    public void setWINS(){
        TextView bestWINS = (TextView) findViewById(R.id.bestWINS);
        //team name with the most wins
        //and set it to team name
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getBestAvgAttrOfTeamsForPitchers("era"),null);
        cursor.moveToFirst();
        String teamName=cursor.getString(0);

        String text= "The team with the most WINS: " + teamName;
        bestWINS.setText(text);
    }
    */
    public void setWHIP(){
        TextView bestWINS = (TextView) findViewById(R.id.bestWHIP);
        //team name with the best WHIP
        //and set it to team name
        String whip = "1.232";
        String teamName="Bob";
        String text= "The team with best WHIP: " + teamName;
        //bestWINS.setText(text);
    }
    public void setERA(){
        TextView bestERA = (TextView) findViewById(R.id.bestERA);
        //team name with the most wins
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getBestAvgAttrOfTeamsForPitchers("era"),null);
        cursor.moveToFirst();
        String teamName=cursor.getString(0);
        String text= "The team with the best ERA: " + teamName;
        //bestERA.setText(text);
    }

}
