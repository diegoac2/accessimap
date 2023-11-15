package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Spinner;

public class NewTripActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        TextView textView = findViewById(R.id.preference);
        RadioGroup radioGroup = findViewById(R.id.preferOptions);

        boolean rampSwitchState = getIntent().getBooleanExtra("RAMP_SWITCH_STATE", false);
        boolean elevatorSwitchState = getIntent().getBooleanExtra("ELEVATOR_SWITCH_STATE", false);

        if (rampSwitchState && elevatorSwitchState)
        {
            textView.setText("Do you prefer routes with ramps or with elevators?");
            textView.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
        }
        Spinner buildingSpinnerFrom = findViewById(R.id.building_spinner_from);
        Spinner roomSpinnerFrom = findViewById(R.id.room_spinner_from);
        Spinner buildingSpinnerTo = findViewById(R.id.building_spinner_to);
        Spinner roomSpinnerTo = findViewById(R.id.room_spinner_to);

        // Assuming you have an array or list of buildings and rooms
        String[] buildings = {"Building X", "Building Y"};
        String[] rooms = {"Room A", "Room B", "Room C"};

        // Create ArrayAdapter for buildings
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, buildings);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter for building Spinners
        buildingSpinnerFrom.setAdapter(buildingAdapter);
        buildingSpinnerTo.setAdapter(buildingAdapter);

        // Create ArrayAdapter for rooms
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter for room Spinners
        roomSpinnerFrom.setAdapter(roomAdapter);
        roomSpinnerTo.setAdapter(roomAdapter);

    }

    public void accessHomeScreen(View view) {
        finish();
    }

    // TODO - fix button linking to according nav page
    public void openIndoorNav(View view) {
        Intent intent = new Intent(NewTripActivity.this, IndoorNavActivity.class);
        startActivity(intent);
    }


}