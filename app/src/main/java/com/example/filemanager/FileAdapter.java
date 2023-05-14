package com.example.filemanager;

import static com.example.filemanager.R.id.menu_copy;
import static com.example.filemanager.R.id.menu_delete;
import static com.example.filemanager.R.id.menu_move;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.fileViewHolder> {


    private List<File> files;
    private List<File> filteredFiles;
    private FileItemEventListener eventListener;

    private ViewTpe viewTpe = ViewTpe.ROW;


    public FileAdapter(List<File> files, FileItemEventListener eventListener) {
        this.files = new ArrayList<>(files);
        this.eventListener = eventListener;
        this.filteredFiles = this.files;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setViewTpe(ViewTpe viewTpe) {
        this.viewTpe = viewTpe;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public fileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == ViewTpe.ROW.getValue() ? R.layout.item_files : R.layout.item_files_grid, parent, false);
        return new fileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull fileViewHolder holder, int position) {
        holder.bindFile(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTpe.getValue();
    }

    public class fileViewHolder extends RecyclerView.ViewHolder {
        private TextView fileName;
        private ImageView directoryImg;
        private ImageView moreIv;

        public fileViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.tv_file_name);
            directoryImg = itemView.findViewById(R.id.iv_file);
            moreIv = itemView.findViewById(R.id.iv_file_more);

        }

        private void bindFile(File file) {
            if (file.isDirectory()) {
                directoryImg.setImageResource(R.drawable.ic_folder_black_32dp);
            } else {
                directoryImg.setImageResource(R.drawable.ic_file_black_32dp);
            }

            fileName.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onItemClickListener(file);
                }
            });


            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_file_item, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case menu_delete:
                                    eventListener.onItemFileDeleteClick(file);
                                    break;
                                case menu_copy:
                                    eventListener.onItemCopyFileClick(file);
                                    break;
                                case menu_move:
                                    eventListener.onItemMoveFileClick(file);
                                    break;
                            }


                            return true;
                        }
                    });
                }
            });
        }
    }

    public void addFile(File file) {
        files.add(0, file);
        notifyItemInserted(0);
    }

    public void deleteFile(File file) {
        int index = files.indexOf(file);
        if (index > -1) {
            files.remove(index);
            notifyItemRemoved(index);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void search(String query) {

        List<File> searchResult = new ArrayList<>();
        if (query.length() > 0) {
            for (File file : this.files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase()))
                    searchResult.add(file);
            }

            this.filteredFiles = searchResult;
            notifyDataSetChanged();

        } else {
            this.filteredFiles = this.files;
            notifyDataSetChanged();
        }
    }

}
