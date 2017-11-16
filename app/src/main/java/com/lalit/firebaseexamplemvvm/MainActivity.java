package com.lalit.firebaseexamplemvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.lalit.firebaseexamplemvvm.adapter.UserDataRecyclerAdapter;
import com.lalit.firebaseexamplemvvm.data.User;
import com.lalit.firebaseexamplemvvm.viewmodel.UserListViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, UserDataRecyclerAdapter.OnItemClickListener {
    private UserListViewModel viewModel;
    private Button btnUpdateUser, btnAddUser, btnDeleteUser;
    private RecyclerView userListView;
    private EditText etUserName, etUserEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddUser = (Button) findViewById(R.id.btn_add_user);
        btnUpdateUser = (Button) findViewById(R.id.btn_update_user);
        btnDeleteUser = (Button) findViewById(R.id.btn_delete_user);
        etUserEmail = (EditText) findViewById(R.id.tiet_user_email);
        etUserName = (EditText) findViewById(R.id.tiet_user_name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userListView = (RecyclerView) findViewById(R.id.recycler_view);
        btnAddUser.setOnClickListener(this);
        btnUpdateUser.setOnClickListener(this);
        btnDeleteUser.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        if (viewModel.getUserList().getValue() != null) {
            populateListView(viewModel.getUserList().getValue());
            progressBar.setVisibility(View.GONE);
        }
        userListView.setLayoutManager(new LinearLayoutManager(this));
        addDataObserver();
        initEditText();
        FirebaseApp.initializeApp(this);
    }

    public void populateListView(ArrayList<User> userList){
        UserDataRecyclerAdapter adapter = new UserDataRecyclerAdapter(userList);
        adapter.setOnItemClickListener(this);
        userListView.setAdapter(adapter);
    }

    public void addDataObserver() {
        viewModel.getMediatorLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if (o instanceof ArrayList) {
                    populateListView((ArrayList<User>)o);
                }
                if (o != null && o instanceof Boolean) {
                    if (!((Boolean) o)) {
                        Toast.makeText(MainActivity.this, R.string.err_msg, Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_user:
                progressBar.setVisibility(View.INVISIBLE);
                if (!etUserName.getText().toString().isEmpty() && !etUserEmail.getText().toString().isEmpty()) {
                    User user = new User(etUserName.getText().toString(), etUserEmail.getText().toString());
                    viewModel.addUser(user);
                }
                break;
            case R.id.btn_update_user:
                progressBar.setVisibility(View.VISIBLE);
                viewModel.updateUser(etUserName.getText().toString(), etUserEmail.getText().toString());
                break;
            case R.id.btn_delete_user:
                progressBar.setVisibility(View.VISIBLE);
                viewModel.deleteUser(viewModel.getSelectedUser());
                etUserName.setText("");
                etUserEmail.setText("");
                break;
        }
    }

    @Override
    public void onItemClicked(User user) {
        viewModel.setSelectedUser(user);
        initEditText();
    }

    public void initEditText() {
        User selectedUser = viewModel.getSelectedUser();
        if (selectedUser != null) {
            etUserEmail.setText(selectedUser.getEmail());
            etUserName.setText(selectedUser.getName());
        }
    }
}
