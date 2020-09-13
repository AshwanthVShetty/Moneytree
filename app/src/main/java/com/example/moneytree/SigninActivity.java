package com.example.moneytree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneytree.Base.FireBaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SigninActivity extends AppCompatActivity  {

    EditText edt1;
    Button b1;

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private boolean timeout = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

//    private ActivityPhoneAuthBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edt1=findViewById(R.id.signin_etxt);
        b1=findViewById(R.id.signin_btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mVerificationInProgress)
                    getOTP();
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, edt1.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mAuth=FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    SharedPreferences.Editor editor=getSharedPreferences("LoginCheck",0).edit();
                    editor.putBoolean("isLoggedIn",false);
                    editor.apply();
                    Toast.makeText(SigninActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                }
                else {
                }
            }
        };
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            SharedPreferences.Editor editor=getSharedPreferences("LoginCheck",0).edit();
                            editor.putBoolean("isLoggedIn",true);
                            editor.apply();
                            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                            FireBaseHelper.getFireBaseHelper().addUserDoc(firebaseUser.getUid(),firebaseUser.getPhoneNumber());

                            startActivity(new Intent(SigninActivity.this,HomeActivity.class));
                            Log.d(TAG, "signInWithCredential:success");


                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SigninActivity.this,"OTP error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void getOTP(){

        String phoneNumber="+91"+edt1.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        starttimer();
        b1.setText(R.string.submit);
        edt1.setText(null);
        edt1.setHint("Enter the OTP");
    }
    public void starttimer(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mVerificationInProgress=false;
                edt1.setEnabled(false);
                b1.setText("Resend OTP");
            }
        },60000);
    }



//    public void login(){
//        if(!isEmpty(edt1)){
//            //start home activity
//            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//        }
//    }
//    public boolean isEmpty(EditText edt1){
//
//        if(edt1.getText().equals(null)){
//            edt1.setError("Please enter the password");
//            return true;
//        }else {
//            edt1.setError(null);
//            return false;
//        }
//    }
//    public void loadFragment(int i,Fragment obj,String tag){
//
//        FragmentManager fm=getSupportFragmentManager();
//        FragmentTransaction ft=fm.beginTransaction();
//        ft.add(i,obj,tag);
//        ft.addToBackStack(null);
//        ft.commit();
//
//
//    }
//
//    @Override
//    public void onComplete() {
//        acclist=Show_accs.listView;
//
//        acclist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selcted_position=position;
//                b1.setText(R.string.login);
//                Toast.makeText(LoginActivity.this,""+position,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                selcted_position=-1;
//            }
//        });
//
//    }
//
//    @Override
//    public void onCompleteLogin() {
//        edt1=Login.editText;
//        Login.tv1.setText(acclist.getSelectedItem().toString());
//    }

//    class AsynchClassToAddUser extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//
//        }
//    }
}
