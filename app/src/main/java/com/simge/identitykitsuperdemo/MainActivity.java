package com.simge.identitykitsuperdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.identity.entity.UserAddress;
import com.simge.identitykitsuperdemo.model.UserAddressModel;
import com.simge.identitykitsuperdemo.utils.IdentityKitHelper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    UserAddressModel userAddressModel;
    IdentityKitHelper identityKitHelper;
    CircleImageView imageView;
    TextView nameTv,emailTv,resultTv;
    Button getAddress;
    String name,email,photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        getAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identityKitHelper.getUserAddress();
            }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Log.i("TAG", "requestCode=" + requestCode + ", resultCode=" + resultCode);
            if (resultCode == Activity.RESULT_OK) {
                UserAddress userAddress = UserAddress.parseIntent(data);
                userAddressModel.setName(userAddress.getName());
                userAddressModel.setPhoneNumber(userAddress.getPhoneNumber());
                userAddressModel.setEmail(userAddress.getEmailAddress());
                userAddressModel.setAddress(userAddress.getAddressLine2());
                userAddressModel.setProvince(userAddress.getAdministrativeArea());
                userAddressModel.setCity(userAddress.getLocality());
                userAddressModel.setCountry(userAddress.getCountryCode());
                userAddressModel.setPostalCode(userAddress.getPostalNumber());

                resultTv.setText("Name : " + userAddressModel.getName()+ '\n'+
                        "Email : " + userAddressModel.getEmail()+'\n'+
                        "Phone Number : " +userAddressModel.getPhoneNumber()+'\n'+
                        "Address : " +userAddressModel.getAddress()+'\n'+
                        "Province : " +userAddressModel.getProvince()+'\n'+
                        "City : " + userAddressModel.getCity()+'\n'+
                        "County : " +userAddressModel.getCountry()+'\n'+
                        "PostalCode : " +userAddressModel.getPostalCode());
                emailTv.setText(userAddressModel.getEmail());
                Log.i("TAGG", userAddressModel.getAddress());
            } else {
                Log.d("TAG", "onActivityResult: error");
            }
    }

    void initComponents(){

        if (getIntent() != null) {
            name = getIntent().getStringExtra("displayName");
            email = getIntent().getStringExtra("email");
            photoUrl = getIntent().getStringExtra("photoUrl");
        }

        getAddress = findViewById(R.id.buttonGetAddress);
        nameTv = findViewById(R.id.tvName);
        emailTv = findViewById(R.id.tvEmail);
        resultTv = findViewById(R.id.tvResultAddress);
        imageView = findViewById(R.id.imageView);

        identityKitHelper = new IdentityKitHelper(this);
        userAddressModel=new UserAddressModel();
        Picasso.get().load(photoUrl)
                .placeholder(R.drawable.ic_profile_avatar).error(R.drawable.ic_profile_avatar)
                .into(imageView);

        emailTv.setText(email);
        nameTv.setText(name);

    }
}