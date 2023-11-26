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

    private Spinner entranceSpinner;
//    private Spinner roomSpinnerFrom;
    private Spinner buildingSpinnerTo;
    private Spinner roomTypeSpinnerTo;
    private Spinner roomSpinnerTo;

    // Map to store room options based on the selected building
    private Map<String, List<String>> buildingRoomMap;
    private Map<String, List<String>> buildingRoomTypeMap;
    private Map<String, List<String>> buildingEntranceMap;
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
        roomTypeSpinnerTo = findViewById(R.id.room_type_spinner_to); // Add this line

        // Initialize the buildingRoomMap
        buildingRoomMap = new HashMap<>();
        buildingRoomMap.put("Campus Instructional Facility", generateRoomOptionsForBuildingX());
        buildingRoomMap.put("Siebel Center for Design", generateRoomOptionsForBuildingY());

        // Initialize the buildingRoomTypeMap
        buildingRoomTypeMap = new HashMap<>();
        buildingRoomTypeMap.put("Campus Instructional Facility", generateRoomTypesForBuildingX());
        buildingRoomTypeMap.put("Siebel Center for Design", generateRoomTypesForBuildingY());

        buildingEntranceMap = new HashMap<>();
        buildingEntranceMap.put("Campus Instructional Facility", generateEntranceOptionsForBuildingX());
        buildingEntranceMap.put("Siebel Center for Design", generateEntranceOptionsForBuildingY());


        // Set up initial spinner options
        setUpBuildingSpinner(buildingSpinnerFrom);
        setUpBuildingSpinner(buildingSpinnerTo);

        // Set up "from" spinner with entrances
        setUpRoomSpinner(entranceSpinner, buildingSpinnerFrom, true);
        // Set up "to" spinner with room types
        setUpRoomTypeSpinner(roomTypeSpinnerTo, buildingSpinnerTo);
        // Set up "to" spinner with rooms
        setUpRoomSpinner(roomSpinnerTo, roomTypeSpinnerTo, false);

        // Call updateEntranceOptions with the initially selected building for "from" spinner
        String selectedBuildingFrom = buildingSpinnerFrom.getSelectedItem().toString();
        updateEntranceOptions(selectedBuildingFrom, entranceSpinner);

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

                // Enable/disable the spinners based on the selected building
                updateSpinnerStatus(selectedBuilding, roomTypeSpinnerTo, roomSpinnerTo);

                // Update room options based on the selected building

                updateRoomOptions(selectedBuilding, roomTypeSpinnerTo.getSelectedItem().toString(), roomSpinnerTo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void setUpEntranceSpinner(Spinner entranceSpinner, Spinner buildingSpinner, boolean isFrom) {
        // Create ArrayAdapter for entrances
        ArrayAdapter<String> entranceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                generateEntranceOptions());
        entranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        entranceSpinner.setAdapter(entranceAdapter);

        // Set initial selection to "Select an entrance"
        entranceSpinner.setSelection(entranceAdapter.getPosition("-- select an entrance --"));

        // Set item selected listener for entrance Spinner
        entranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedBuilding = buildingSpinner.getSelectedItem().toString();
                String selectedEntrance = (String) parentView.getItemAtPosition(position);

                // Call updateEntranceOptions with the selected building for "From" spinner
                if (isFrom) {
                    updateEntranceOptions(selectedBuilding, entranceSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void setUpRoomTypeSpinner(Spinner spinner, Spinner buildingSpinner) {
        ArrayAdapter<String> roomTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                generateRoomTypeOptions());
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(roomTypeAdapter);
        spinner.setSelection(roomTypeAdapter.getPosition("-- select a room type --"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedBuilding = buildingSpinner.getSelectedItem().toString();
                String selectedRoomType = (String) parentView.getItemAtPosition(position);

                // Update room options based on the selected building and room type
                updateRoomOptions(selectedBuilding, roomTypeSpinnerTo.getSelectedItem().toString(), roomSpinnerTo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void setUpRoomSpinner(Spinner roomSpinner, Spinner roomTypeSpinner, boolean isFrom) {
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

        // Set item selected listener for room type Spinner
        roomTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedBuilding = buildingSpinnerTo.getSelectedItem().toString();
                String selectedRoomType = (String) parentView.getItemAtPosition(position);

                // Update room options based on the selected building and room type
                updateRoomOptions(selectedBuilding, selectedRoomType, roomSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void updateRoomOptions(String selectedBuilding, String selectedRoomType, Spinner roomSpinner) {
        // Update room options based on the selected building and room type
        List<String> roomOptions = buildingRoomMap.get(selectedBuilding);
        ArrayAdapter<String> roomAdapter = (ArrayAdapter<String>) roomSpinner.getAdapter();

        if (roomOptions != null) {
            // Filter room options based on the selected room type
            List<String> filteredRoomOptions = filterRoomOptionsByType(roomOptions, selectedRoomType);

            roomAdapter.clear();
            roomAdapter.addAll(filteredRoomOptions);
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


    private void updateEntranceOptions(String selectedBuilding, Spinner entranceSpinner) {
        // Update entrance options based on the selected building
        List<String> entranceOptions = buildingEntranceMap.get(selectedBuilding);
        ArrayAdapter<String> entranceAdapter = (ArrayAdapter<String>) entranceSpinner.getAdapter();
        if (entranceOptions != null) {
            entranceAdapter.clear();
            entranceAdapter.addAll(entranceOptions);
            entranceAdapter.notifyDataSetChanged();
            entranceSpinner.setEnabled(true);
        } else {
            // Handle the case where entranceOptions is null (optional)
            // You might want to set entranceSpinner to a default state or log an error.
            entranceAdapter.clear();
            entranceAdapter.notifyDataSetChanged();
            entranceSpinner.setEnabled(false);
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
        List<String> entrances = new ArrayList<>();
        entrances.add("-- select an entrance --");
        entrances.add("East Side");
        entrances.add("West Side");
        return entrances;
    }
    private List<String> generateEntranceOptionsForBuildingX() {
        // TODO: Add your logic to generate entrance options for Building X
        List<String> entrances = new ArrayList<>();
        entrances.add("Entrance A");
        entrances.add("Entrance B");
        return entrances;
    }
    private List<String> generateEntranceOptionsForBuildingY() {
        // TODO: Add your logic to generate entrance options for Building Y
        List<String> entrances = new ArrayList<>();
        entrances.add("East Side");
        entrances.add("West Side");
        return entrances;
    }
    private List<String> generateRoomOptionsForBuildingX() {
        // TODO: Add your logic to generate room options for Building X
        List<String> rooms = new ArrayList<>();
        rooms.add("Room X1");
        rooms.add("Room X2");
        rooms.add("Room X3");
        rooms.add("Fountain");
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
    private List<String> filterRoomOptionsByType(List<String> roomOptions, String selectedRoomType) {
        List<String> filteredOptions = new ArrayList<>();

        // Loop through each room option
        for (String roomOption : roomOptions) {
            // Implement your filtering logic based on room type
            // For example, if the room type is "Classroom," only include rooms that belong to classrooms
            if (isRoomOfType(roomOption, selectedRoomType)) {
                filteredOptions.add(roomOption);
            }
        }

        return filteredOptions;
    }

    private boolean isRoomOfType(String room, String roomType) {
        // Implement your specific logic here
        // For demonstration purposes, this example assumes room names contain keywords for types

        if (roomType.equals("Classroom")) {
            // Check if the room name contains keywords for classrooms
            return room.contains("Amphitheater") || room.contains("Sunrise Studio 1040") || room.contains("Room");
        } else if (roomType.equals("Utility")) {
            // Check if the room name contains keywords for utility rooms
            return room.contains("Restroom") || room.contains("Fountain");
        }
        return false;
    }

    private List<String> generateRoomTypeOptions() {
        List<String> roomTypes = new ArrayList<>();
        roomTypes.add("-- select a room type --");
        roomTypes.add("Classroom");
        roomTypes.add("Utility");
        // Add more room types as needed
        return roomTypes;
    }

    private List<String> generateRoomTypesForBuildingX() {
        // TODO: Add your logic to generate room types for Building X
        List<String> roomTypes = new ArrayList<>();
        roomTypes.add("Classroom");
        roomTypes.add("Utility");
        return roomTypes;
    }

    private List<String> generateRoomTypesForBuildingY() {
        // TODO: Add your logic to generate room types for Building Y
        List<String> roomTypes = new ArrayList<>();
        roomTypes.add("Classroom");
        roomTypes.add("Utility");
        return roomTypes;
    }

    private void updateSpinnerStatus(String selectedBuilding, Spinner roomTypeSpinner, Spinner roomSpinner) {
        // Check the selected building and decide whether to enable or disable the spinners
        boolean enableSpinners = true; // Change this based on your logic

        // Enable or disable the spinners accordingly
        roomTypeSpinner.setEnabled(enableSpinners);
        roomSpinner.setEnabled(enableSpinners);
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

        // Create an intent to start the next activity
        Intent intent = new Intent(NewTripActivity.this, IndoorNavActivity.class);

        // Pass the selected items as extras in the intent
        intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingTo);
        intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);
        // Start the next activity
        startActivity(intent);

    }

}