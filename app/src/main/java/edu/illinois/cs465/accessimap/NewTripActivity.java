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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewTripActivity extends AppCompatActivity
{
    private Spinner buildingSpinnerFrom;
//    private Spinner roomSpinnerFrom;
    private Spinner buildingSpinnerTo;
    private Spinner entranceSpinner;
    private Spinner roomSpinnerTo;

    // Map to store room options based on the selected building
    private Map<String, List<String>> buildingRoomMap;

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
        buildingSpinnerFrom = findViewById(R.id.building_spinner_from);
//        roomSpinnerFrom = findViewById(R.id.room_spinner_from);
        buildingSpinnerTo = findViewById(R.id.building_spinner_to);
        roomSpinnerTo = findViewById(R.id.room_spinner_to);
        entranceSpinner = findViewById(R.id.entrance_spinner);

        // Initialize the buildingRoomMap
        buildingRoomMap = new HashMap<>();
        buildingRoomMap.put("Campus Instructional Facility", generateRoomOptionsForBuildingX());
        buildingRoomMap.put("Siebel Center for Design", generateRoomOptionsForBuildingY());

        // Set up initial spinner options
        // TODO: We will probably not have a building to building navigation
        setUpBuildingSpinner(buildingSpinnerFrom);
        setUpBuildingSpinner(buildingSpinnerTo);

        setUpRoomSpinner(entranceSpinner, buildingSpinnerFrom);
        setUpRoomSpinner(roomSpinnerTo, buildingSpinnerTo);

        setUpEntranceSpinner(entranceSpinner, buildingSpinnerTo);


        // Assuming you have an array or list of buildings and rooms
//        String[] buildings = {"Building X", "Building Y"};
//        String[] rooms = {"Room A", "Room B", "Room C"};

        // Create ArrayAdapter for buildings
//        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, buildings);
//        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter for building Spinners
//        buildingSpinnerFrom.setAdapter(buildingAdapter);
//        buildingSpinnerTo.setAdapter(buildingAdapter);

        // Create ArrayAdapter for rooms
//        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms);
//        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter for room Spinners
//        roomSpinnerFrom.setAdapter(roomAdapter);
//        roomSpinnerTo.setAdapter(roomAdapter);

    }
    private void setUpBuildingSpinner(Spinner spinner) {
        // Create ArrayAdapter for buildings
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                generateBuildingOptions());
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter for building Spinners
        spinner.setAdapter(buildingAdapter);

        // Set initial selection to "Select a building"
        spinner.setSelection(buildingAdapter.getPosition("-- select a building --"));

        // Set item selected listener for building Spinners
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedBuilding = (String) parentView.getItemAtPosition(position);

                // Update room options based on the selected building
                updateRoomOptions(selectedBuilding, spinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void setUpEntranceSpinner(Spinner entranceSpinner, Spinner buildingSpinner) {
        // Create ArrayAdapter for buildings
        ArrayAdapter<String> entranceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                generateEntranceOptions());
        entranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        entranceSpinner.setAdapter(entranceAdapter);
//        entranceSpinner.setEnabled(false);

        // Set the ArrayAdapter for building Spinners
        entranceSpinner.setAdapter(entranceAdapter);

        // Set initial selection to "Select a building"
        entranceSpinner.setSelection(entranceAdapter.getPosition("-- select an entrance --"));

        // Set item selected listener for building Spinners
        entranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // TODO: Does nothing right now
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void setUpRoomSpinner(Spinner roomSpinner, Spinner buildingSpinner) {
        // Set up room Spinner with an empty list initially
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(roomAdapter);
        roomSpinner.setEnabled(false);

        // Set item selected listener for room Spinner
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle room selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Set item selected listener for building Spinner
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedBuilding = (String) parentView.getItemAtPosition(position);

                // Update room options based on the selected building
                updateRoomOptions(selectedBuilding, roomSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }
    private void updateRoomOptions(String selectedBuilding, Spinner roomSpinner) {
        // Update room options based on the selected building
        List<String> roomOptions = buildingRoomMap.get(selectedBuilding);
        ArrayAdapter<String> roomAdapter = (ArrayAdapter<String>) roomSpinner.getAdapter();
        if (roomOptions != null) {
            roomAdapter.clear();
            roomAdapter.addAll(roomOptions);
            roomAdapter.notifyDataSetChanged();
            roomSpinner.setEnabled(true);
        } else {
            // Handle the case where roomOptions is null (optional)
            // You might want to set roomSpinner to a default state or log an error.
            roomAdapter.clear();
            roomAdapter.notifyDataSetChanged();
            roomSpinner.setEnabled(false);
        }
    }
    private List<String> generateBuildingOptions() {
        List<String> buildings = new ArrayList<>();
        buildings.add("-- select a building --");
        buildings.add("Campus Instructional Facility");
        buildings.add("Siebel Center for Design");
        return buildings;
    }

    private List<String> generateEntranceOptions() {
        List<String> buildings = new ArrayList<>();
        buildings.add("-- select an entrance --");
        buildings.add("East Side");
        buildings.add("West Side");
        return buildings;
    }

    private List<String> generateRoomOptionsForBuildingX() {
        // TODO: Add your logic to generate room options for Building X
        List<String> rooms = new ArrayList<>();
        rooms.add("Room X1");
        rooms.add("Room X2");
        rooms.add("Room X3");
        return rooms;
    }

    private List<String> generateRoomOptionsForBuildingY() {
        // TODO: Add your logic to generate room options for Building Y
        List<String> rooms = new ArrayList<>();
        rooms.add("Amphitheater");
        rooms.add("Sunrise Studio 1040");
        rooms.add("Restroom");
        return rooms;
    }
    public void accessHomeScreen(View view) {
        finish();
    }

    // TODO - fix button linking to according nav page
    public void openIndoorNav(View view) {
        // Retrieve the selected building and room from Spinners
        String selectedBuildingFrom = buildingSpinnerFrom.getSelectedItem().toString();
//        String selectedRoomFrom = roomSpinnerFrom.getSelectedItem().toString();
        String selectedBuildingTo = buildingSpinnerTo.getSelectedItem().toString();
        String selectedRoomTo = roomSpinnerTo.getSelectedItem().toString();

        if (selectedBuildingFrom.equals(selectedBuildingTo)) {
            // Create an intent to start the next activity
            Intent intent = new Intent(NewTripActivity.this, IndoorNavActivity.class);

            // Pass the selected items as extras in the intent
//        intent.putExtra("SELECTED_BUILDING_FROM", selectedBuildingFrom);
//        intent.putExtra("SELECTED_ROOM_FROM", selectedRoomFrom);
            intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingTo);
            intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);

            // Start the next activity
            startActivity(intent);
        } else {
            Intent intent = new Intent(NewTripActivity.this, OutdoorNavActivity.class);
            intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingTo);
            intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);
            // Start the next activity
            startActivity(intent);
        }
    }

}


