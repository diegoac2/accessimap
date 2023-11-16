package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity
{
    private Switch rampSwitch, elevatorSwitch, bathroomSwitch;

    private Intent homeIntent;
    private SharedPreferences sp;

    private static final String PREF_NAME = "AccessPrefs";
    protected static final String RAMP_SWITCH_STATE = "RAMP_SWITCH_STATE";
    protected static final String ELEVATOR_SWITCH_STATE = "ELEVATOR_SWITCH_STATE";
    private static final String BATHROOM_SWITCH_STATE = "BATHROOM_SWITCH_STATE";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rampSwitch = findViewById(R.id.ramps);
        elevatorSwitch = findViewById(R.id.elevators);
        bathroomSwitch = findViewById(R.id.bathrooms);

        sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Retrieve the last saved state of the switches
        boolean savedRampSwitchState = sp.getBoolean(RAMP_SWITCH_STATE, false);
        boolean savedElevatorSwitchState = sp.getBoolean(ELEVATOR_SWITCH_STATE, false);
        boolean savedBathroomSwitchState = sp.getBoolean(BATHROOM_SWITCH_STATE, false);

        // Set the switches to their last saved state
        rampSwitch.setChecked(savedRampSwitchState);
        elevatorSwitch.setChecked(savedElevatorSwitchState);
        bathroomSwitch.setChecked(savedBathroomSwitchState);

        // Set a listener for rampSwitch
        rampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                homeIntent = null;
                saveSwitchState(RAMP_SWITCH_STATE, isChecked);
            }
        });

        // Set a listener for elevatorSwitch
        elevatorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                homeIntent = null;
                saveSwitchState(ELEVATOR_SWITCH_STATE, isChecked);
            }
        });

        // Set a listener for bathroomSwitch
        bathroomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                homeIntent = null;
                saveSwitchState(BATHROOM_SWITCH_STATE, isChecked);
            }
        });
    }

    private void saveSwitchState(String switchKey, boolean isChecked)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(switchKey, isChecked);
        editor.apply();
    }

    public void updateText() {
        boolean rampSwitchState = rampSwitch.isChecked();
        boolean elevatorSwitchState = elevatorSwitch.isChecked();
        boolean bathroomSwitchState = bathroomSwitch.isChecked();

        if (homeIntent == null) {
            homeIntent = new Intent(SettingsActivity.this, MainActivity.class);
        }

        homeIntent.putExtra(RAMP_SWITCH_STATE, rampSwitchState);
        homeIntent.putExtra(ELEVATOR_SWITCH_STATE, elevatorSwitchState);
        homeIntent.putExtra(BATHROOM_SWITCH_STATE, bathroomSwitchState);
    }

    public void accessHomeScreen(View view) {
        updateText();
        startActivity(homeIntent);
    }
}