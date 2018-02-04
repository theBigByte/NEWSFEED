    package andromeda.petrochemical;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.text.TextUtils;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.facebook.AccessToken;
    import com.facebook.CallbackManager;
    import com.facebook.FacebookCallback;
    import com.facebook.FacebookException;
    import com.facebook.FacebookSdk;
    import com.facebook.login.LoginResult;
    import com.facebook.login.widget.LoginButton;
    import com.google.android.gms.auth.api.Auth;
    import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
    import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
    import com.google.android.gms.auth.api.signin.GoogleSignInResult;
    import com.google.android.gms.common.ConnectionResult;
    import com.google.android.gms.common.SignInButton;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthCredential;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FacebookAuthProvider;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.auth.GoogleAuthProvider;


    public class LoginPage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
        private static final String TAG = "LoginPage";
        LoginButton loginButton;
        CallbackManager callbackManager;
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private EditText loginName;
        private EditText loginPassword;
        private Button loginBtn;
        private ProgressDialog mLoginprogressDialog;
        private Toolbar toolbar;
        private SignInButton googleBtn;
        private static final int RC_SIGN_IN = 1;
        private GoogleApiClient mGoogleApiClient;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.login_page);
            loginButton = (LoginButton) findViewById(R.id.facebookloginButton);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Login");

            mLoginprogressDialog = new ProgressDialog(this);
            loginName = (EditText) findViewById(R.id.nameLogin);
            loginPassword = (EditText) findViewById(R.id.password_main_login);
            loginBtn = (Button) findViewById(R.id.Loginlogin);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = loginName.getText().toString();
                    String password = loginPassword.getText().toString();
                    if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                        mLoginprogressDialog.setTitle("Signing In ");
                        mLoginprogressDialog.setMessage("please wait");
                        mLoginprogressDialog.setCanceledOnTouchOutside(false);
                        mLoginprogressDialog.show();
                        loginUser(email, password);
                    }
                }
            });
            mAuth = FirebaseAuth.getInstance();


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        startActivity(new Intent(LoginPage.this,HomePage.class));
                        Log.d("","user logged in"+ user.getEmail());
                        finish();
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };

            ///////GOOGLE SIGN IN

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this , this )
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            googleBtn = (SignInButton) findViewById(R.id.sign_in_button_google);

            googleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });

            callbackManager = CallbackManager.Factory.create();
            loginButton.setReadPermissions("email", "public_profile");

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    Intent intent = new Intent(LoginPage.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {

                }
            });

        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                mLoginprogressDialog.setMessage("Signing In with google");
                mLoginprogressDialog.show();
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    mLoginprogressDialog.dismiss();
                    Toast.makeText(LoginPage.this,"SignIn failed.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(LoginPage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                mLoginprogressDialog.dismiss();
                                Intent googleIntent = new Intent(LoginPage.this,HomePage.class);
                                googleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(googleIntent);
                                finish();

                            }
                        }
                    });
        }
        @Override
        public void onStart () {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }
        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
        private void loginUser(String email, String password) {

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mLoginprogressDialog.dismiss();
                        Intent loginIntent = new Intent(LoginPage.this,HomePage.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();
                    }else {
                        mLoginprogressDialog.hide();
                        Toast.makeText(LoginPage.this, "login failed" , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        private void handleFacebookAccessToken(AccessToken token) {
            Log.d("TAG", "handleFacebookAccessToken:" + token);

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "signInWithCredential", task.getException());
                                Toast.makeText(LoginPage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            // An unresolvable error has occurred and Google APIs (including Sign-In) will not
            // be available.
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
            Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
        }
    }
