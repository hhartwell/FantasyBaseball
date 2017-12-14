package harvey.com.fantasybaseball;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Team extends AppCompatActivity {
// this class will display a user's team
    static final String TAG = "TEAM";
    ExcelToSQLite databaseHelper;
    Long phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        databaseHelper = new ExcelToSQLite(this);
        ListView listView = (ListView) (findViewById(R.id.roster));
        Intent recievedIntent = getIntent();
        phoneNumber = recievedIntent.getLongExtra("phone_number", 5099190888L);

        //query SQL to populate listView, default sort by name
        Log.d(TAG, String.valueOf(phoneNumber));

        //Select players ordered by their batting average
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(databaseHelper.getNewTeamByNumber(phoneNumber), null);
        // need a cursor adapter
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_4,
                cursor,
                new String[]{"player_name", "_id",  "ba", "era"},
                new int[] {R.id.player_name_list, R.id.player_id_list, R.id.player_first, R.id.player_second},
                0);
        listView.setAdapter(cursorAdapter);
    }
}


