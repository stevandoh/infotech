package com.iinfotechsystemsonline.app.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by SOVAVY on 5/5/2017.
 */

public class GenUtils {

    public static ArrayList<Object> arrNoGPSCaptureIds = new ArrayList<Object>();
    private static String strDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";


    public static String base64FromUri(Uri imageUri, Context context) {
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        return encodeImage(selectedImage);
    }

    public static boolean isNotEnoughDigit(EditText textView, TextInputLayout textInputLayout,
                                           String errorMessage) {

        if (textView.getText().toString().trim().length() < 10) {
            for(int i = 0; i<textView.getText().toString().trim().length(); i++){
                if(!Character.isDigit(textView.getText().toString().trim().charAt(i))){
                    return true;
                }
            }
            textInputLayout.setError(errorMessage);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }


    private static String encodeImage(Bitmap bitmapImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static <T> T[] extendArray(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String joinArray(String[] arrItems) {
        StringBuilder sb = new StringBuilder();
        for (String n : arrItems) {
            if (sb.length() > 0)
                sb.append(',');
            sb.append(n);
            // sb.append("'").append(n).append("'");
        }
        return sb.toString();
    }

    public static String[] getCursorPropAsArray(Cursor cursor, String fieldName) {
        String[] arrItems = new String[cursor.getCount()];
        int cnt = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrItems[cnt] = cursor.getString(cursor.getColumnIndex(fieldName));
            cnt++;
        }
        return arrItems;
    }

    public static JSONObject getJSONRelatedObjOrNull(JSONObject jsObj, String name) {
        JSONObject jsRelObj = null;

        if (!jsObj.isNull(name)) {
            try {
                jsRelObj = jsObj.getJSONObject(name);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        return jsRelObj;
    }

    public static String getIdFromUrlRes(String url) {
        // Get ID only from resource url. This is on useful for REST api's
        String strId = "";

        String[] parts = url.split("/");
        if (parts.length > 1) {
            strId = parts[parts.length - 1];
            Log.d("getIdFromUrlRes", "ID: " + strId);
        }

        return strId;
    }


    public static boolean gpsIsEnabled(Context ctx) {
        final LocationManager manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        assert manager != null;
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void gpsAlertDialog(Context ctx) {
        final Context context = ctx;

        new AlertDialog.Builder(ctx)
                .setMessage("We need your location for easy delivery. You must enable your GPS to continue!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        }).show();
    }

    public static void enableGPSCheck(Context ctx) {

        if (!gpsIsEnabled(ctx)) {
            gpsAlertDialog(ctx);
        }

    }

    public static String getNowString() {
        String DATE_FORMAT_NOW = strDateTimeFormat;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public static Date string2Date(String strDatetime) {
        Date dtDatetime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(strDateTimeFormat);
        if (strDatetime != null && !strDatetime.isEmpty()) {
            try {
                dtDatetime = sdf.parse(strDatetime);
            } catch (ParseException e) {
                Log.d("string2Date", e.getMessage());
            }
        }
        return dtDatetime;
    }

    public static String Date2string(Date dtDatetime) {
        DateFormat df = new SimpleDateFormat(strDateTimeFormat.replace("T", " "));
        return df.format(dtDatetime);
    }

    public static String getStringIntegerHexBlocks(int value) {
        String result = "";
        String string = Integer.toHexString(value);

        int remain = 8 - string.length();
        char[] chars = new char[remain];
        Arrays.fill(chars, '0');
        string = new String(chars) + string;

        int count = 0;
        for (int i = string.length() - 1; i >= 0; i--) {
            count++;
            result = string.substring(i, i + 1) + result;
            if (count == 4) {
                result = "-" + result;
                count = 0;
            }
        }

        if (result.startsWith("-")) {
            result = result.substring(1, result.length());
        }

        return result;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static String getAppVersion(Context ctx) {
        String strAppVersion = "";
        String strVersion = "";
        int intCode = 0;
        try {
            strVersion = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    0).versionName;
            intCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        strAppVersion = intCode + "," + strVersion;

        return strAppVersion;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String makeSlug(String input) {
        Pattern NONLATIN = Pattern.compile("[^\\w-]");
        Pattern WHITESPACE = Pattern.compile("[\\s]");

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }


    public static String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key).substring(2, obj.getString(key).length() - 2);
//            trimmedString  = obj.get;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

//    public static void createAlert(Context context, String message, String title, String buttonMsg) {
//        new AlertDialogWrapper.Builder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setNegativeButton(buttonMsg, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).show();
//    }

    public static void CheckIfNullValue(String modelField, TextView textView) {
        if (modelField.equals("null")) {
            textView.setText("");
        } else {
            textView.setText(modelField);
        }
    }

    public static double formatingCost(double data) {
        DecimalFormat df = new DecimalFormat("#.##");

        return Double.valueOf(df.format(data));
    }

    public static boolean isEmpty(EditText textView, TextInputLayout textInputLayout,
                                  String errorMessage) {

        if (textView.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(errorMessage);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }


    public static boolean isAmountValid(EasyMoneyEditText textView, TextInputLayout textInputLayout,
                                        String errorMessage, float amount) {

        if (Float.parseFloat(textView.getValueString()) < amount) {
            textInputLayout.setError(errorMessage);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isAmountGreaterThanTwoThousand(EasyMoneyEditText textView,
                                                         TextInputLayout textInputLayout,
                                                         String errorMessage, float amount) {

        if (Float.parseFloat(textView.getValueString()) > amount) {
            textInputLayout.setError(errorMessage);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }


    public static void getToastMessage(Context context, String messsage) {
        try {
            Toast.makeText(context, messsage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("Toast Message Error", e.getMessage());
        }
    }


    public static ArrayAdapter<CharSequence> getCharSequenceArrayAdapter(Activity activity,
                                                                         int array, int layout) {
        return ArrayAdapter.createFromResource(activity, array, layout);
    }

    public static boolean checkNetwork(Context context) {

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(CONNECTIVITY_SERVICE);

//For 3G check
        assert manager != null;
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
//For WiFi Check
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();

        return !is3g && !isWifi;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDeviceID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = "";

        try {
            deviceId = telephonyManager.getImei();
        } catch (NoSuchMethodError ee) {
            Log.e("DeviceID", "imei not available");
        } catch (SecurityException e) {
            Log.e("DeviceID", "imei not accessible. perms error");
        }

        if (deviceId == null || deviceId.isEmpty()) {
            try {
                deviceId = telephonyManager.getDeviceId();
            } catch (SecurityException e) {
                deviceId = "";
                Log.e("DeviceID", "deviceid not available");
            }
        }

        if (deviceId == null)
            deviceId = "";

        Log.d("DeviceID", deviceId);
        return deviceId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static JSONObject getDeviceDetails(Context context) throws JSONException {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", Build.DEVICE);
        jsonObject.put("source", "mobile-android");
        jsonObject.put("ip_address", "0.0.0.0"); // Get real ip
        jsonObject.put("version", Build.VERSION.SDK_INT);
        jsonObject.put("model", Build.MODEL);
        jsonObject.put("imei", getDeviceID(context));
        jsonObject.put("mac_address", wifiManager.getConnectionInfo().getMacAddress());
//        jsonObject.put("app_version", getVersionCode());
        return jsonObject;
    }

    public static String createZipnetUserMail(String username) {
        return username + "@example.com";
    }

    public static String md5(String in) throws NoSuchAlgorithmException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (byte anA : a) {
                sb.append(Character.forDigit((anA & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(anA & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Spanned get_html_text(String body) {
        Spanned txt = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txt = Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY);
        } else {
            txt = Html.fromHtml(body);
        }

        return txt;
    }

    @NonNull
    public static String getSplitDate(String dateString) {

        int dotPosition = dateString.indexOf(".");

        return dateString.substring(0, dotPosition);
    }

    @NonNull
    public static String getSplitTime(String dateString) {
        String time = dateString.split("T")[1];

        int position = time.indexOf(":", time.indexOf(":") + 1);
        return time.substring(0, position);
    }

    public static String createDate(String date) {
        String splitDate = date.split("T")[0];
        String year = splitDate.split("-")[0];
        String month = splitDate.split("-")[1];
        String day = splitDate.split("-")[2];

        return String.format("%s/%s/%s", day, month, year);
//    return day +"/"+month+"/"+year;
    }

    public static String fullDate(String date, String time) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String result = "";
        try {
            Date d1 = df.parse(date);
            result = String.format("%1$s %2$tB %2$td, %2$tY", "", d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.format("%s %s", result, time);
    }

//    public static int getVersionCode() {
//        return BuildConfig.VERSION_CODE;
//    }
//
//    public static String getVersionName() {
//        return BuildConfig.VERSION_NAME;
//    }

    public static void openGooglePlay(Context context, String appId) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            context.startActivity(webIntent);
        }
    }

    public static void openBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static boolean isPhoneValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 10;
    }

    //    +233201234567
    public static String createInternationalPhoneNumber(String phoneNumber) {
        return String.format("+233%s", phoneNumber.substring(1, phoneNumber.length()));
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            assert ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)) != null;
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

