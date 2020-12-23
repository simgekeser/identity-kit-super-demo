package com.simge.identitykitsuperdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "HuaweiIdActivity";
    private AccountAuthService mAuthManager;
    private AccountAuthParams mAuthParam;

    HuaweiIdAuthButton huaweiIdAuthButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        huaweiIdAuthButton = findViewById(R.id.loginButton);

        huaweiIdAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInCode();
            }
        });
    }

    private void signInCode() {
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setProfile()
                .setAuthorizationCode()
                .createParams();
        mAuthManager = AccountAuthManager.getService(LoginActivity.this, mAuthParam,' ');
        startActivityForResult(mAuthManager.getSignInIntent(), 8888);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 8888) {
                Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
                if (authAccountTask.isSuccessful()) {
                    // The sign-in is successful, and the user's ID information and authorization code are obtained.
                    AuthAccount authAccount = authAccountTask.getResult();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("displayName", authAccount.getDisplayName());
                    intent.putExtra("email", authAccount.getEmail());
                    intent.putExtra("photoUrl", authAccount.getAvatarUriString());
                    startActivity(intent);
                    Log.i(TAG, "serverAuthCode:" + authAccount.getAuthorizationCode());
                } else {
                    // The sign-in failed.
                    Log.e(TAG, "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
                }
            }
        }
}