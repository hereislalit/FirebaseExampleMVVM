package com.lalit.firebaseexamplemvvm.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lalit.firebaseexamplemvvm.data.User;

import java.util.ArrayList;

public class UserListViewModel extends ViewModel implements ValueEventListener {
    private User selectedUser;
    private MutableLiveData<ArrayList<User>> userList;
    private MutableLiveData<Boolean> status;
    private MediatorLiveData mediatorLiveData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;

    public UserListViewModel() {
        String path = "app_user/" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReference = firebaseDatabase.getReference(path + "/user");
        dbReference.addValueEventListener(this);
        userList = new MutableLiveData<ArrayList<User>>();
        status=new MutableLiveData<Boolean>();
        mediatorLiveData=new MediatorLiveData();
        mediatorLiveData.addSource(userList, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(@Nullable ArrayList<User> users) {
                mediatorLiveData.setValue(users);
            }
        });
        mediatorLiveData.addSource(status, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                mediatorLiveData.setValue(b);
            }
        });
    }

    public MediatorLiveData getMediatorLiveData(){
        return mediatorLiveData;
    }

    public void addUser(User user) {
        String id = dbReference.push().getKey();
        user.setId(id);
        dbReference.child(id).setValue(user);
    }

    public void updateUser(String name, String email) {
        if (selectedUser != null) {
            if (name != null) {
                selectedUser.setName(name);
            }
            if (email != null) {
                selectedUser.setEmail(email);
            }
            dbReference.child(selectedUser.getId()).updateChildren(User.getKeyMap(selectedUser));
        }
    }

    public void deleteUser() {
        if (selectedUser != null) {
            dbReference.child(selectedUser.getId()).removeValue();
        }
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User user) {
        selectedUser = user;
    }

    public LiveData<ArrayList<User>> getUserList() {
        return userList;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<User> list = new ArrayList<>();
//        if (userList != null && userList.getValue()!=null) {
//            Log.i("MVVM", "View Model: user list is cleared");
//            userList.getValue().clear();
//        }
        for (DataSnapshot dbsnap : dataSnapshot.getChildren()) {
            User user = dbsnap.getValue(User.class);
            if (user.getId() == null) {
                user.setId(dbsnap.getKey());
            }
            list.add(user);
        }
        userList.postValue(list);
        status.setValue(true);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        status.setValue(false);
    }
}
