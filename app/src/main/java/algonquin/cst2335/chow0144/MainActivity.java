package algonquin.cst2335.chow0144;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The MainActivity class is the main activity of the password checker app. It includes a TextView
 * object to display messages, an EditText object to input passwords, and a Button object to
 * initiate the password checking process.
 * @author Shing Kwan Chow
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen*/
    TextView tv = null;
    /** This holds the edit text box for passwords input*/
    EditText et = null;
    /** This is the login button*/
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.pwMsgTextView);
        et = findViewById(R.id.pwEditText);
        btn = findViewById(R.id.pwButton);

        btn.setOnClickListener(clk ->{
            String password = et.getText().toString();

            if (checkPasswordComplexity(password)){
                tv.setText("Your password meets the requirements");
            }else{
                tv.setText("You shall not pass!");
            }

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