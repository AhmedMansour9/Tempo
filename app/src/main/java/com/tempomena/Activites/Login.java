package com.tempomena.Activites;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tempomena.Model.UserloginMain;
import com.tempomena.R;
import com.tempomena.tokenid.SharedPrefManager;

public class Login extends AppCompatActivity {
      FirebaseAuth mAuth;
    LoginResult loginResu;
    CallbackManager mCallbackManager;
    Button loginfac;
    DatabaseReference currnetuser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView Privacy,T_forget;
    Button Btn_signup;
    EditText E_EmailLogin;
    EditText E_PasswordLogin;
    CheckBox CheckBox;
    Button Btn_login;
    ProgressBar progressBarLogin;
    Dialog dialog;
    String emaaail;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginfac=findViewById(R.id.Btn_loginFace);
        Btn_login=findViewById(R.id.Btn_login);
        T_forget=findViewById(R.id.T_forget);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        LoginFacebook();
        openPrivacy();
        openRegister();
        openLogin();
        ForgetPassword();

    }


    public void ForgetPassword(){
        T_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(Login.this);
                dialog.setContentView(R.layout.custom_layout);
                Button dialogButton = (Button) dialog.findViewById(R.id.btnreset);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            EditText edit = dialog.findViewById(R.id.getppassword);

                            String EMAIL = edit.getText().toString().trim();
                            if (edit.getText().toString().isEmpty()) {
                            } else if (EMAIL.isEmpty() || !isValidEmail(EMAIL)) {
                                edit.setError(getResources().getString(R.string.invalidemail));
                            } else {
                                emaaail = edit.getText().toString();
                                sendResetPasswordEmail();
                                dialog.dismiss();
                            }

                    }
                });
                dialog.show();
            }
        });
    }

    private void sendResetPasswordEmail() {
        mAuth.sendPasswordResetEmail(emaaail)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this,
                                    getResources().getString(R.string.reseet)
                                            + emaaail,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this,
                                        getResources().getString(R.string.invalidemaail),
                                        Toast.LENGTH_SHORT).show();
                            }}
                    }
                });
    }
    private void openLogin(){
      progressBarLogin=findViewById(R.id.progressBarLogin);
      CheckBox=findViewById(R.id.CheckBox);
      Btn_login=findViewById(R.id.Btn_login);
      E_EmailLogin=findViewById(R.id.E_EmailLogin);
      E_PasswordLogin=findViewById(R.id.E_PasswordLogin);
      Btn_login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
           if(!ValidateEmail() || !ValidatePassword()){


           }else if (!CheckBox.isChecked()){
               Toast.makeText(Login.this, getResources().getString(R.string.checked), Toast.LENGTH_SHORT).show();
           }else {
             String  firstName = E_EmailLogin.getText().toString();
               String passwooord = E_PasswordLogin.getText().toString();
               progressBarLogin.setVisibility(View.VISIBLE);
               mAuth.signInWithEmailAndPassword(firstName, passwooord).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       progressBarLogin.setVisibility(View.GONE);
                       if (!task.isSuccessful()) {


                       } else {
                           mAuth = FirebaseAuth.getInstance();
                           SharedPrefManager.getInstance(getBaseContext()).saveSocialId(mAuth.getUid());
                           Intent intent=new Intent(Login.this,Home.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                           finish();



                       }
                   }
               });
           }

          }
      });
  }

    private Boolean ValidateEmail(){
        String EMAIL=E_EmailLogin.getText().toString().trim();
        if (EMAIL.isEmpty()||!isValidEmail(EMAIL)){
            E_EmailLogin.setError(getResources().getString(R.string.invalidemail));

            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
            E_EmailLogin.setError(getResources().getString(R.string.invalidemail));
            return false;
        }
        return true;
    }
    private Boolean ValidatePassword(){
        if(E_PasswordLogin.getText().toString().trim().isEmpty()&&E_PasswordLogin.getText().toString().trim().length()>3){
            E_PasswordLogin.setError(getResources().getString(R.string.enterpas));
            return false;
        }else {
            return true;
        }}

   public void openPrivacy(){
       Privacy=findViewById(R.id.Privacy);
       Privacy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(Login.this, com.tempomena.Activites.Privacy.class));
           }
       });
   }

    public void openRegister(){
        Btn_signup=findViewById(R.id.Btn_signup);
        Btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, com.tempomena.Activites.Register.class));
            }
        });
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void LoginFacebook(){
        loginfac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckBox.isChecked()){
                    Toast.makeText(Login.this, getResources().getString(R.string.checked), Toast.LENGTH_SHORT).show();
                }else {
                    LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));

                    LoginManager.getInstance().registerCallback(mCallbackManager,
                            new FacebookCallback<LoginResult>() {
                                @Override
                                public void onSuccess(LoginResult loginResult) {
                                    Log.d("Success", "LoginPresenter");
                                    loginResu = loginResult;
                                    handleFacebookAccessToken(loginResult.getAccessToken());
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(Login.this, "LoginPresenter Cancel", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(FacebookException exception) {
                                    Toast.makeText(Login.this, exception.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {

//        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currnetuser= FirebaseDatabase.getInstance().getReference();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String useer=user.getDisplayName();
                            SharedPrefManager.getInstance(getBaseContext()).saveMyName(useer);
                            final String emaail=user.getEmail();
                            SharedPrefManager.getInstance(Login.this).saveSocialId(user.getUid());
                            final String id=user.getUid();
                            String token= SharedPrefManager.getInstance(Login.this).getDeviceToken();
                            UserloginMain a=new UserloginMain(useer,emaail,id,token);
                            currnetuser.child("Users").child(id).setValue(a);
                            Intent intent=new Intent(Login.this,Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
