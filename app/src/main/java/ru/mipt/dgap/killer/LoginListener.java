package ru.mipt.dgap.killer;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

/**
 * Created by anton on 15.02.15.
 */
public class LoginListener extends VKSdkListener {
    private Activity c;
    private VKLibListener vkll;
    private static final String[] sMyScope = new String[] {
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.WALL
    };


    public LoginListener(Activity c, VKLibListener vkll){
        this.c = c;
        this.vkll = vkll;
        VKSdk.initialize(this, "4782220");
        VKUIHelper.onCreate(c);
        VKSdk.authorize(sMyScope, true, false);
    }

    @Override
    public void onReceiveNewToken(VKAccessToken newToken) {
        Log.d("login","succ");
        vkll.onLogin(true);
    }

    @Override
    public void onAcceptUserToken(VKAccessToken token) {
        Log.d("login","succ2");
        vkll.onLogin(true);
    }

    @Override
    public void onCaptchaError(VKError captchaError) {
        new VKCaptchaDialog(captchaError).show();
    }

    @Override
    public void onTokenExpired(VKAccessToken expiredToken) {
        VKSdk.authorize(sMyScope);
    }

    @Override
    public void onAccessDenied(VKError authorizationError) {
        new AlertDialog.Builder(VKUIHelper.getTopActivity())
                .setMessage(authorizationError.toString())
                .show();
        vkll.onLogin(false);
    }
}
