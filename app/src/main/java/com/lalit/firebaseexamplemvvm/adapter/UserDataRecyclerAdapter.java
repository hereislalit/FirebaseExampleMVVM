package com.lalit.firebaseexamplemvvm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalit.firebaseexamplemvvm.R;
import com.lalit.firebaseexamplemvvm.data.User;

import java.util.ArrayList;

public class UserDataRecyclerAdapter extends RecyclerView.Adapter<UserDataRecyclerAdapter.ViewHolder> {
    private ArrayList<User> userList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClicked(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public UserDataRecyclerAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data_layout, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.setUser(user);
    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        } else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvUserEmail;
        User user;
        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = (TextView) itemView.findViewById(R.id.tv_user_email);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                  if(user!=null && listener!=null){
                      listener.onItemClicked(user);
                  }
                }
            });
        }

        public void setUser(User user){
            this.user=user;
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
        }
    }
}
