package fr.utt.peirun_zhongping.by_note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private UserSessionManager session;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEdit = (EditText) findViewById(R.id.loginEmail);
        passwordEdit = (EditText) findViewById(R.id.loginPassword);
        registerText = (TextView) findViewById(R.id.register);
        loginButton = (Button) findViewById(R.id.loginButton);

        session = new UserSessionManager(getApplicationContext());

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
                if (login.equals("") || password.equals("")) {

                    new AlertDialog.Builder(LoginActivity.this).setTitle("Erreur")
                            .setMessage("login and password can not be null").setPositiveButton("Confirm", null)
                            .show();
                } else {
                    if(isUserinfo(login, password)){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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
                new AlertDialog.Builder(LoginActivity.this).setTitle("Erreur")
                        .setMessage("Login or password is not correct！").setPositiveButton("Confirm", null)
                        .show();
                return false;
            }else{
                cursor.moveToFirst();
                session.createUserLoginSession(cursor.getInt(cursor.getColumnIndex(UserDB.USER_ID)), cursor.getString(cursor.getColumnIndex(UserDB.USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(UserDB.EMAIL)));
                Toast.makeText(getApplicationContext(), "Sign in successfully", Toast.LENGTH_SHORT).show();
                return true;
            }

        }catch(SQLiteException e){

        }
        return false;
    }

}



