package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity
{
    private Switch rampSwitch;
    private Switch elevatorSwitch;

    private Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rampSwitch = findViewById(R.id.ramps);
        elevatorSwitch = findViewById(R.id.elevators);

        // Set a listener for switch1
        rampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                homeIntent = null;
            }
        });

        // Set a listener for switch2
        elevatorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                homeIntent = null;
            }
        });
    }

    public void updateText() {
        boolean rampSwitchState = rampSwitch.isChecked();
        boolean elevatorSwitchState = elevatorSwitch.isChecked();

        if (homeIntent == null) {
            homeIntent = new Intent(SettingsActivity.this, MainActivity.class);
        }

        homeIntent.putExtra("RAMP_SWITCH_STATE", rampSwitchState);
        homeIntent.putExtra("ELEVATOR_SWITCH_STATE", elevatorSwitchState);
    }

    public void accessHomeScreen(View view) {
        updateText();
        startActivity(homeIntent);
    }
}