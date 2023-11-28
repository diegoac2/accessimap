package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.widget.Toast;
import java.io.File;
import android.util.Log;


public class HistoryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Call the method to obtain the saved path information
        String savedPathInfo = readPathFromFile();

        // Check if the savedPathInfo is not null before using it
        if (savedPathInfo != null) {
            // Do something with the saved path information, such as displaying it or processing it
            Log.d("SPInfo", "Saved Path Information: " + savedPathInfo);
        } else {
            // Handle the case where reading the path information failed
            Log.d("NRead", "Nothing read");
        }
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
        intent.putExtra("SAVE_OPTION", "NO");

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
        intent.putExtra("SAVE_OPTION", "NO");

        // Start the next activity
        startActivity(intent);
    }
    public void openIndoorNav3(View view) {
        // Retrieve the selected building and room from the saved file
        String savedPathInfo = readPathFromFile();

        if (savedPathInfo != null) {
            Log.d("testNULL", "Building Line Not Null");
            String selectedBuildingTo = extractBuildingFromPathInfo(savedPathInfo);
            String selectedRoomTo = extractRoomFromPathInfo(savedPathInfo);

            // Create an intent to start the next activity
            Intent intent = new Intent(HistoryActivity.this, IndoorNavActivity.class);

            // Pass the selected items as extras in the intent
            intent.putExtra("SELECTED_BUILDING_TO", selectedBuildingTo);
            intent.putExtra("SELECTED_ROOM_TO", selectedRoomTo);
            intent.putExtra("SAVE_OPTION", "NO");

            // Start the next activity
            startActivity(intent);
        } else {
            // Handle the case where reading the path information failed
        }
    }

    // Helper method to extract building information from path info
    // Helper method to extract building information from path info
    private String extractBuildingFromPathInfo(String pathInfo) {
        // Find the line that starts with "Building:"
        String buildingLine = findLineByPrefix(pathInfo, "Building:");

        if (buildingLine != null) {
            // Log the line to check its content
            Log.d("Test Building", "Building Line: " + buildingLine);

            // Extract the building name from the line
            String buildingName = buildingLine.replace("Building:", "").trim();

            // Log the extracted building name
            Log.d("Test Building 2", "Extracted Building: " + buildingName);

            return buildingName;
        } else {
            // Log a message indicating that the line is not found
            Log.d("Test Building", "Building Line not found");

            // Handle the case where the line is not found
            return "DefaultBuilding";
        }
    }


    // Helper method to extract room information from path info
    private String extractRoomFromPathInfo(String pathInfo) {
        // Find the line that starts with "Room:"
        String roomLine = findLineByPrefix(pathInfo, "Room:");
        // Extract the room name from the line
        if (roomLine != null) {
            return roomLine.replace("Room: ", "").trim();
        } else {
            // Handle the case where the line is not found
            return "DefaultRoom";
        }
    }

    // Helper method to find a line in the text that starts with a given prefix
    private String findLineByPrefix(String text, String prefix) {
        String[] lines = text.split("\n");

        for (String line : lines) {
            if (line.startsWith(prefix)) {
                return line;
            }
        }

        // Return null if the line is not found
        return null;
    }


    private String readPathFromFile() {
        try {
            // Define the file name and path
            String fileName = "path_information.txt";
            String filePath = getFilesDir() + "/" + fileName;

            // Check if the file exists before reading
            File file = new File(filePath);
            if (!file.exists()) {
                // Optionally, you can show a message or log that the file does not exist
                Log.e("TAG", "File does not exist: " + filePath);
                return null;
            }

            // Read the contents of the file
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            inputStream.close();

            // Return the contents as a string
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "Error reading path from file: " + e.getMessage());
            // Optionally, you can show an error message to the user
            Toast.makeText(HistoryActivity.this, "Error reading path from file", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}