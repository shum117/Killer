package ru.mipt.dgap.killer;

/**
 * Created by anton on 15.02.15.
 */
public class MyVKUser {
    private String id;
    private String first_name;
    private String last_name;
    private String photo;

    public MyVKUser(String id, String first_name, String last_name, String photo){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo = photo;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getId() {
        return id;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhoto() {
        return photo;
    }
}
