package com.shoppinglist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppinglist.AddNewProduct;
import com.shoppinglist.MainActivity;
import com.shoppinglist.Model.ShoppingListModel;
import com.shoppinglist.R;
import com.shoppinglist.Utils.DataBaseHelper;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder>
{
    private List<ShoppingListModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ShoppingListAdapter(DataBaseHelper myDB, MainActivity activity)
    {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoplist_layout , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ShoppingListModel item = mList.get(position);
        holder.mCheckBox.setText(item.getProduct());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    myDB.updateStatus(item.getId() , 1);
                }
                else
                {
                    myDB.updateStatus(item.getId() , 0);
                }
            }
        });
    }

    public boolean toBoolean(int num)
    {
        return num!=0;
    }

    public Context getContext()
    {
        return activity;
    }

    public void setProducts(List<ShoppingListModel> mList)
    {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteProduct(int position)
    {
        ShoppingListModel item = mList.get(position);
        myDB.deleteProduct(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position)
    {
        ShoppingListModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("product" , item.getProduct());

        AddNewProduct product = new AddNewProduct();

        product.setArguments(bundle);
        product.show(activity.getSupportFragmentManager() , product.getTag());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}

