package ro.ub.fmi.xChat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils.Constants;

public class LoginActivity extends AppCompatActivity {




    private EditText et_email;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private TextView tv_forgotPassword;

    private  ProgressDialog progressDialog;


    private FirebaseAuth mAuth;

    private void initLayoutComponents(){
        et_email =  findViewById(R.id.login_email);
        et_password =  findViewById(R.id.login_password);
        btn_login = findViewById(R.id.login_btnLogin);
        btn_register = findViewById(R.id.login_btnRegister);



        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayoutComponents();

        mAuth = FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intentRegister, Constants.REGISTER_RESULT_CODE);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                authProgressDialog();
                loginSequence();
            }
        });

    }

    private boolean emailCheck() {
        if( et_email.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches() ) {
            return false;
        }
        return true;
    }


    private boolean passwordCheck() {
        if (et_password.getText().toString().isEmpty() ||
                et_password.getText().toString().length()<6 || et_password.getText().toString().length()>24) {
            return false;
        }
        return true;
    }

    private boolean checkField(){
        if(emailCheck()  && passwordCheck() ){
            return true;
        }

        if(!emailCheck()){
            et_email.setError(getString(R.string.txt_enterEmail));
        }

        if(!passwordCheck()){
            et_password.setError(getString(R.string.txt_enterPassword));
        }


        return false;



    }

    private void loginSequence(){

        if(checkField()) {
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
            logginIn(email, password);
        }else{
            progressDialog.dismiss();
        }
    }



    private  void authProgressDialog(){

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.txt_authenticating));
        progressDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == resultCode){
            this.et_email.setText(data.getStringExtra(Constants.REGISTER_EMAIL));
            this.et_password.setText(data.getStringExtra(Constants.REGISTER_PASS));
        }
    }



    private void logginIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),getString(R.string.txt_validEmailPassword),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
