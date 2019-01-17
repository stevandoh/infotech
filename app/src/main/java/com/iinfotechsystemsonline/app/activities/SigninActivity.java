package com.iinfotechsystemsonline.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.iinfotechsystemsonline.app.R;
import com.iinfotechsystemsonline.app.models.UserMDL;
import com.iinfotechsystemsonline.app.utilities.Constants;
import com.iinfotechsystemsonline.app.utilities.GenUtils;
import com.iinfotechsystemsonline.app.utilities.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

import static com.iinfotechsystemsonline.app.utilities.Constants.PREFS;
import static com.iinfotechsystemsonline.app.utilities.Constants.prefBooleanDefault;
import static com.iinfotechsystemsonline.app.utilities.Constants.prefsLogin;
import static com.iinfotechsystemsonline.app.utilities.ConstantsApi.LOGIN_URL;


public class SigninActivity extends AppCompatActivity {

//    @BindView(R.id.toolbar)
    Toolbar toolbar;
//    @BindView(R.id.tiUsername)
    TextInputLayout tiUsername;
//    @BindView(R.id.tiPwd)
    TextInputLayout tiPwd;
//    @BindView(R.id.btnLogin)

    Button btnLogin;
//    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    boolean usernameError, passwordError;
    VolleyRequests mVolleyRequest;
    Realm mRealm;
    RealmAsyncTask realmAsyncTask;
    SharedPreferences prefs;
    private UserMDL userMDL;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
//        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnLogin = findViewById(R.id.btnLogin);
        tiPwd = findViewById(R.id.tiPwd);
        tiUsername = findViewById(R.id.tiUsername);
        progressBar = findViewById(R.id.progressBar);
        mVolleyRequest = new VolleyRequests(SigninActivity.this);
        mRealm = Realm.getDefaultInstance(); prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
//        mProgressBar.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(SigninActivity.this);
        checkAlreadyLogin();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                usernameError = GenUtils.isEmpty(tiUsername.getEditText(), tiUsername, "Enter your username");
                passwordError = GenUtils.isEmpty(tiPwd.getEditText(), tiPwd, "Enter your password");
                if (!(usernameError && passwordError)) {

                    Toast.makeText(SigninActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();

                }else {

//                    Toast.makeText(LoginActivity.this, "Starting volley requests", Toast.LENGTH_SHORT).show();


                    Map<String, String> params = new HashMap<>();
                    params.put("username", tiUsername.getEditText().getText().toString().trim());
                    params.put("password", tiPwd.getEditText().getText().toString());


                    mVolleyRequest.postData(LOGIN_URL, params, new VolleyRequests.VolleyPostCallBack() {

                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onSuccess(final JSONObject result) {
                            try {


                                progressBar.setVisibility(View.GONE);
                                Log.d("Result", result.toString());

                                final String id = UUID.randomUUID().toString();
                                userMDL = new UserMDL();

                                String status = null;

                                status = result.getString("status");


                                if (status.trim().toLowerCase().equalsIgnoreCase("success")) {


                                    mRealm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            try {
//                                                JSONObject user = result.getJSONObject("user");
                                                JSONArray data= result.getJSONArray("data");
                                                String token = data.getJSONObject(0).getString("token");
                                                Log.d("token", token);
                                                userMDL.setId(id);
                                                userMDL.setToken(token);
                                                userMDL.setUsername(tiUsername.getEditText().getText().toString());


//                                            if (result.getJSONArray("submetros").length() > 0) {
//                                                JSONObject submetro = result.getJSONArray("submetros").getJSONObject(0);
//                                                userMDL.setSubMetroId(submetro.getInt("submetro_id"));
//                                                userMDL.setSubMetro(submetro.getString("submetro_name"));
//                                            }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            realm.copyToRealmOrUpdate(userMDL);
                                            msg("AWESOME");
                                            setUserlogin();

                                        }
                                    });
                                } else {
                                    msg("Invalid username / password.. Please try again");
                                }

                            }catch(Exception ex){

                            }
                        }


                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onError(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String message = null;

                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                if (error instanceof NetworkError) {
                                    message = "Cannot connect to Internet...Please check your connection!";
                                } else if (statusCode == 422) {
                                    message = "Wrong credentials...Please try again!";
                                } else if (error instanceof ServerError) {
                                    message = "The server could not be found. Please try again after some time!!";
                                } else if (error instanceof ParseError) {
                                    message = "Parsing error! Please try again after some time!!";
                                } else if (error instanceof TimeoutError) {
                                    message = "Connection TimeOut! Please check your internet connection.";
                                }
//                                else if (error instanceof NoConnectionError) {
//                                    message = "Cannot connect to Internet...Please check your connection!";
//                                }
                                msg(message);
                            } else {
                                message = "No Internet Connection!";
//                                msg(message);
                            }

                        }

                        @Override
                        public void onStart() {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFinish() {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });


                }
            }
        });
    }

    public void setUserlogin() {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(prefsLogin, true);
        editor.apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();





    }
    private void msg(String content) {

//        Log.d("Content", content);

        try {
            new MaterialDialog.Builder(SigninActivity.this)
                    .title("Sign In")
                    .content(content)
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO

                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void checkAlreadyLogin() {
        Boolean bl = prefs.getBoolean(Constants.prefsLogin, prefBooleanDefault);

        if (bl) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


//    @OnClick(R.id.btnLogin)
//    public void onViewClicked() {
//
//
//    }
}
