package algonquin.cst2335.chow0144.ui;

import androidx.appcompat.app.AppCompatActivity;
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

        variableBinding.mytext.setText((CharSequence) model.editString);
        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
            //variableBinding.mytext.setText("Your edit text has: " + model.editString);

        });

        model.editString.observe(this, s->{
            variableBinding.myedittext.setText("Your edit text has: "+s);
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