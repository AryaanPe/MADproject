package com.example.electronicbazarmad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class useradapter extends RecyclerView.Adapter<useradapter.ViewHolder> {

    private List<users> userList;
    private Context context;

    public useradapter(List<users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(users user);
    }

    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.usersscroll, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        users user = userList.get(position);
        holder.UID.setText("UID: " + user.getUid());
        holder.Uname.setText("Uname: " + user.getUfname() + " " + user.getUlname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView UID;
        public TextView Uname;

        public ViewHolder(View itemView) {
            super(itemView);
            UID = itemView.findViewById(R.id.UID);
            Uname = itemView.findViewById(R.id.Uname);
        }
    }
}
