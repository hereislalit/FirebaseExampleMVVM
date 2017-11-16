package com.lalit.firebaseexamplemvvm.data;

import java.util.HashMap;

public class User {
    private String name;
    private String email;
    private String id;
    private static final String NAME_VARIABLE_VALUE = "name";
    private static final String EMAIL_VARIABLE_VALUE = "email";
    private static final String ID_VARIABLE_VALUE = "id";

    public User(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public User() {
        // Default constructor might be needed when using firebase.
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static HashMap<String, Object> getKeyMap(User user) {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put(NAME_VARIABLE_VALUE, user.getName());
        hm.put(EMAIL_VARIABLE_VALUE, user.getEmail());
        hm.put(ID_VARIABLE_VALUE, user.getId());
        return hm;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            if (id.compareTo(((User) obj).id) == 0) {
                return true;
            }
            return false;
        }
        return false;
    }
}
