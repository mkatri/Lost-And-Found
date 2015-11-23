package edu.gatech.cc.lostandfound.mobile.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.gatech.cc.lostandfound.mobile.R;
import permissions.dispatcher.DeniedPermission;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import permissions.dispatcher.ShowsRationale;

@RuntimePermissions
public class LoginActivity extends AppCompatActivity {
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_AUTHORIZATION = 2000;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String SCOPE = "oauth2:https://www.googleapis" +
            ".com/auth/userinfo.profile";
    @InjectView(R.id.btn_login)
    Button _loginButton;
    SharedPreferences settings;

    String selectedAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        settings = getSharedPreferences("LostAndFound", 0);
        selectedAccount = settings.getString(Constants.PREF_ACCOUNT_NAME, null);
        if (selectedAccount == null) {
            String[] accountTypes = new String[]{"com.google"};
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    accountTypes, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
        } else {
            LoginActivityPermissionsDispatcher.loginWithCheck(this);
        }

    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void login() {
        Log.d(TAG, "Login");

        if (selectedAccount == null) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog
                (LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>
                () {
            @Override
            protected Boolean doInBackground(Void... params) {
                String token = fetchToken();
                if (token == null) {
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }

            /**
             * Gets an authentication token from Google and handles any
             * GoogleAuthException that may occur.
             */
            protected String fetchToken() {
                try {
                    return GoogleAuthUtil.getToken(LoginActivity.this,
                            selectedAccount, SCOPE);
                } catch (UserRecoverableAuthException
                        userRecoverableException) {
                    // GooglePlayServices.apk is either old, disabled, or not
                    // present
                    // so we need to show the user some UI in the activity to
                    // recover.
                    startActivityForResult(userRecoverableException.getIntent
                            (), REQUEST_AUTHORIZATION);

                    //LoginActivity.this.handleException
                    // (userRecoverableException);
                    //Log.e("error", userRecoverableException.toString());
                } catch (GoogleAuthException fatalException) {
                    // Some other type of unrecoverable exception has occurred.
                    // Report and log the error as appropriate for your app.
                    Log.e("error", fatalException.toString());
                } catch (IOException e) {
                    Log.e("error", e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result == null) {
                    progressDialog.dismiss();
                    onLoginFailed();

                } else {
                    progressDialog.dismiss();
                    onLoginSuccess();
                }
            }
        };
        //if (isDeviceOnline()) {
        task.execute();
        //} else {
        //    Toast.makeText(this, R.string.not_online, Toast.LENGTH_LONG)
        // .show();
        //}
    }

    void handleException(Exception e) {

    }

    @ShowsRationale(Manifest.permission.GET_ACCOUNTS)
    void showRationaleForAccounts() {
        Toast.makeText(this, R.string.permission_accounts_rationale, Toast
                .LENGTH_SHORT).show();
    }

    @DeniedPermission(Manifest.permission.GET_ACCOUNTS)
    void showDeniedForAccounts() {
        Toast.makeText(this, R.string.permission_accounts_denied, Toast
                .LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                setSelectedAccountName(data.getStringExtra(AccountManager
                        .KEY_ACCOUNT_NAME));
                // With the account name acquired, go get the auth token
                LoginActivityPermissionsDispatcher.loginWithCheck(this);
            } else if (resultCode == RESULT_CANCELED) {
                // The account picker dialog closed without selecting an
                // account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, R.string.pick_account, Toast
                        .LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
        // NOTE: delegate the permission handling to generated method
        LoginActivityPermissionsDispatcher.
                onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        selectedAccount = accountName;
    }
}
