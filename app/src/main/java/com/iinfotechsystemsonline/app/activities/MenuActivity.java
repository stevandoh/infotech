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
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.iinfotechsystemsonline.app.R;
import com.iinfotechsystemsonline.app.adapters.AgeGroupAdapter;
import com.iinfotechsystemsonline.app.models.AgeGroupMDL;
import com.iinfotechsystemsonline.app.models.UserMDL;
import com.iinfotechsystemsonline.app.utilities.Constants;
import com.iinfotechsystemsonline.app.utilities.ConstantsApi;
import com.iinfotechsystemsonline.app.utilities.ItemClickSupport;
import com.iinfotechsystemsonline.app.utilities.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
        volleyRequests = new VolleyRequests(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        rv = findViewById(R.id.rv);
        prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

       getAgeGroup();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new MaterialDialog.Builder(MenuActivity.this)
                        .title("Add Age group")
                        .content("Do you want to add an age group")
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(new Intent(MenuActivity.this, CreateAgeGroupActivity.class));

                            }
                        }).show();
            }
        });
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
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();

            }
        });
    }
    private void setRecyclerView() {

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final List<AgeGroupMDL> ageGroupMDLS = setData();
        ageGroupAdapter = new AgeGroupAdapter(this ,realm, ageGroupMDLS);
        rv.setAdapter(ageGroupAdapter);
//        GenUtils.getToastMessage(getApplicationContext(), businessMDLs.size() + "");

        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {

                new MaterialDialog.Builder(MenuActivity.this)
                        .title("Manage Age group")
                        .items(R.array.age_settings)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which == 0) {
                                    Intent intent = new Intent(MenuActivity.this, UpdateAgeGroupActivity.class);
                                    intent.putExtra("id", ageGroupMDLS.get(position).getId());

                                    startActivity(intent);
                                }
//                                else if (which == 2) {

                               else if (which == 1) {

                                    try {
                                        new MaterialDialog.Builder(MenuActivity.this)
                                                .title("Delete age group")
                                                .content("are you sure you want to delete this age group")
                                                .positiveText("Delete")
                                                .negativeText("Cancel")
                                                .negativeColor(getResources().getColor(R.color.md_red_900))
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        realm.executeTransaction(new Realm.Transaction() {
                                                            @Override
                                                            public void execute(Realm realm) {
                                                                ageGroupMDLS.get(position).deleteFromRealm();
                                                                ageGroupAdapter.notifyItemRemoved(position);
                                                                ageGroupAdapter.notifyItemRangeChanged(position, ageGroupMDLS.size());
//                                                                volleyRequests.deleteObjSecureRequest(ConstantsApi.AGEGROUP_URL + "/" + ageGroupMDLS.get(position).getId(), new VolleyRequests.VolleyJsonCallBack() {
//                                                                    @Override
//                                                                    public void onSuccess(JSONObject result) throws JSONException {
//                                                                        progressDialog.dismiss();
//
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onError(VolleyError error) {
//                                                                        progressDialog.dismiss();
//
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onStart() {
//                                                                        progressDialog.setMessage("Please wait");
//                                                                        progressDialog.show();
//
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onFinish() {
//                                                                        progressDialog.dismiss();
//
//                                                                    }
//                                                                });


                                                            }
                                                        });
                                                        //startActivity(new Intent(MeterListActivity.this,RegisterMeterActivity.class));


                                                    }
                                                }).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                return true;
                            }
                        }).show();

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




}
