package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSettings(View view) {
//        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(MainActivity.this, IndoorNavActivity.class);
        startActivity(intent);
    }

    public void openHistory(View view) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void utilityInfo(View view) {
        // TODO -- popup message
    }

    public void startNewTrip(View view) {
        Intent intent = new Intent(MainActivity.this, NewTripActivity.class);
        startActivity(intent);
    }

    public void findUtility(View view) {
        Intent intent = new Intent(MainActivity.this, FindUtilityActivity.class);
        startActivity(intent);
    }
}