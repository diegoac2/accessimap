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

        if (SharedPreferencesHelper.isFirstTime(this)) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            SharedPreferencesHelper.setFirstTime(this, false);

            // finish(); <-- would prevent users from coming back to the home screen
        }
    }

    public void openSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openHistory(View view) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void openFavorite(View view) {
        Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void startNewTrip(View view) {
        Intent intent = new Intent(MainActivity.this, NewTripActivity.class);
        // get switch states from SettingsActivity
        boolean rampSwitchState = getIntent().getBooleanExtra("RAMP_SWITCH_STATE", false);
        boolean elevatorSwitchState = getIntent().getBooleanExtra("ELEVATOR_SWITCH_STATE", false);
        intent.putExtra("RAMP_SWITCH_STATE", rampSwitchState);
        intent.putExtra("ELEVATOR_SWITCH_STATE", elevatorSwitchState);
        startActivity(intent);
    }
}