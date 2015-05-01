package at.sum.android.cysmn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import at.sum.android.cysmn.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnStartGame(View view)
    {
        Intent intent = new Intent(this, ChooseFactionActivity.class);
        startActivity(intent);
    }

    public void btnViewDebugOutput(View view)
    {
        Intent intent = new Intent(this, OutputDebugActivity.class);
        startActivity(intent);
    }
}
