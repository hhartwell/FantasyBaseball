package harvey.com.fantasybaseball;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * class used for testing purposes only
 */
public class DataBaseTestingGrounds extends AppCompatActivity {
    final static String TAG = "DATABASE TESTING GROUND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_testing_grounds);
        this.deleteDatabase(ExcelToSQLite.DATABASE_NAME);

        ExcelToSQLite databaseHelper = new ExcelToSQLite(this);
        try {
            databaseHelper.populateTeamsTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, databaseHelper.getSelectAllPlayersList().toString());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(databaseHelper.insertIntoTeamsTable());

        // insert rows into teams

        // first row in teams
        stmt.bindString(1, "team name");
        stmt.bindLong(2, 5099190888L);
        stmt.execute();

        // second row in teams
        stmt.bindString(1, "team 2");
        stmt.bindLong(2, 1112223333L);
        stmt.execute();
        Log.d(TAG, databaseHelper.getSelectAllTeamsList().toString());

        // query teams
        String baQuery = databaseHelper.getOrderByQuery("ba");
        Cursor cursor = db.rawQuery(baQuery, null);

        databaseHelper.close();

    }
}
