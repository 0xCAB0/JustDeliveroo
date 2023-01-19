package com.alvaro.justdeliveroo.ui.Adaptadores;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.viewmodel.CarritoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the recyclerview showing foods saved to cart.
 */
public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.RecyclerViewHolders> {

    private List<ItemCarrito> cartList;
    private final CarritoViewModel carritoViewModel;

    //Parameterized constructor taking the cart item list.
    public CartListAdapter(ArrayList<ItemCarrito> itemCarritos, CarritoViewModel carritoViewModel) {
        this.cartList = itemCarritos;
        this.carritoViewModel = carritoViewModel;
    }

    //Set data method to provide the cart item list.
    public void setData(List<ItemCarrito> data) {
        this.cartList = data;
    }

    //ViewHolder class.
    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tName;
        private final TextView tPrice;
        private final TextView tTotalPrice;
        private final TextView tQuantity;

        RecyclerViewHolders(View itemView) {
            super(itemView);

            AppCompatImageView iDelete = itemView.findViewById(R.id.i_delete);
            tName = itemView.findViewById(R.id.t_name);
            tPrice = itemView.findViewById(R.id.t_price);
            tTotalPrice = itemView.findViewById(R.id.t_total_price);
            tQuantity = itemView.findViewById(R.id.t_quantity);
            iDelete.setOnClickListener(this);
        }

        //Deleting a cart item.
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.i_delete){
                carritoViewModel.removeItem(cartList.get(getAdapterPosition()).getName());
            }
        }
    }

    //Create/recycle a view to be added to the recycler view.
    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_item, null);
        return new RecyclerViewHolders(layoutView);
    }

    //Populating fields of the views.
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolders holder, int position) {
        ItemCarrito itemCarrito = cartList.get(holder.getAdapterPosition());
        holder.tName.setText(itemCarrito.getName());
        holder.tPrice.setText(itemCarrito.getPrice()+" €");
        holder.tQuantity.setText(String.valueOf(itemCarrito.getQuantity()));
        holder.tTotalPrice.setText(itemCarrito.getQuantity() * itemCarrito.getPrice()+ " €");
    }


    @Override
    public int getItemCount() {
        return this.cartList.size();
    }


    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
