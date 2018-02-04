package andromeda.petrochemical;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private EditText et_name, et_email, et_password, et_cpassword;
    private String name, email, password, cpassword;
    Button signupbtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_form);
        mToolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        firebaseAuth = FirebaseAuth.getInstance();
        et_name = (EditText) findViewById(R.id.name);
        et_email = (EditText) findViewById(R.id.Email);
        progressDialog = new ProgressDialog(this);
        et_password = (EditText) findViewById(R.id.Password);
        et_cpassword = (EditText) findViewById(R.id.Confirm_Password);
        signupbtn = (Button) findViewById(R.id.Signbtn);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        initialise();
        if (!validate()) {
            Toast.makeText(this, "Sign up Failed", Toast.LENGTH_SHORT).show();
        } else {
            onSignUpSuccess();
        }
    }

    public void onSignUpSuccess() {
        Intent intent = new Intent(SignUp.this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public boolean validate() {
        boolean valid = true;
        if (name.isEmpty() || name.length()>32) {
            et_name.setError("please enter valid name");
            valid = false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("please enter valid Email");
            valid = false;
    }
    if (et_password.getText().toString().length()<8 ||!isValidPassword(et_password.getText().toString())){
        et_password.setError("please enter a valid password");
        return false;
    }
    progressDialog.setTitle("Registering User ");
    progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            progressDialog.dismiss();
                            Intent intent = getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else{
                            progressDialog.hide();
                            Toast.makeText(SignUp.this, "Registeration failed" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return valid;
}
    public void initialise(){
        name = et_name.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        cpassword = et_cpassword.getText().toString().trim();
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z]).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
