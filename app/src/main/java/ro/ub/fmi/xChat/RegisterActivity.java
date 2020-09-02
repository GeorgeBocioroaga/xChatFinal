package ro.ub.fmi.xChat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils.Constants;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_displayName;
    private EditText et_email;
    private EditText et_password;
    private Button btn_register;
    private EditText et_grupa;
    private Spinner spinner;








    private FirebaseAuth mAuth;

    private  ProgressDialog progressDialog ;

    private  DatabaseReference databaseReference;


    private void initLayoutComponents(){
        spinner = (Spinner) findViewById(R.id.register_an);
        et_displayName =  findViewById(R.id.register_nickname);
        et_email =  findViewById(R.id.register_email);
        et_password = findViewById(R.id.register_password);
        btn_register =  findViewById(R.id.register_btnRegister);
        et_grupa = findViewById(R.id.register_grupa);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.an_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);





        progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initLayoutComponents();

        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerProgressDialog();
                registerSequence();

            }
        });
    }



    private boolean checkDisplayname(){
        if(et_displayName.getText().toString().isEmpty() ||
                et_displayName.getText().toString().length()<Constants.MIN_LENGTH ||
                et_displayName.getText().toString().length()>Constants.MAX_LENGTH){
            return false;
        }
        return true;
    }

    private  boolean checkEmail(){
        if(et_email.getText().toString().isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches() ){
            return false;
        }
        return true;
    }

    private boolean checkPassword(){
        if(et_password.getText().toString().isEmpty() ||
                et_password.getText().toString().length()<Constants.MIN_LENGTH ||
                et_password.getText().toString().length()>Constants.MAX_LENGTH){
            return false;
        }
        return true;
    }

    private boolean checkGrupa(){
        if(et_grupa.getText().toString().isEmpty())
            return false;
        return true;
    }

    private boolean checkFields(){
        if(checkDisplayname() && checkEmail() && checkPassword()){
            return true;
        }
        if(!checkDisplayname()){
            et_displayName.setError(getString(R.string.txt_enterDisplayname));
        }
        if(!checkEmail()){
            et_email.setError(getString(R.string.txt_enterEmail));
        }
        if(!checkPassword()){
            et_password.setError(getString(R.string.txt_enterPassword));
        }
        if(!checkGrupa()){
            et_grupa.setError(getString(R.string.txt_error_grupa));
        }





        return false;
    }


    private void registerSequence(){
        if(checkFields()){
            String displayName = et_displayName.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
            String grupa = et_grupa.getText().toString();
            String an = spinner.getSelectedItem().toString();

            registerUser(displayName,email,password, grupa, an);

        }else{
            progressDialog.dismiss();
        }
    }



    private void registerProgressDialog(){

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.txt_registering));
        progressDialog.show();






    }


    private void registerUser(final String displayName, final String email, final String password, final String grupa, final String an){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){



                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("name",displayName);
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("grupa", grupa);
                    userMap.put("an",an);
                    userMap.put("online","false");
                    userMap.put("facultate","ASE");



                    databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(Constants.REGISTER_EMAIL,email);
                                resultIntent.putExtra(Constants.REGISTER_PASS,password);

                                setResult(Constants.REGISTER_RESULT_CODE,resultIntent);

                                finish();
                            }
                        }
                    });





                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),getString(R.string.txt_registerError),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



