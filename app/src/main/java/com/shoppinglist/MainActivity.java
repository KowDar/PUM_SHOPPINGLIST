package com.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shoppinglist.Adapter.ShoppingListAdapter;
import com.shoppinglist.Model.ShoppingListModel;
import com.shoppinglist.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener
{

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;
    private List<ShoppingListModel> mList;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        myDB = new DataBaseHelper(MainActivity.this);
        mList = new ArrayList<>();
        adapter = new ShoppingListAdapter(myDB , MainActivity.this);

        mList = myDB.getAllProducts();
        Collections.reverse(mList);
        adapter.setProducts(mList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewProduct.newInstance().show(getSupportFragmentManager() , AddNewProduct.TAG);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface)
    {
        mList = myDB.getAllProducts();
        Collections.reverse(mList);
        adapter.setProducts(mList);
        adapter.notifyDataSetChanged();
    }
}