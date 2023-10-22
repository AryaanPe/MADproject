package com.example.electronicbazarmad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<carts> cartItemList;
    private Context context;

    public CartAdapter(List<carts> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(carts cartItem);
        void onRemoveButtonClick(carts cartItem);
    }

    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cartscroll, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        carts cartItem = cartItemList.get(position);
        holder.CartName.setText(cartItem.getCartName());
        holder.CartQuantity.setText(cartItem.getQuantity());
        Glide.with(context).load(cartItem.getImageURL()).into(holder.imgview);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(cartItem);
                }
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onRemoveButtonClick(cartItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView CartName;
        public TextView CartQuantity;
        public Button removeButton;
        public ImageView imgview;

        public ViewHolder(View itemView) {
            super(itemView);
            CartName = itemView.findViewById(R.id.CartName);
            CartQuantity = itemView.findViewById(R.id.CartQuantity);
            removeButton = itemView.findViewById(R.id.removebutton);
            imgview = itemView.findViewById(R.id.CartImage);
        }
    }
}
