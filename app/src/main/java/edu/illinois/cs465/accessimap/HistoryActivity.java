package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HistoryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }
    public void accessHomeScreen(View view) {
        finish();
    }
    public void openIndoorNav1(View view) {
        // Retrieve the selected building and room from Spinners
//        String selectedBuildingFrom = buildingSpinnerFrom.getSelectedItem().toString();
//        String selectedRoomFrom = roomSpinnerFrom.getSelectedItem().toString();
        String selectedBuildingTo = "Siebel Center for Design";
        String selectedRoomTo = "Amphitheater";

        // Create an intent to start the next activity
        Intent intent = new Intent(HistoryActivity.this, IndoorNavActivity.class);

        // Pass the selected items as extras in the intent
//        intent.putExtra("SELECTED_BUILDING_FROM", selectedBuildingFrom);
//        intent.putExtra("SELECTED_ROOM_FROM", selectedRoomFrom);
        intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingTo);
        intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);

        // Start the next activity
        startActivity(intent);
    }
    public void openIndoorNav2(View view) {
        // Retrieve the selected building and room from Spinners
//        String selectedBuildingFrom = buildingSpinnerFrom.getSelectedItem().toString();
//        String selectedRoomFrom = roomSpinnerFrom.getSelectedItem().toString();
        String selectedBuildingTo = "Siebel Center for Design";
        String selectedRoomTo = "Sunrise Studio 1040";

        // Create an intent to start the next activity
        Intent intent = new Intent(HistoryActivity.this, IndoorNavActivity.class);

        // Pass the selected items as extras in the intent
//        intent.putExtra("SELECTED_BUILDING_FROM", selectedBuildingFrom);
//        intent.putExtra("SELECTED_ROOM_FROM", selectedRoomFrom);
        intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingTo);
        intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);

        // Start the next activity
        startActivity(intent);
    }
}