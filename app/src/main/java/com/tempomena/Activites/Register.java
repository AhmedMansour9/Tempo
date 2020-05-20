package com.tempomena.Activites;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tempomena.R;
import com.tempomena.tokenid.SharedPrefManager;

import java.util.HashMap;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class Register extends AppCompatActivity {
    EditText E_FullName,E_Years,E_Major,E_Location,E_CompanyName,E_Phone,E_PhoneCompany,E_PasswordCompany,E_EmailCompany,E_Email,E_Password,E_FullNameCompany,E_Company;
    TextView T_Company,T_Indvidual;
    FirebaseAuth auth;
    private Button Btn_Register;
    private ProgressBar progressBarRegister;
    String username,Type;

    CardView card_view_Indvidual,card_view_company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       init();
       Btn_Company();
       Btn_Indvidual();
       Registeer_Indvidual();

    }

    private void Registeer_Indvidual() {
        Btn_Register=findViewById(R.id.Btn_Register);
        Btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Type.equals("company")){
                   if (!ValidateFullName_Company()||!ValidateName_Company()
                           ||!ValidatePhone_Company()
                           ||!ValidateEmail_Company()
                   ){
                   } else{
                       username = E_FullNameCompany.getText().toString();
                       String  pas = E_PasswordCompany.getText().toString();
                       String Emaail = E_EmailCompany.getText().toString();
                       progressBarRegister.setVisibility(View.VISIBLE);
                       auth.createUserWithEmailAndPassword(Emaail,pas).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               progressBarRegister.setVisibility(View.GONE);
                               if (!task.isSuccessful()) {
                                   Toast.makeText(Register.this, getResources().getString(R.string.alreadyuser) ,
                                           Toast.LENGTH_SHORT).show();
                               } else {
                                   String token= SharedPrefManager.getInstance(Register.this).getDeviceToken();

                                   DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                                   FirebaseUser user = getInstance().getCurrentUser();
                                   HashMap<String,String> hashMap=new HashMap<>();
                                   hashMap.put("username",E_FullNameCompany.getText().toString());
                                   hashMap.put("c_name",E_Company.getText().toString());
                                   hashMap.put("phone",E_PhoneCompany.getText().toString());
                                   hashMap.put("email",E_EmailCompany.getText().toString());
                                   hashMap.put("token",token);
                                   hashMap.put("id",user.getUid());
                                   databaseReference.push().setValue(hashMap);
                                   SharedPrefManager.getInstance(getBaseContext()).saveMyName(username);
                                   SharedPrefManager.getInstance(getBaseContext()).saveSocialId(user.getUid());
//                                      user.sendEmailVerification();
//                                      Toast.makeText(MainActivity.this,
//                                              getResources().getString(R.string.verfiymail) + user.getEmail(),
//                                              Toast.LENGTH_SHORT).show();
                                   auth.getInstance().signOut();
                                   Intent intent=new Intent(Register.this,Login.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);
                                   finish();
                               }
                           }
                       });
                   }
               }else {
                   if (!ValidateUsername()||!ValidateMajor()||!ValidateYears()||!ValidateLocation()
                           ||!ValidateCompanyName()
                           ||!ValidatePhone()
                           ||!ValidateEmail()
                           ||!ValidatePassword()
                   ){
                   } else{
                       username = E_FullName.getText().toString();
                       String  pas = E_Password.getText().toString();
                       String Emaail = E_Email.getText().toString();
                       progressBarRegister.setVisibility(View.VISIBLE);
                       auth.createUserWithEmailAndPassword(Emaail,pas).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               progressBarRegister.setVisibility(View.GONE);
                               if (!task.isSuccessful()) {
                                   Toast.makeText(Register.this, getResources().getString(R.string.alreadyuser) ,
                                           Toast.LENGTH_SHORT).show();
                               } else {
                                   String token= SharedPrefManager.getInstance(Register.this).getDeviceToken();

                                   DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                                   FirebaseUser user = getInstance().getCurrentUser();
                                   HashMap<String,String> hashMap=new HashMap<>();
                                   hashMap.put("username",E_FullName.getText().toString());
                                   hashMap.put("major",E_Major.getText().toString());
                                   hashMap.put("years",E_Years.getText().toString());
                                   hashMap.put("location",E_Location.getText().toString());
                                   hashMap.put("c_name",E_CompanyName.getText().toString());
                                   hashMap.put("phone",E_Major.getText().toString());
                                   hashMap.put("email",E_Email.getText().toString());
                                   hashMap.put("token",token);
                                   hashMap.put("id",user.getUid());
                                   databaseReference.push().setValue(hashMap);
                                   SharedPrefManager.getInstance(getBaseContext()).saveMyName(username);
                                   SharedPrefManager.getInstance(getBaseContext()).saveSocialId(user.getUid());
//                                      user.sendEmailVerification();
//                                      Toast.makeText(MainActivity.this,
//                                              getResources().getString(R.string.verfiymail) + user.getEmail(),
//                                              Toast.LENGTH_SHORT).show();
                                   auth.getInstance().signOut();
                                   Intent intent=new Intent(Register.this,Login.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);
                                   finish();
                               }
                           }
                       });
                   }
               }


            }
        });

    }

    private void init(){
        Type="indvidual";
        progressBarRegister=findViewById(R.id.progressBarRegister);
        E_FullNameCompany=findViewById(R.id.E_FullNameCompany);
        E_FullName = findViewById(R.id.E_FullName);
        E_Years = findViewById(R.id.E_Years);
        E_Major = findViewById(R.id.E_Major);
        E_Location = findViewById(R.id.E_Location);
        E_Phone = findViewById(R.id.E_Phone);
        E_EmailCompany=findViewById(R.id.E_EmailCompany);
        E_PasswordCompany=findViewById(R.id.E_PasswordCompany);
        E_Company=findViewById(R.id.E_Company);
        E_PhoneCompany=findViewById(R.id.E_PhoneCompany);
        E_CompanyName = findViewById(R.id.E_CompanyName);
        E_Email=findViewById(R.id.E_Email);
        E_Password=findViewById(R.id.E_Password);
        T_Company = findViewById(R.id.T_Company);
        T_Indvidual = findViewById(R.id.T_Indvidual);
        card_view_Indvidual=findViewById(R.id.card_view_Indvidual);
        card_view_company=findViewById(R.id.card_view_company);
        auth = getInstance();

    }

    private Boolean ValidateYears(){
        if (E_Years.getText().toString().trim().isEmpty()){
            E_Years.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidateE_Company(){
        if (E_Major.getText().toString().trim().isEmpty()){
            E_Major.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidateLocation(){
        if (E_Location.getText().toString().trim().isEmpty()){
            E_Location.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidateUsername(){
        if (E_FullName.getText().toString().trim().isEmpty()){
            E_FullName.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidatePhone(){
        if (E_Phone.getText().toString().trim().isEmpty()){
            E_Phone.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidateCompanyName(){
        if (E_CompanyName.getText().toString().trim().isEmpty()){
            E_CompanyName.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidateEmail(){
        String EMAIL=E_Email.getText().toString().trim();
        if (EMAIL.isEmpty()||!isValidEmail(EMAIL)){
            E_Email.setError(getResources().getString(R.string.invalidemail));

            return false;
        }else if(!E_Email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            E_Email.setError(getResources().getString(R.string.invalidemail));
            return false;
        }
        return true;
    }
    private Boolean ValidatePassword(){
        if(E_Password.getText().toString().trim().isEmpty()&&E_Password.getText().toString().trim().length()>3){
            E_Password.setError(getResources().getString(R.string.enterpas));
            return false;
        }else {
            return true;
        }}
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void Btn_Indvidual() {
        T_Indvidual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type="indvidual";

                T_Company.setTextColor(Color.parseColor("#a2a2a2"));
                T_Indvidual.setTextColor(Color.parseColor("#ffffff"));
                T_Company.setBackgroundColor(Color.parseColor("#ffffff"));
                T_Indvidual.setBackgroundResource(R.drawable.bc_leftselected);
                card_view_Indvidual.setVisibility(View.VISIBLE);
                card_view_company.setVisibility(View.GONE);
            }
        });

    }

    public void Btn_Company() {

        T_Company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type="company";
                T_Indvidual.setTextColor(Color.parseColor("#a2a2a2"));
                T_Company.setTextColor(Color.parseColor("#ffffff"));
                T_Indvidual.setBackgroundColor(Color.parseColor("#ffffff"));
                T_Company.setBackgroundResource(R.drawable.bc_rightselected);
                card_view_Indvidual.setVisibility(View.GONE);
                card_view_company.setVisibility(View.VISIBLE);

            }
        });

    }




    private Boolean ValidateFullName_Company(){
        if (E_FullNameCompany.getText().toString().trim().isEmpty()){
            E_FullNameCompany.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidateName_Company(){
        if (E_Company.getText().toString().trim().isEmpty()){
            E_Company.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }


    private Boolean ValidatePhone_Company(){
        if (E_PhoneCompany.getText().toString().trim().isEmpty()){
            E_PhoneCompany.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }

    private Boolean ValidateEmail_Company(){
        if (E_EmailCompany.getText().toString().trim().isEmpty()){
            E_EmailCompany.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
    private Boolean ValidateMajor(){
        if (E_Major.getText().toString().trim().isEmpty()){
            E_Major.setError(getResources().getString(R.string.feildempty));
            return false;
        }
        return true;
    }
}
