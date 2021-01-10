package com.emugamestudios.groupup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.Holder>{
    Context context;
    List<ModelUser> userList;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String userPhoto = userList.get(position).getPhoto();
        String userName = userList.get(position).getName();
        String userDepartment = userList.get(position).getDepartment();

        holder.text_users_name.setText(userName);
        holder.text_users_department.setText(userDepartment);
        Picasso.get().load(userPhoto).placeholder(R.drawable.ic_default_avatar).into(holder.image_users_avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        CircleImageView image_users_avatar;
        TextView text_users_name, text_users_department;

        public Holder(@NonNull View itemView) {
            super(itemView);

            image_users_avatar = itemView.findViewById(R.id.image_users_avatar);
            text_users_name = itemView.findViewById(R.id.text_users_name);
            text_users_department = itemView.findViewById(R.id.text_users_department);
        }
    }
}
