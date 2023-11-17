package edu.illinois.cs465.accessimap;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndoorNavActivity extends AppCompatActivity {
    private static int curr_floor_i = 0; // Most of the code will use this
    private int curr_floor; // This is the actual floor; used for the floor name
    private int[] floor_order;
    private int curr_waypoint = 0;
    private int[] num_waypoints;
    private int[][] x_coords;
    private int[][] y_coords;
    private int[][] zooms;
    private String[] floors;
    private PhotoView photoView;

    private Map<String, Map<String, List<Integer>>> floor_images = new HashMap<String, Map<String, List<Integer>>>() {
        {
            put("Siebel Center for Design", new HashMap<String, List<Integer>>() {{
                List<Integer> temp;
                temp = Arrays.asList(R.drawable.scd_plan2_floor1, R.drawable.scd_plan2_floorb);
                put("Amphitheater", temp);
                temp = Arrays.asList(R.drawable.scd_plan1_floor1);
                put("Sunrise Studio 1040", temp);
                temp = Arrays.asList(R.drawable.scd_plan3_floor1);
                put("Restroom", temp);
            }});
        }};

    // https://www.youtube.com/watch?v=5_pTPTC3aMc
    private void loadJson(String building, String room) {
        try {
            // Load the JSON
            InputStream inputStream = getAssets().open("nav_data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Get the JSON
            String json;
            json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);

            // Start initializing global variables
            JSONArray floorArray = jsonObject.getJSONObject("buildings")
                    .getJSONObject(building).getJSONArray("floors");
            JSONObject location = jsonObject.getJSONObject("buildings")
                    .getJSONObject(building).getJSONObject("rooms").getJSONObject(room);
            JSONArray floorOrder = location.getJSONArray("floor_order");
            JSONArray numWaypoints = location.getJSONArray("num_waypoints");
            JSONArray xCoords = location.getJSONArray("x_coords");
            JSONArray yCoords = location.getJSONArray("y_coords");
            JSONArray zoomArray = location.getJSONArray("zooms");

            String[] all_floors = new String[floorArray.length()];
            int[] floor_order = new int[floorOrder.length()];
            int[] num_waypoints = new int[numWaypoints.length()];
            int[][] x_coords = new int[xCoords.length()][];
            int[][] y_coords = new int[yCoords.length()][];
            int[][] zooms = new int[zoomArray.length()][];

            for (int i = 0; i < floorArray.length(); i++) {
                all_floors[i] = floorArray.getString(i);
            }
            for (int i = 0; i < floorOrder.length(); i++) {
                floor_order[i] = floorOrder.getInt(i);
            }
            for (int i = 0; i < numWaypoints.length(); i++) {
                num_waypoints[i] = numWaypoints.getInt(i);
            }
            for (int i = 0; i < xCoords.length(); i++) {
                int temp_len = xCoords.getJSONArray(i).length();
                int[] temp = new int[temp_len];
                for (int j = 0; j < temp_len; j++) {
                    temp[j] = xCoords.getJSONArray(i).getInt(j);
                }
                x_coords[i] = temp;
            }
            for (int i = 0; i < yCoords.length(); i++) {
                int temp_len = yCoords.getJSONArray(i).length();
                int[] temp = new int[temp_len];
                for (int j = 0; j < temp_len; j++) {
                    temp[j] = yCoords.getJSONArray(i).getInt(j);
                }
                y_coords[i] = temp;
            }
            for (int i = 0; i < zoomArray.length(); i++) {
                int temp_len = zoomArray.getJSONArray(i).length();
                int[] temp = new int[temp_len];
                for (int j = 0; j < temp_len; j++) {
                    temp[j] = zoomArray.getJSONArray(i).getInt(j);
                }
                zooms[i] = temp;
            }

            this.floors = all_floors;
            this.floor_order = floor_order;
            this.num_waypoints = num_waypoints;
            this.x_coords = x_coords;
            this.y_coords = y_coords;
            this.zooms = zooms;
        }
        catch (Exception e) {
            Log.e("TAG", "loadJSON: error" + e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_nav);

        /* PART 1: Fetch the Data */
//        String selectedBuildingFrom = getIntent().getStringExtra("SELECTED_BUILDING_FROM");
//        String selectedRoomFrom = getIntent().getStringExtra("SELECTED_ROOM_FROM");
        String selectedBuildingTo = getIntent().getStringExtra("SELECTED_BUILDING_TO");
        String selectedRoomTo = getIntent().getStringExtra("SELECTED_ROOM_TO");

        // TODO(1): This function would need to add accessibility options if we want to see a different path based on navigation settings
        loadJson(selectedBuildingTo, selectedRoomTo);
        curr_floor = floor_order[curr_floor_i];

        /* PART 2: Navigation Buttons */
        // Initialize the buttons for the navigation bar
        int num_buttons = num_waypoints[curr_floor_i];
        Button[] buttons = new Button[num_buttons];
        for (int i = 0; i < num_buttons; i++) {
            Button button = new Button(this);
            button.setId(i);
            buttons[i] = button;
        }

        // Style the buttons
        for (int i = 0; i < num_buttons; i++) {
            Button button = buttons[i];
            button.setText(Integer.toString(i + 1));
            button.setTextSize(22);
            button.setTextColor(Color.argb(255, 255, 255, 255));

            if (curr_waypoint != i) {
                button.setScaleX(0.75f);
                button.setScaleY(0.75f);
                button.setBackgroundColor(Color.argb(150, 86, 174, 255));
                button.setEnabled(false);
            } else {
                button.setBackgroundColor(Color.argb(255, 86, 174, 255));
            }
        }

        // Enable certain buttons
        if (curr_waypoint - 1 >= 0) {
            buttons[curr_waypoint - 1].setEnabled(true);
        }
        if (curr_waypoint + 1 < buttons.length) {
            buttons[curr_waypoint + 1].setEnabled(true);
            buttons[curr_waypoint + 1].setBackgroundColor(Color.argb(255, 86, 174, 255));
        }

        // Add functionality to buttons
        int[] curr_x = x_coords[curr_floor_i];
        int[] curr_y = y_coords[curr_floor_i];
        int[] curr_zoom = zooms[curr_floor_i];

        for (int i = 0; i < num_buttons; i++) {
            int x = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (x == curr_waypoint) { // If the button is clicked, just move the PDF to the set location
                        photoView.setScale((int) curr_zoom[x], (int) curr_x[x], (int) curr_y[x], false);
                        curr_waypoint = buttons[x].getId();
                    }
                    else { // Change the current waypoint to be that of the button pressed
                        photoView.setScale((int) curr_zoom[x], (int) curr_x[x], (int) curr_y[x], false);
                        curr_waypoint = buttons[x].getId();
                        buttons[x].setScaleX(1f);
                        buttons[x].setScaleY(1f);
                        if (x - 1 >= 0) {
                            buttons[x-1].setScaleX(0.75f);
                            buttons[x-1].setScaleY(0.75f);
                            buttons[x-1].setEnabled(true);
                            buttons[x-1].setBackgroundColor(Color.argb(255, 86, 174, 255));
                        }
                        if (x + 1 < buttons.length) {
                            buttons[x+1].setScaleX(0.75f);
                            buttons[x+1].setScaleY(0.75f);
                            buttons[x+1].setEnabled(true);
                            buttons[x+1].setBackgroundColor(Color.argb(255, 86, 174, 255));
                        }
                        for (int j = 0; j < num_buttons; j++) {
                            if (j != curr_waypoint) {
                                buttons[j].setScaleX(0.75f);
                                buttons[j].setScaleY(0.75f);
                            }
                        }
                    }
                }
            });
        }

        // Add the buttons to the navigation bar
        LinearLayout layout = findViewById(R.id.buttons);
        for (int i = 0; i < num_buttons; i++) {
            layout.addView(buttons[i]);
        }

        // TODO(2): This needs to be checked for paths that contain three or more floors
        /* PART 3: Other Buttons */
        // Create the end button
        Button end_button = new Button(this);
        end_button.setText("END");
        end_button.setTextSize(20);
        end_button.setScaleX(0.9f);
        end_button.setScaleY(0.9f);
        end_button.setTextColor(Color.argb(255, 255, 255, 255));
        end_button.setBackgroundColor(Color.argb(255, 191, 0, 0));

        // Pop-up appears when the user want to end navigation
        end_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IndoorNavActivity.this);
                builder.setCancelable(true);
                builder.setTitle("End navigation");
                builder.setMessage("You are about to end your navigation.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Return to the home screen
                                curr_floor_i = 0; // TODO(3): A small bug arises if the user "leaves" navigation using the swipe motion.
                                Intent i = new Intent(IndoorNavActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Create an up button
        Button up_button = new Button(this);
        up_button.setText("UP");
        up_button.setTextSize(20);
        up_button.setScaleX(0.9f);
        up_button.setScaleY(0.9f);
        up_button.setTextColor(Color.argb(255, 255, 255, 255));
        up_button.setBackgroundColor(Color.argb(255, 221, 138, 2));

        // Create a down button
        Button down_button = new Button(this);
        down_button.setText("DOWN");
        down_button.setTextSize(20);
        down_button.setScaleX(0.9f);
        down_button.setScaleY(0.9f);
        down_button.setTextColor(Color.argb(255, 255, 255, 255));
        down_button.setBackgroundColor(Color.argb(255, 221, 138, 2));

        // Add buttons to the layout depending on the path the user has chosen
        if (curr_floor_i+1 < num_waypoints.length) { // Checks the next floor
            if (floor_order[curr_floor_i+1] > floor_order[curr_floor_i]) {
                layout.addView(up_button);
                up_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        curr_floor_i++;
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
            else {
                layout.addView(down_button);
                down_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        curr_floor_i++;
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
        }

        if (curr_floor_i-1 >= 0) {
            if (floor_order[curr_floor_i-1] > floor_order[curr_floor_i]) {
                layout.addView(up_button, 0);
                up_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        curr_floor_i--;
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
            else {
                layout.addView(down_button, 0);
                down_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        curr_floor_i--;
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
        }

        if (curr_floor_i == num_waypoints.length - 1) { // If the user is on the final floor, add the end button
            layout.addView(end_button);
        }

        /* PART 4: The Floor */
        TextView floor = findViewById(R.id.curr_floor);
        floor.setText(floors[curr_floor]);

        /* PART 5: The Map Viewer */
        // Set up the initial position for the floor plan that the user looks at
        photoView = findViewById(R.id.floor_plan);
        photoView.setImageResource(floor_images.get(selectedBuildingTo).get(selectedRoomTo).get(curr_floor_i));
        photoView.setMinimumScale(1);
        photoView.setMaximumScale(10);

        ViewTreeObserver vto = photoView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                photoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                photoView.setScale((int) curr_zoom[0], (int) curr_x[0], (int) curr_y[0], false);
            }
        });
    }

    public void accessTripScreen(View view) {
        curr_floor_i = 0;
        finish();
    }
}
