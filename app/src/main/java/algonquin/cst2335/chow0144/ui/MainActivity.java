package algonquin.cst2335.chow0144.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

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

        //variableBinding.mytext.setText(model.editString);
        variableBinding.mybutton.setOnClickListener(click ->
        {
            // Before Live Data
            //model.editString = variableBinding.myedittext.getText().toString();
            //variableBinding.mytext.setText("Your edit text has: " + model.editString);

            // After Live Data
            model.editString.postValue(variableBinding.myedittext.getText().toString());


        });

        model.editString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                variableBinding.mytext.setText("Your edit text has:" + s);
            }
        });
        //btn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        String editString = myedit.getText().toString();
        //        mytext.setText( "Your edit text has: " + editString);
        //    }
        //});
        //String editString = myedit.getText().toString();
        //btn.setOnClickListener(   vw  ->  mytext.setText("Your edit text has: " + editString)    );
    }
}