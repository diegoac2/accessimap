package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class NewTripActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    private Spinner navSelectRoomTypeSpinner;
    private ArrayAdapter<CharSequence> navSelectRoomTypeAdapter;

    private Spinner navSelectBuildingSpinner;
    private ArrayAdapter<CharSequence> navSelectBuildingAdapter;

    private Spinner fromEntranceSpinner;
    private ArrayAdapter<CharSequence> fromEntranceAdapter;

    private Spinner toLocationSpinner;
    private ArrayAdapter<CharSequence> toLocationSiebelAdapter;
    private ArrayAdapter<CharSequence> toRestroomSiebelAdapter;
    private ArrayAdapter<CharSequence> toLocationCifAdapter;

    private static final String rampElevatorBody = "Do you prefer routes with ramps or with elevators?";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        TextView textView = findViewById(R.id.preference);
        RadioGroup radioGroup = findViewById(R.id.preferOptions);

        boolean rampSwitchState = getIntent().getBooleanExtra(SettingsActivity.RAMP_SWITCH_STATE, false);
        boolean elevatorSwitchState = getIntent().getBooleanExtra(SettingsActivity.ELEVATOR_SWITCH_STATE, false);

        if (rampSwitchState && elevatorSwitchState)
        {
            textView.setText(rampElevatorBody);
            textView.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
        }

        // get spinners
        navSelectRoomTypeSpinner = (Spinner) findViewById(R.id.navigation_type);
        navSelectBuildingSpinner = (Spinner) findViewById(R.id.navigation_type_building_from);
        fromEntranceSpinner = (Spinner) findViewById(R.id.entrance_spinner);
        toLocationSpinner = (Spinner) findViewById(R.id.room_spinner_to);

        // we want only the select room type spinner to be enabled at first
        navSelectBuildingSpinner.setEnabled(false);
        fromEntranceSpinner.setEnabled(false);
        toLocationSpinner.setEnabled(false);

        // on item selected, we need to enable/disable spinners according to the values of other spinners
        navSelectRoomTypeSpinner.setOnItemSelectedListener(this);
        navSelectBuildingSpinner.setOnItemSelectedListener(this);
        fromEntranceSpinner.setOnItemSelectedListener(this);
        toLocationSpinner.setOnItemSelectedListener(this);

        // add adapter to select room type spinner to get started with setting up the options
        navSelectRoomTypeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.select_room_type,
                android.R.layout.simple_spinner_item
        );
        navSelectRoomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        navSelectRoomTypeSpinner.setAdapter(navSelectRoomTypeAdapter);

        navSelectBuildingAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.select_building,
                android.R.layout.simple_spinner_item
        );
        navSelectBuildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        navSelectBuildingSpinner.setAdapter(navSelectBuildingAdapter);

        fromEntranceAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.select_entrance,
                android.R.layout.simple_spinner_item
        );
        fromEntranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromEntranceSpinner.setAdapter(fromEntranceAdapter);

        toLocationSiebelAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.select_siebel_classrooms,
                android.R.layout.simple_spinner_item
        );
        toLocationSiebelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toRestroomSiebelAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.select_siebel_restrooms,
                android.R.layout.simple_spinner_item
        );
        toRestroomSiebelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        toLocationCifAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.select_cif_classrooms,
                android.R.layout.simple_spinner_item
        );
        toLocationCifAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        Spinner spinner = (Spinner) parent;
        String selectedNavigationType = (String) parent.getItemAtPosition(pos);
        String selectedBuildingFrom = (String) parent.getItemAtPosition(pos);
        if(spinner.getId() == R.id.navigation_type) {
            selectedNavigationType = (String) parent.getItemAtPosition(pos);
            switch (selectedNavigationType) {
                case "--select a room type--":
                    navSelectBuildingSpinner.setEnabled(false);
                    break;
                case "Classrooms":
                    toLocationSpinner.setAdapter(toLocationSiebelAdapter);
                    navSelectBuildingSpinner.setEnabled(true);
                    if(selectedBuildingFrom == "--select a classroom--"){
                        toLocationSpinner.setEnabled(true);
                    }
                    toLocationSpinner.setEnabled(true);
                    break;
                case "Restrooms":
                    toLocationSpinner.setAdapter(toRestroomSiebelAdapter);
                    navSelectBuildingSpinner.setEnabled(true);
                case "Water Fountains":
                    navSelectBuildingSpinner.setEnabled(true);
                    toLocationSpinner.setEnabled(false);
                    break;
            }
        } else if(spinner.getId() == R.id.navigation_type_building_from) {
            switch (selectedBuildingFrom) {
                case "--select a building--":
                    fromEntranceSpinner.setEnabled(false);
                    break;
                case "Siebel Center for Design":
                    enableToAndFromSpinners();
                    if(selectedNavigationType == "Classrooms") {
                        toLocationSpinner.setAdapter(toLocationSiebelAdapter);
                    }
                    else if(selectedNavigationType == "Restrooms"){
                        toLocationSpinner.setAdapter(toRestroomSiebelAdapter);
                    }
                    break;
                case "Campus Instructional Facility":
                    enableToAndFromSpinners();
                    toLocationSpinner.setAdapter(toLocationCifAdapter);
                    break;
            }
        } else if (spinner.getId() == R.id.entrance_spinner) {
            String selectedEntrance = (String) parent.getItemAtPosition(pos);
        } else if (spinner.getId() == R.id.room_spinner_to) {
            String selectedRoomTo = (String) parent.getItemAtPosition(pos);
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }

    private void enableToAndFromSpinners() {
        fromEntranceSpinner.setEnabled(true);
        if ((navSelectRoomTypeSpinner.getSelectedItem().toString()).equals("Classrooms")) {
            toLocationSpinner.setEnabled(true);
        } else {
            toLocationSpinner.setEnabled(false);
        }
    }

    public void accessHomeScreen(View view) {
        finish();
    }

    // TODO - fix button linking to according nav page
    public void openIndoorNav(View view) {
        // Retrieve the selected building and room from Spinners
        String selectedBuildingFrom = navSelectBuildingSpinner.getSelectedItem().toString();
        String selectedRoomTo = toLocationSpinner.getSelectedItem().toString();

        // Create an intent to start the next activity
        Intent intent = new Intent(NewTripActivity.this, IndoorNavActivity.class);

        // Pass the selected items as extras in the intent
        intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingFrom);
        intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);
        // Start the next activity
        startActivity(intent);

    }

}