package ru.mipt.dgap.killer;

/**
 * Created by anton on 15.02.15.
 */
public interface VKLibListener {
    public void onLogin(boolean success);
    public void onGetUserByID(MyVKUser user);
    public void onGetMe(MyVKUser user);
}
