package com.example.electronicbazarmad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class productadapter extends RecyclerView.Adapter<productadapter.ViewHolder> {
    private List<products> productsList;
    private Context context;

    public productadapter(List<products> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(products product);
    }

    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.productscroll, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        products product = productsList.get(position);
        holder.PID.setText(product.getPID());
        holder.PName.setText(product.getPName());
        holder.PCost.setText(product.getPCost());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView PID;
        public TextView PName;
        public TextView PCost;

        public ViewHolder(View itemView) {
            super(itemView);
            PID = itemView.findViewById(R.id.PID);
            PName = itemView.findViewById(R.id.PName);
            PCost = itemView.findViewById(R.id.PCost);
        }
    }
}
