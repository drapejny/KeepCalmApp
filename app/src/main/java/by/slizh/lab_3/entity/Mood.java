package by.slizh.lab_3.entity;

import static by.slizh.lab_3.activity.MainActivity.DB_HELPER;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import by.slizh.lab_3.db.DatabaseHelper;
import by.slizh.lab_3.activity.MainActivity;
import by.slizh.lab_3.ProfileFragment;

public class Mood {
    public int moodImageResourceID;
    public String name;
    public String tableColumnName;
    public int clickedCount;

    public Mood(int moodImageResourceID, String name, String tableColumnName, int clickedCount) {
        this.moodImageResourceID = moodImageResourceID;
        this.name = name;
        this.tableColumnName = tableColumnName;
        this.clickedCount = clickedCount;
    }

    public void incrementMoodClick(){
        clickedCount++;
        updateMoodClickInDatabase();
    }

    private void updateMoodClickInDatabase(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(tableColumnName, clickedCount);
        String where = DatabaseHelper.KEY_id + " = '" + ProfileFragment.userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_MOODS, contentValues, where, null);

        DB_HELPER.close();
    }
}
