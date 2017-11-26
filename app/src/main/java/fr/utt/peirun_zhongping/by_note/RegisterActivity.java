package fr.utt.peirun_zhongping.by_note;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private UserDB userDB;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText passwordEidt;
    private Button registerButton;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userDB = new UserDB(RegisterActivity.this);
        nameEdit = (EditText) findViewById(R.id.registerName);
        emailEdit = (EditText) findViewById(R.id.registerEmail);
        passwordEidt = (EditText) findViewById(R.id.loginPassword);
        registerButton =(Button) findViewById(R.id.registerButton);
        dbWriter = userDB.getWritableDatabase();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addUser()){
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    public boolean addUser() {
        try {
            ContentValues user = new ContentValues();
            user.put(userDB.USER_NAME, nameEdit.getText().toString());
            user.put(userDB.EMAIL, emailEdit.getText().toString());
            user.put(userDB.PASSWORD, passwordEidt.getText().toString());
            user.put(userDB.TIME, getTime());
            dbWriter.insert(userDB.TABLE_NAME, null, user);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
}
