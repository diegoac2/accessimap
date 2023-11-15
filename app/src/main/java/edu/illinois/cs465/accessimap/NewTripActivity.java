package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

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