package com.iinfotechsystemsonline.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.iinfotechsystemsonline.app.R;
import com.iinfotechsystemsonline.app.adapters.AgeGroupAdapter;
import com.iinfotechsystemsonline.app.models.AgeGroupMDL;
import com.iinfotechsystemsonline.app.models.UserMDL;
import com.iinfotechsystemsonline.app.utilities.Constants;
import com.iinfotechsystemsonline.app.utilities.GenUtils;
import com.iinfotechsystemsonline.app.utilities.VolleyRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.iinfotechsystemsonline.app.utilities.ConstantsApi.AGEGROUP_URL;

public class UpdateAgeGroupActivity extends AppCompatActivity {
    private Realm realm;
    private VolleyRequests volleyRequests;
    private UserMDL userMDL;
    private ProgressDialog progressDialog;
    SharedPreferences prefs;
    private RecyclerView rv;
    private AgeGroupAdapter ageGroupAdapter;
    RealmResults<AgeGroupMDL> results;
    private TextInputLayout tvName;
    private TextInputLayout tvMaxAge;
    private TextInputLayout tvMinAge;
    private Button btnSubmit;
    private boolean nameError, maxError,minError;
    private Bundle bundle;
    private AgeGroupMDL ageGroupMDL;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_age_group);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        realm = Realm.getDefaultInstance();
        volleyRequests = new VolleyRequests(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        rv = findViewById(R.id.rv);
        prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        id = getIntent().getIntExtra("id",0);
        ageGroupMDL = realm.where(AgeGroupMDL.class).equalTo("id", id).findFirst();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tvName = findViewById(R.id.tvName);
        tvMaxAge = findViewById(R.id.tvMaxAge);
        tvMinAge = findViewById(R.id.tvMinAge);
        btnSubmit = findViewById(R.id.btnSubmit);
        volleyRequests = new VolleyRequests(this);
        progressDialog = new ProgressDialog(this);
        if(ageGroupMDL != null){
            tvName.getEditText().setText(ageGroupMDL.getName());
            tvMaxAge.getEditText().setText(ageGroupMDL.getMaxage()+"");
            tvMinAge.getEditText().setText(ageGroupMDL.getMinage()+"");
        }



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                nameError = GenUtils.isEmpty(tvName.getEditText(), tvName, "Enter a name");
                maxError = GenUtils.isEmpty(tvMaxAge.getEditText(), tvMaxAge, "Enter max age");
                minError = GenUtils.isEmpty(tvMinAge.getEditText(), tvMaxAge, "Enter min age");
                if (!(nameError && maxError && minError)){
                    Toast.makeText(UpdateAgeGroupActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
//
//                    "isactive": true,
//                            "maxage": 0,
//                            "minage": 0,
//                            "name": "string"

                }else{
                    Map<String, String> params = new HashMap<>();
                    params.put("name", tvName.getEditText().getText().toString().trim());
                    params.put("maxage", tvMaxAge.getEditText().getText().toString());
                    params.put("minage", tvMinAge.getEditText().getText().toString());
                    params.put("isactive", true +"");
                    volleyRequests.putSecureData(AGEGROUP_URL +"/"+id, params, new VolleyRequests.VolleyPostCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) throws JSONException {
                            progressDialog.dismiss();
                            Log.d("result",result.toString());
                            String success = result.getString("status");
                            if (success.equalsIgnoreCase("success")) {
                                new MaterialDialog.Builder(UpdateAgeGroupActivity.this)
                                        .title("Success")
                                        .content("age group updated successfully")
                                        .positiveText("OK")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                startActivity(new Intent(UpdateAgeGroupActivity.this,MenuActivity.class));

                                            }
                                        }).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            progressDialog.dismiss();
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
//                    msg(message);
                            } else {
                                message = "No Internet Connection!";
//                                msg(message);
                            }

                        }

                        @Override
                        public void onStart() {
                            progressDialog.show();

                        }

                        @Override
                        public void onFinish() {
                            progressDialog.dismiss();


                        }
                    });
                }
            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
