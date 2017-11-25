package fr.utt.peirun_zhongping.by_note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private UserDB userDB;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText passwordEidt;
    private Button registerButton;

    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userDB = new UserDB(RegisterActivity.this);
        nameEdit = (EditText) findViewById(R.id.name);
        emailEdit = (EditText) findViewById(R.id.email);
        passwordEidt = (EditText) findViewById(R.id.password);


    }
}
