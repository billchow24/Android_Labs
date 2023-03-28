package algonquin.cst2335.chow0144;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.chow0144.databinding.ActivityMainBinding;

/**
 * The MainActivity class is the main activity of the password checker app. It includes a TextView
 * object to display messages, an EditText object to input passwords, and a Button object to
 * initiate the password checking process.
 * @author Shing Kwan Chow
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected RequestQueue queue = null;

    private void loadImage(String iconName) {
        String imageUrl = "https://openweathermap.org/img/w/" + iconName + ".png";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getForecast.setOnClickListener(clk->{
            cityName = binding.editText.getText().toString();
            String stringURL = null;

            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cityName,"UTF-8")+"&appid=c5810579f8e926e96c9f72bdc41695dc&units=metric";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            JSONObject coord = response.getJSONObject("coord");
                            JSONArray weatherArray = response.getJSONArray ( "weather" );
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");
                            int vis = response.getInt("visibility");
                            String name = response.getString( "name" );
                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            runOnUiThread( (  )  -> {
                            binding.temp.setText("The current temperature is " + current);
                            binding.temp.setVisibility(View.VISIBLE);
                            binding.min.setText("The min temperature is " + min);
                            binding.min.setVisibility(View.VISIBLE);
                            binding.max.setText("The max temperature is " + max);
                            binding.max.setVisibility(View.VISIBLE);
                            binding.humidity.setText("The humidity is " + humidity);
                            binding.humidity.setVisibility(View.VISIBLE);
                            binding.descriptions.setText("The description is " + description);
                            binding.descriptions.setVisibility(View.VISIBLE);
                            });


                            String imageUrl = "https://openweathermap.org/img/w/" + iconName + ".png";
                            ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    // Do something with loaded bitmap...
                                    FileOutputStream fOut = null;
                                    try {
                                        fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);

                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                        fOut.flush();
                                        fOut.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();

                                    }

                                }
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error ) -> {

                            });
                            queue.add(imgReq);

                            File file = new File( getFilesDir(), iconName + ".png");
                            if(file.exists()) {
                                try {
                                    Bitmap theImage = BitmapFactory.decodeStream(openFileInput(iconName + ".png"));
                                    binding.icon.setImageBitmap(theImage);
                                    binding.icon.setVisibility(View.VISIBLE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error)->{});

            queue.add(request);


        });



    }

    /**
     * This function check if a character is a special symbol (#$%^&*!@?)
     * @param c The character that we are checking
     * @return Return true if special character is found; false otherwiese
     */
    boolean isSpecialCharacter(char c) {
        switch (c){
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }


    /**
     * This function check if this string has an Upper Case letter, a lower case letter, a number,
     * and a special symbol (#$%^&*!@?). If it is missing any of these 4 requirements, then show a
     * Toast message saying which requirement is missing.
     * @param pw The String object that we are checking
     * @return Return true if the password is complex enough, and false if it is not complex enough,
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for(int i = 0; i < pw.length(); i++){
            if (Character.isDigit(pw.charAt(i)))
                foundNumber = true;
            else if (Character.isUpperCase(pw.charAt(i)))
                foundUpperCase = true;
            else if(Character.isLowerCase(pw.charAt(i)))
                foundLowerCase = true;
            else if(isSpecialCharacter(pw.charAt(i)))
                foundSpecial = true;
        }

        if(!foundUpperCase) {
            Toast.makeText(this, "Passwords are missing an upper case letter", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( ! foundLowerCase) {
            Toast.makeText(this, "Passwords are missing a lower case letter", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( ! foundNumber) {
            Toast.makeText(this, "Passwords are missing a digit character", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(! foundSpecial) {
            Toast.makeText(this, "Passwords are missing a special character", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }
}