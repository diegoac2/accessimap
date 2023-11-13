package edu.illinois.cs465.accessimap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewTripActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
    }

    public void accessHomeScreen(View view) {
        Intent intent = new Intent(NewTripActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // TODO - fix button linking to according nav page
    public void openIndoorNav(View view) {
        Intent intent = new Intent(NewTripActivity.this, IndoorNavActivity.class);
        startActivity(intent);
    }
}