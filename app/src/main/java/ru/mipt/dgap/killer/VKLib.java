package ru.mipt.dgap.killer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anton on 15.02.15.
 */
public class VKLib{
    private VKLibListener vkll;
    private static LoginListener logl = null;

    public VKLib(VKLibListener vkll){
        this.vkll = vkll;
    }

    public void login(Activity c){
        logl = new LoginListener(c, vkll);
    }

    public void getUserByID(String id){
        VKRequest rqst = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, id, VKApiConst.FIELDS, "id,first_name,last_name,photo_max_orig"));
        rqst.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response)
            {
                MyVKUser u = null;
                try {
                    JSONObject o = response.json.getJSONArray("response").getJSONObject(0);
                    u = new MyVKUser(o.getString("id"),o.getString("first_name"),o.getString("last_name"),o.getString("photo_max_orig"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                vkll.onGetUserByID(u);
            }

            @Override
            public void onError(VKError error)
            {
                Log.e("executeListener", error.toString());
                vkll.onGetUserByID(null);
            }
        });
    }

    public void getMe(){
        VKRequest rqst = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,photo_max_orig"));
        rqst.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response)
            {
                MyVKUser u = null;
                try {
                    JSONObject o = response.json.getJSONArray("response").getJSONObject(0);
                    u = new MyVKUser(o.getString("id"),o.getString("first_name"),o.getString("last_name"),o.getString("photo_max_orig"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                vkll.onGetMe(u);
            }

            @Override
            public void onError(VKError error)
            {
                Log.e("executeListener", error.toString());
                vkll.onGetMe(null);
            }
        });
    }

    public void postToGroup(String group_id, String msg){
        VKRequest rqst = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, "-" + group_id, VKApiConst.FROM_GROUP, "1", VKApiConst.MESSAGE, msg));
        rqst.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onError(VKError error)
            {
                Log.e("executeListener", error.toString());
            }
        });
    }

    public void postToGroupWithPhoto(String msg, Bitmap bmp, String group_id){
        VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(bmp, VKImageParameters.jpgImage(0.9f)), 0, Integer.parseInt(group_id));
        final String grid = group_id;
        final String m = msg;
        request.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response)
            {
                Log.d("photo",response.responseString);
                try{
                    JSONObject o = response.json.getJSONArray("response").getJSONObject(0);
                    String att = "photo" + o.getString("owner_id") + "_" + o.getString("id");
                    VKRequest rqst = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, "-" + grid, VKApiConst.FROM_GROUP, "1", VKApiConst.MESSAGE, m, VKApiConst.ATTACHMENTS, att));
                    rqst.executeWithListener(new VKRequest.VKRequestListener()
                    {
                        @Override
                        public void onError(VKError error)
                        {
                            Log.e("executeListener", error.toString());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*MyVKUser u = null;
                try {
                    JSONObject o = response.json.getJSONArray("response").getJSONObject(0);
                    u = new MyVKUser(o.getString("id"),o.getString("first_name"),o.getString("last_name"),o.getString("photo_max_orig"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                vkll.onGetMe(u);*/
            }

            @Override
            public void onError(VKError error)
            {
                Log.e("executeListener", error.toString());
                //vkll.onGetMe(null);
            }
        });
    }
}



