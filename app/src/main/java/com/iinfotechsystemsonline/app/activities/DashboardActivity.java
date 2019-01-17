package com.iinfotechsystemsonline.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.util.Log;
import android.view.View;

import com.iinfotechsystemsonline.app.R;
import com.iinfotechsystemsonline.app.adapters.AgeGroupAdapter;
import com.iinfotechsystemsonline.app.models.AgeGroupMDL;
import com.iinfotechsystemsonline.app.models.UserMDL;
import com.iinfotechsystemsonline.app.utilities.ConstantApi;
import com.iinfotechsystemsonline.app.utilities.Constants;
import com.iinfotechsystemsonline.app.utilities.ConstantsApi;
import com.iinfotechsystemsonline.app.utilities.GenUtils;
import com.iinfotechsystemsonline.app.utilities.ItemClickSupport;
import com.iinfotechsystemsonline.app.utilities.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    private Realm realm;
    private VolleyRequests volleyRequests;
    private UserMDL userMDL;
    private ProgressDialog progressDialog;
    SharedPreferences prefs;
    private RecyclerView rv;
    private AgeGroupAdapter ageGroupAdapter;
    RealmResults<AgeGroupMDL> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
        volleyRequests = new VolleyRequests(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        rv = findViewById(R.id.rv);
        prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

//        getAgeGroup();
        Log.d("size",setData().size()+"");

        //setRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fab);
        startActivity(new Intent(this, DashboardActivity.class));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new MaterialDialog.Builder(DashboardActivity.this)
//                        .title("Add Age group")
//                        .content("Do you want to add an age group")
//                        .positiveText("OK")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                startActivity(new Intent(DashboardActivity.this, CreateAgeGroupActivity.class));
//
//                            }
//                        }).show();
//
            }
        });

    }

    private void setRecyclerView() {

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<AgeGroupMDL> ageGroupMDLS = setData();
        ageGroupAdapter = new AgeGroupAdapter(this ,realm, ageGroupMDLS);
        rv.setAdapter(ageGroupAdapter);
//        GenUtils.getToastMessage(getApplicationContext(), businessMDLs.size() + "");

        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                GenUtils.getToastMessage(getApplicationContext(), String.valueOf(position));

//                setBusinessIntent(position, v);
            }
        });
    }

//    private void setBusinessIntent(int position, View v) {
//        Intent intent = new Intent(v.getContext(), PaymentByActivity.class);
//        String busId = results.get(position).getUuid();
//        intent.putExtra("id", busId);
//        intent.putExtra("payment_by","business");
//        startActivity(intent);
//    }

    private List<AgeGroupMDL> setData() {
        results = realm.where(AgeGroupMDL.class).findAll();
        return results;
    }

    private void getAgeGroup() {
//        RequestQueue queue = Volley.newRequestQueue(this);
        volleyRequests.JsonObjSecureRequest(ConstantsApi.AGEGROUP_URL, new VolleyRequests.VolleyJsonCallBack() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                progressDialog.dismiss();
                Log.d("result",result.toString());
//                GenUtils.getToastMessage(getApplicationContext(),result.toString());
                String status = result.getString("status");
                Log.d("status",status);
//                if (status.equalsIgnoreCase("status")){

                    JSONArray ageGroups = result.getJSONObject("data").getJSONArray("content");
                    Log.d("array : ", result.getJSONObject("data").toString());
                    for (int i = 0; i < ageGroups.length(); i++) {
                        final JSONObject agegroup = (JSONObject) ageGroups.get(i);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                AgeGroupMDL ageGroupMDL= new AgeGroupMDL();
                                try {
                                    ageGroupMDL.setId(agegroup.getInt("id"));
                                    ageGroupMDL.setMinage(agegroup.getInt("minage"));
                                    ageGroupMDL.setMaxage(agegroup.getInt("maxage"));
                                    ageGroupMDL.setName(agegroup.getString("name"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                realm.copyToRealmOrUpdate(ageGroupMDL);

                            }
                        });
                        setRecyclerView();
                    }
//                }


            }

            @Override
            public void onError(VolleyError error) {
//                progressBar.setVisibility(View.INVISIBLE);
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
