package algonquin.cst2335.chow0144;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> cameraResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        TextView welcomeMsg = findViewById(R.id.textView);
        ImageView profileImage = findViewById(R.id.imageView2);
        welcomeMsg.setText("Welcome " + emailAddress);

        cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Toast.makeText(getApplicationContext(), "Captured", Toast.LENGTH_SHORT).show();
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;
                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch ( IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


        File file = new File( getFilesDir(), "Picture.png");
        if(file.exists()) {
            try {
                Bitmap theImage = BitmapFactory.decodeStream(openFileInput("Picture.png"));
                profileImage.setImageBitmap(theImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button callButton = findViewById(R.id.call);
        EditText phoneNum = findViewById(R.id.editTextPhone);
        Button changePicture = findViewById(R.id.changePic);

        callButton.setOnClickListener(clk -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNum.getText()));
            startActivity(call);
        });

        changePicture.setOnClickListener(clk -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            }

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });
    }
}