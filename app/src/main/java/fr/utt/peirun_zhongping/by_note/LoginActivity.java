package fr.utt.peirun_zhongping.by_note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    private EditText emailEdit;
    private EditText passwordEdit;
    private TextView registerText;
    private Button loginButton;
    private String login;
    private String password;
    private UserDB userDB;
    private SQLiteDatabase userRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = (EditText) findViewById(R.id.loginEmail);
        passwordEdit = (EditText) findViewById(R.id.loginPassword);
        registerText = (TextView) findViewById(R.id.register);
        loginButton = (Button) findViewById(R.id.loginButton);

        if (userDB == null) {
            userDB = new UserDB(this);

        }
        userRead = userDB.getReadableDatabase();

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();
                Log.d("yu", "yu");

                if (login.equals("") || password.equals("")) {

                    new AlertDialog.Builder(LoginActivity.this).setTitle("Erreur")
                            .setMessage("login and password can not be null").setPositiveButton("Confirm", null)
                            .show();
                } else {
                    if(isUserinfo(login, password)){
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

    }

    public Boolean isUserinfo(String login, String pwd) {
        try{
            String str="select * from users where email=? and password=?";

            Cursor cursor = userRead.rawQuery(str, new String []{login,pwd});
            if(cursor.getCount()<=0){
                Toast.makeText(LoginActivity.this, "This is my  isuserinfo!",
                        Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(LoginActivity.this).setTitle("Erreur")
                        .setMessage("Login or password is not correct！").setPositiveButton("Confirm", null)
                        .show();
                return false;
            }else{
                new AlertDialog.Builder(LoginActivity.this).setTitle("Correct")
                        .setMessage("Sign in successfully").setPositiveButton("Confirm", null)
                        .show();
                return true;
            }

        }catch(SQLiteException e){

        }
        return false;
    }

}



