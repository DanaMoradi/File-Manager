package com.example.filemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewFolderDialog extends DialogFragment {

    private AddNewFolderCallBack callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBack = (AddNewFolderCallBack) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_new_folder, null, false);
        TextInputLayout inputLayout = view.findViewById(R.id.etl_addNewFolder);
        TextInputEditText editText = view.findViewById(R.id.et_addNewFolder);
        MaterialButton create = view.findViewById(R.id.btn_addNewFolder_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.length() > 0) {
                    callBack.onCreateFolderButtonClick(editText.getText().toString());
                    dismiss();
                } else inputLayout.setError("Folder Name con not be empty");
            }
        });

        return builder.setView(view).create();
    }
}
