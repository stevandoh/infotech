package com.iinfotechsystemsonline.app.utilities.networkUtils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomInterceptor implements Interceptor {
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
//        val originalRequest = chain.request()
//        val token = getToken()
        String token = getToken();
        Log.d("token",token);
        if ((POST.equals(originalRequest.method()) && !token.isEmpty())
                ||
                (PUT.equals(originalRequest.method()) && !token.isEmpty())
                ||
                (GET.equals(originalRequest.method()) && !token.isEmpty())) {
            Request secureRequest = originalRequest
                    .newBuilder()
                    .addHeader("x-access-token", token)
                    .build();

            return chain.proceed(secureRequest);

        }
        return chain.proceed(originalRequest);

    }


    private  String getToken(){
        return "";
    }

    //    private fun getToken(): String {
//        val userProfileMDL = UserProfileMDL().queryFirst()
//        var token = ""
//        if (userProfileMDL != null) {
//            token = userProfileMDL.token!!
//        }
//        return token
//    }
}
