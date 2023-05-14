package com.example.filemanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddNewFolderCallBack {

    private ImageView addFolder;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (StorageHelper.isExternalStorageReadable()) {
            File externalFilesDir = getExternalFilesDir(null);
            listFile(externalFilesDir.getPath(), false);
        } else {
            Log.e("ERROR : ", StorageHelper.isExternalStorageReadable() + "");
        }


        addFolder = findViewById(R.id.iv_main_addNewFolder);
        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewFolderDialog addNewFolderDialog = new AddNewFolderDialog();
                addNewFolderDialog.show(getSupportFragmentManager(), null);
            }
        });


        search = findViewById(R.id.et_main_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fram_main_fragmentContainer);
                if (fragment instanceof FilesListFragment) {
                    ((FilesListFragment) fragment).search(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.tg_main);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.btn_main_list && isChecked) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fram_main_fragmentContainer);
                    if (fragment instanceof FilesListFragment) {
                        ((FilesListFragment) fragment).setViewType(ViewTpe.ROW);
                    }
                } else if (checkedId == R.id.btn_main_grid && isChecked) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fram_main_fragmentContainer);
                    if (fragment instanceof FilesListFragment) {
                        ((FilesListFragment) fragment).setViewType(ViewTpe.GRID);
                    }
                }
            }
        });

    }


    public void listFile(String path, Boolean addToBack) {
        FilesListFragment filesListFragment = new FilesListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        filesListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram_main_fragmentContainer, filesListFragment);
        if (addToBack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void listFile(String path) {
        this.listFile(path, true);
    }

    @Override
    public void onCreateFolderButtonClick(String FolderName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fram_main_fragmentContainer);
        if (fragment instanceof FilesListFragment) {
            ((FilesListFragment) fragment).createNewFolder(FolderName);
        }
    }
}