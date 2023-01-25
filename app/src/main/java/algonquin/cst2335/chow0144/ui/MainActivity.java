package algonquin.cst2335.chow0144.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import algonquin.cst2335.chow0144.databinding.ActivityMainBinding;
import algonquin.cst2335.chow0144.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        // Button Click Event
        variableBinding.mybutton.setOnClickListener(click ->
        {

            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });

        model.editString.observe(this, s ->{
                variableBinding.mytext.setText("Your edit text has:" + s);
        });

        // Box selection Event
        model.isSelected.observe(this, selected -> {

            variableBinding.checkbox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "The value is now: " + selected, duration);
            toast.show();
        });

        variableBinding.checkbox.setOnCheckedChangeListener((CompoundButton chBox, boolean isChecked)->{
            model.isSelected.postValue(isChecked);
        });

        variableBinding.switch1.setOnCheckedChangeListener((CompoundButton swith1, boolean isChecked)->{
            model.isSelected.postValue(isChecked);
        });

        variableBinding.radioButton.setOnCheckedChangeListener((CompoundButton radioBtn, boolean isChecked)->{
            model.isSelected.postValue(isChecked);
        });

        //Image Click Event
        variableBinding.imgView.setOnClickListener(click->{

        });

        variableBinding.myimagebutton.setOnClickListener(click->{
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "The width = " + variableBinding.myimagebutton.getWidth()
                    + " and height = " + variableBinding.myimagebutton.getHeight(), duration);
            toast.show();
        });
    }
}