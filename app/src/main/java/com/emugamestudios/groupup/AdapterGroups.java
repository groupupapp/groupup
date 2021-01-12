package com.emugamestudios.groupup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterGroups extends RecyclerView.Adapter<AdapterGroups.MyHolder> {

    Context context;
    List<ModelGroup> groupList;

    public AdapterGroups(Context context, List<ModelGroup> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_groups, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String uid = groupList.get(position).getUid();
        String groupdId = groupList.get(position).getGroupdId();
        String groupTitle = groupList.get(position).getGroupTitle();
        String groupDescription = groupList.get(position).getGroupDescription();
        String groupPhoto = groupList.get(position).getGroupPhoto();
        String name = groupList.get(position).getName();
        String email = groupList.get(position).getEmail();
        String uni = groupList.get(position).getUni();
        String department = groupList.get(position).getDepartment();
        String photo = groupList.get(position).getPhoto();

        Picasso.get().load(groupPhoto).into(holder.search_group_img);
        Picasso.get().load(photo).into(holder.search_pic);

        //set data
        holder.search_group_name.setText(groupTitle);
        holder.search_group_desc.setText(groupDescription);
        holder.search_name.setText(name);
        holder.search_department.setText(department);
        holder.search_uni.setText(uni);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView search_group_img, search_pic;
        TextView search_group_name,search_group_desc,search_name,search_department,search_uni;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            search_name = itemView.findViewById(R.id.search_name);
            search_uni = itemView.findViewById(R.id.search_uni);
            search_pic = itemView.findViewById(R.id.search_pic);
            search_department = itemView.findViewById(R.id.search_department);
            search_group_img = itemView.findViewById(R.id.search_group_img);
            search_group_name = itemView.findViewById(R.id.search_group_name);
            search_group_desc = itemView.findViewById(R.id.search_group_desc);
        }
    }
}
