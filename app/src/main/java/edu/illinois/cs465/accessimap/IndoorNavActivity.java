package edu.illinois.cs465.accessimap;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

public class IndoorNavActivity extends AppCompatActivity {
    private String curr_floor = "2";
    private int curr_waypoint = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_nav);

        /* PART 1: Navigation Buttons */
        // Initialize the buttons for the navigation bar
        int num_buttons = 4;
        Button[] buttons = new Button[num_buttons];
        for (int i = 0; i < num_buttons; i++) {
            Button button = new Button(this);
            button.setId(i);
            buttons[i] = button;
        }

        // Style the buttons
        for (int i = 0; i < num_buttons; i++) {
            Button button = buttons[i];
            button.setText(Integer.toString(i+1));
            button.setTextColor(Color.argb(255, 255, 255, 255));

            // Make the other buttons look different
            if (curr_waypoint != i) {
                button.setScaleX(0.75f);
                button.setScaleY(0.75f);
                button.setBackgroundColor(Color.argb(150, 103, 80, 164));
                button.setEnabled(false);
            }
            else {
                button.setBackgroundColor(Color.argb(255, 103, 80, 164));
            }
        }

        // Enable certain buttons
        if (curr_waypoint - 1 > 0) {
            buttons[curr_waypoint-1].setEnabled(true);
        }
        if (curr_waypoint + 1 < buttons.length) {
            buttons[curr_waypoint+1].setEnabled(true);
            buttons[curr_waypoint+1].setBackgroundColor(Color.argb(255, 103, 80, 164));
        }

        // Add functionality to buttons
        float[] x_coords = {150f, 450f};
        float[] y_coords = {950f, 900f};
        for (int i = 0; i < num_buttons; i++) {
            int x = i;
            buttons[i].setOnClickListener(new View.OnClickListener(){
                public void onClick(View arg0) {
                    if (x == curr_waypoint) { // If the button is clicked, just move the PDF to the set location
                        // TODO: How to have each button zoom in and move to a location on the image?
                    }
                    else { // Change the current waypoint to be that of the button pressed
                        curr_waypoint = buttons[x].getId();
                        buttons[x].setScaleX(1f);
                        buttons[x].setScaleY(1f);
                        if (x - 1 >= 0) {
                            buttons[x-1].setScaleX(0.75f);
                            buttons[x-1].setScaleY(0.75f);
                            buttons[x-1].setEnabled(true);
                            buttons[x-1].setBackgroundColor(Color.argb(255, 103, 80, 164));
                        }
                        if (x + 1 < buttons.length) {
                            buttons[x+1].setScaleX(0.75f);
                            buttons[x+1].setScaleY(0.75f);
                            buttons[x+1].setEnabled(true);
                            buttons[x+1].setBackgroundColor(Color.argb(255, 103, 80, 164));
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons);
        for (int i = 0; i < num_buttons; i++) {
            layout.addView(buttons[i]);
        }

        // TODO: When doing multi-level navigation, have the end button be on the final floor,
        //  and have "UP" and "DOWN" buttons after/before the navigation buttons to go up or down.
        /* PART 2: Other Buttons */
        // Create the end button
        Button end_button = new Button(this);
        end_button.setText("END");
        end_button.setScaleX(0.9f);
        end_button.setScaleY(0.9f);
        end_button.setTextColor(Color.argb(255, 255, 255, 255));
        end_button.setBackgroundColor(Color.argb(255, 191, 0, 0));

        // Pop-up appears when the user want to end navigation
        end_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                AlertDialog.Builder builder = new AlertDialog.Builder(IndoorNavActivity.this);
                builder.setCancelable(true);
                builder.setTitle("End navigation");
                builder.setMessage("You are about to end your navigation.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: Return to the home screen
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
        layout.addView(end_button);

        // TODO: For part 3 and 4, maybe figure out a way to map the floor plan(s) to the number of waypoints per floor/number of floors
        /* PART 3: The Floor */
        TextView floor = (TextView) findViewById(R.id.curr_floor);
        floor.setText("FLOOR " + curr_floor);
//        floor.setText("BASEMENT");

        /* PART 4: The Map Viewer */
        // Set up the initial position for the floor plan that the user looks at
        PhotoView photoView = (PhotoView) findViewById(R.id.floor_plan);
        photoView.setImageResource(R.drawable.scd_p1_f1);
        photoView.setMinimumScale(1);
        photoView.setMaximumScale(10);

        ViewTreeObserver vto = photoView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                photoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                photoView.setScale(7f, 150, 950, false);
            }
        });
    }
}