package com.example.myproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<User> list;
    private OnItemClickListener itemClickListener;

    public MyAdapter(Context context, List<User> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = listener;
    }
    public MyAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.fullName.setText(user.getFullName());
        holder.overall.setText(user.getOverall());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView fullName,overall;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            overall = itemView.findViewById(R.id.overall);

        }
    }
    public void setUsers(List<User> userList) {
        list.clear(); // Clear the existing list
        list.addAll(userList); // Add the new list of users
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