//package edu.illinois.cs465.accessimap;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.AdapterView;
//import android.view.View;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Spinner;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class NewTripActivity extends AppCompatActivity
//{
//    private Spinner buildingSpinnerFrom;
//    private Spinner roomSpinnerFrom;
//    private Spinner buildingSpinnerTo;
//    private Spinner roomSpinnerTo;
//    private Spinner utilitySpinnerTo;
//
//    // Map to store room options based on the selected building
//    private Map<String, List<String>> buildingRoomMap;
//
//    private static final String rampElevatorBody = "Do you prefer routes with ramps or with elevators?";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_trip);
//
//        TextView textView = findViewById(R.id.preference);
//        RadioGroup radioGroup = findViewById(R.id.preferOptions);
//
//        boolean rampSwitchState = getIntent().getBooleanExtra(SettingsActivity.RAMP_SWITCH_STATE, false);
//        boolean elevatorSwitchState = getIntent().getBooleanExtra(SettingsActivity.ELEVATOR_SWITCH_STATE, false);
//
//        if (rampSwitchState && elevatorSwitchState)
//        {
//            textView.setText(rampElevatorBody);
//            textView.setVisibility(View.VISIBLE);
//            radioGroup.setVisibility(View.VISIBLE);
//        }
//        else {
//            textView.setVisibility(View.GONE);
//            radioGroup.setVisibility(View.GONE);
//        }
//        buildingSpinnerFrom = findViewById(R.id.building_spinner_from);
//        roomSpinnerFrom = findViewById(R.id.room_spinner_from);
//        buildingSpinnerTo = findViewById(R.id.building_spinner_to);
//        roomSpinnerTo = findViewById(R.id.room_spinner_to);
//        utilitySpinnerTo = findViewById(R.id.utility_spinner_to);
//
//        // Initialize the buildingRoomMap
//        buildingRoomMap = new HashMap<>();
//        buildingRoomMap.put("Campus Instructional Facility", generateRoomOptionsForBuildingX());
//        buildingRoomMap.put("Siebel Center for Design", generateRoomOptionsForBuildingY());
//
//        // Set up initial spinner options
//        // TODO: We will probably not have a building to building navigation
////        setUpBuildingSpinner(buildingSpinnerFrom);
//        setUpBuildingSpinner(buildingSpinnerTo);
//        setUpUtilitySpinner(utilitySpinnerTo);
//
////        setUpRoomSpinner(roomSpinnerFrom, buildingSpinnerFrom);
//        setUpRoomSpinner(roomSpinnerTo, buildingSpinnerTo);
//
//    }
//    private void setUpBuildingSpinner(Spinner spinner) {
//        // Create ArrayAdapter for buildings
//        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
//                generateBuildingOptions());
//        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Set the ArrayAdapter for building Spinners
//        spinner.setAdapter(buildingAdapter);
//
//        // Set initial selection to "Select a building"
//        spinner.setSelection(buildingAdapter.getPosition("-- select a building --"));
//
//        // Set item selected listener for building Spinners
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
//    }
//    private void setUpRoomSpinner(Spinner roomSpinner, Spinner buildingSpinner) {
//        // Set up room Spinner with an empty list initially
//        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
//        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roomSpinner.setAdapter(roomAdapter);
//        roomSpinner.setEnabled(false);
//
//        // Set item selected listener for room Spinner
//        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // Handle room selection if needed
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
//
//        // Set item selected listener for building Spinner
//        utilitySpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String selectedBuilding = buildingSpinnerTo.getSelectedItem().toString();
//                updateRoomOptions(selectedBuilding, roomSpinnerTo, utilitySpinnerTo);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
//
//    }
//
//    private void updateRoomOptions(String selectedBuilding, Spinner roomSpinner, Spinner utilitySpinner) {
//        String selectedUtility = utilitySpinner.getSelectedItem().toString();
//
//        List<String> roomOptions;
//
//        // Update room options based on the selected utility type
//        if ("Room".equals(selectedUtility)) {
//            roomOptions = buildingRoomMap.get(selectedBuilding);
//        } else if ("Restroom".equals(selectedUtility)) {
//            // Implement logic for restroom options
//            roomOptions = generateRestroomOptions();
//        } else {
//            // Handle other utility types if needed
//            roomOptions = new ArrayList<>();
//        }
//
//        ArrayAdapter<String> roomAdapter = (ArrayAdapter<String>) roomSpinner.getAdapter();
//        if (roomOptions != null) {
//            roomAdapter.clear();
//            roomAdapter.addAll(roomOptions);
//            roomAdapter.notifyDataSetChanged();
//            roomSpinner.setEnabled(true);
//        } else {
//            roomAdapter.clear();
//            roomAdapter.notifyDataSetChanged();
//            roomSpinner.setEnabled(false);
//        }
//    }
//
//    private List<String> generateRestroomOptions() {
//        // Implement logic to generate restroom options
//        List<String> restrooms = new ArrayList<>();
//        restrooms.add("Restroom 1");
//        // Add other restroom options as needed
//        return restrooms;
//    }
//
//    private List<String> generateBuildingOptions() {
//        List<String> buildings = new ArrayList<>();
//        buildings.add("-- select a building --");
//        buildings.add("Campus Instructional Facility");
//        buildings.add("Siebel Center for Design");
//        return buildings;
//    }
//
//    private List<String> generateRoomOptionsForBuildingX() {
//        // TODO: Add your logic to generate room options for Building X
//        List<String> rooms = new ArrayList<>();
//        rooms.add("Room X1");
//        rooms.add("Room X2");
//        rooms.add("Room X3");
//        return rooms;
//    }
//
//    private List<String> generateRoomOptionsForBuildingY() {
//        // TODO: Add your logic to generate room options for Building Y
//        List<String> rooms = new ArrayList<>();
//        rooms.add("Amphitheater");
//        rooms.add("Sunrise Studio 1040");
//        return rooms;
//    }
//    public void accessHomeScreen(View view) {
//        finish();
//    }
//
//    private void setUpUtilitySpinner(Spinner spinner) {
//        // Create ArrayAdapter for utility types
//        ArrayAdapter<String> utilityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
//                generateUtilityOptions());
//        utilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Set the ArrayAdapter for utility Spinners
//        spinner.setAdapter(utilityAdapter);
//
//        // Set initial selection to "Select a utility type"
//        spinner.setSelection(utilityAdapter.getPosition("-- select a utility type --"));
//
//        // Set item selected listener for utility Spinners
//        utilitySpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String selectedBuilding = buildingSpinnerTo.getSelectedItem().toString();
//                updateRoomOptions(selectedBuilding, roomSpinnerTo, utilitySpinnerTo);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
//
//    }
//
//    private List<String> generateUtilityOptions() {
//        List<String> utilityTypes = new ArrayList<>();
//        utilityTypes.add("-- select a utility type --");
//        utilityTypes.add("Room");
//        utilityTypes.add("Restroom");
//        return utilityTypes;
//    }
//    // TODO - fix button linking to according nav page
//    public void openIndoorNav(View view) {
//        String selectedBuildingTo = buildingSpinnerTo.getSelectedItem().toString();
//        String selectedRoomTo = roomSpinnerTo.getSelectedItem().toString();
//
//        // Create an intent to start the next activity
//        Intent intent = new Intent(NewTripActivity.this, IndoorNavActivity.class);
//
//        // Pass the selected items as extras in the intent
//        intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingTo);
//        intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);
//
//        // Start the next activity
//        startActivity(intent);
//    }
//
//}