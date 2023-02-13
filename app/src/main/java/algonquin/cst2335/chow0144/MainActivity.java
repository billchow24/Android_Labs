package algonquin.cst2335.chow0144;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w( "TAG", "First function that gets created when an application is launched." );
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w( "TAG", "The application is now visible on screen." );

        Button loginButton = findViewById(R.id.login_button);
        EditText emailEditText = findViewById(R.id.email_text);

        loginButton.setOnClickListener( clk-> {
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", emailEditText.getText().toString());
            startActivity(nextPage);
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w( "TAG", " The application is now responding to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.w( "TAG", "The application no longer responds to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.w( "TAG", "The application is no longer visible." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.w( "TAG", "Any memory used by the application is freed." );
    }
}