package com.shoppinglist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shoppinglist.Model.ShoppingListModel;
import com.shoppinglist.Utils.DataBaseHelper;

public class AddNewProduct extends BottomSheetDialogFragment
{
    public static final String TAG = "AddNewProduct";

    //widgets
    private EditText mEditText;
    private Button mSaveButton;

    private DataBaseHelper myDb;

    public static AddNewProduct newInstance()
    {
        return new AddNewProduct();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newshoplist , container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);

        myDb = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            isUpdate = true;
            String product = bundle.getString("product");
            mEditText.setText(product);

            if(product.length() > 0)
            {
                mSaveButton.setEnabled(false);
            }
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(""))
                {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                }
                else
                {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();

                if(finalIsUpdate)
                {
                    myDb.updateProduct(bundle.getInt("id") , text);
                }
                else
                {
                    ShoppingListModel item = new ShoppingListModel();
                    item.setProduct(text);
                    item.setStatus(0);
                    myDb.insertProduct(item);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();

        if(activity instanceof OnDialogCloseListener)
        {
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
