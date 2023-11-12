package edu.illinois.cs465.accessimap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

public class IndoorNavActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_nav);

        // Set up the navigation buttons at the bottom of the screen
        int num_buttons = 5;
        Button[] buttons = new Button[num_buttons];
        for (int i = 0; i < num_buttons; i++) {
            Button button = new Button(this);
            button.setId((i+1));
            button.setText(Integer.toString(i+1));
//            button.setEnabled(false);
            buttons[i] = button;
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons);
        for (int i = 0; i < num_buttons; i++) {
            layout.addView(buttons[i]);
        }


        // Set up the initial position for the floor plan that the user looks at
        PhotoView photoView = (PhotoView) findViewById(R.id.floor_plan);
        photoView.setImageResource(R.drawable.test_floorplan);
        photoView.setMinimumScale(1);
        photoView.setMaximumScale(10);
        photoView.setScale(7, 100, 1000, false);
    }

    public void nextLocation(View view) {
//        PhotoView photoView = (PhotoView) findViewById(R.id.floor_plan);
//        photoView.setImageResource(R.drawable.test_floorplan);
//        photoView.setMinimumScale(1);
//        photoView.setMaximumScale(10);
//
//        photoView.setScale(7, 0, 0, true);
    }
}
