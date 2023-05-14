package com.example.filemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class FilesListFragment extends Fragment implements FileItemEventListener {

    private String path;
    private FileAdapter adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_files, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.rv_files);
        TextView directoryPathTxt = view.findViewById(R.id.tv_files_path);
        ImageView back = view.findViewById(R.id.iv_files_back);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        File currentFolder = new File(path);
        if (StorageHelper.isExternalStorageWritable()) {

            File[] files = currentFolder.listFiles();
            adapter = new FileAdapter(Arrays.asList(files), this);
            recyclerView.setAdapter(adapter);
        }
        directoryPathTxt.setText(currentFolder.getName().equalsIgnoreCase("files") ? "External Storage" : currentFolder.getName());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    @Override
    public void onItemClickListener(File file) {

        if (file.isDirectory()) {
            ((MainActivity) getActivity()).listFile(file.getPath());
        }

    }

    @Override
    public void onItemFileDeleteClick(File file) {
        if (StorageHelper.isExternalStorageWritable()) {

            if (file.delete()) {
                adapter.deleteFile(file);
            }
        }
    }

    @Override
    public void onItemCopyFileClick(File file) {
        if (StorageHelper.isExternalStorageWritable()) {

            try {
                copy(file, getDestination(file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemMoveFileClick(File file) {
        if (StorageHelper.isExternalStorageWritable()) {
            try {
                if (!file.equals(getDestination(file.getName()))) {
                    copy(file, getDestination(file.getName()));
                    onItemFileDeleteClick(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createNewFolder(String FolderName) {

        if (StorageHelper.isExternalStorageWritable()) {

            File newFolder = new File(path + File.separator + FolderName);
            if (!newFolder.exists()) {
                if (newFolder.mkdir()) {
                    adapter.addFile(newFolder);
                    recyclerView.smoothScrollToPosition(0);
                }
            }

        }
    }

    private void copy(File source, File destination) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(source);
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read()) > 0) {
            fileOutputStream.write(buffer, 0, length);
        }

        fileInputStream.close();
        fileOutputStream.close();


    }


    private File getDestination(String fileName) {

        return new File(getContext().getExternalFilesDir(null).getPath() + File.separator + "Destination" + File.separator + fileName);
    }


    public void search(String query) {

        if (adapter != null)
            adapter.search(query);
    }

    public void setViewType(ViewTpe viewType) {
        if (adapter != null) {
            adapter.setViewTpe(viewType);
        }
        if (viewType == ViewTpe.GRID) {
            gridLayoutManager.setSpanCount(2);
        } else {
            gridLayoutManager.setSpanCount(1);
        }


    }


}






