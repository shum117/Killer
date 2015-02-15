package ru.mipt.dgap.killer;

import android.app.Activity;

public class Core implements VKLibListener{
    Player killer, victim;
    Activity activity;
    private VKLib vkl;
    public Core (Activity act){
        killer = new Player();
        activity = act;
        vkl = new VKLib(this);
    }

    @Override
    public void onLogin(boolean success) {
        if(success){
            vkl.getMe();
        }
    }

    @Override
    public void onGetUserByID(MyVKUser user) {

    }

    @Override
    public void onGetMe(MyVKUser user) {
        killer.setAvatar(user.getPhoto());
        killer.setAgentName("007");
        killer.setName(user.getFirst_name()+" "+user.getLast_name());
        killer.setId(user.getId());

        String[] result = new String[3];
        result[0] = killer.getAvatar();
        result[1] = killer.getAgentName();
        result[2] = killer.getName();

        ((MainActivity)activity).refreshKiller(result);
    }

    public class Player{
        private String userId;
        private String userAvatar;
        private String userName;
        private String agentName;
        private int victimHash;
        public String getName(){
            return userName;
        }
        public String getAgentName(){
            return agentName;
        }
        public String getAvatar(){
            return userAvatar;
        }

        public String getId(){
            return userId;
        }
        public void setName(String name){
            userName = name;
        }
        public void setAgentName(String name){
            agentName = name;
        }
        public void setAvatar(String avatar){
            userAvatar = avatar;
        }
        public void setHash(int hash){
            victimHash = hash;
        }
        public int getHash(){
            return victimHash;
        }
        public void setId(String id){
            userId = id;
        }
    }
    public void logIn() {
        vkl.login(activity);
    }
    public boolean kill(String name){
        int h = name.hashCode();
        return (h < victim.getHash());
    }
    public void getVictim(){
        //here we are getting user data from VK
        victim = new Player();

        victim.setAvatar("https://pp.vk.me/c405131/v405131405/31c8/hCUgS6M2cZI.jpg");
        victim.setName("Famenka");
        victim.setId("https://vk.com/vl_fom");
        String[] result = new String[3];
        result[0] = victim.getAvatar();
        result[1] = victim.getName();
        result[2] = victim.getId();
        ((MainActivity)activity).refreshVictim(result);
    }

}
