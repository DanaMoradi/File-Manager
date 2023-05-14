package com.example.filemanager;

import java.io.File;

public interface FileItemEventListener {

    void onItemClickListener(File file);

    void onItemFileDeleteClick(File file);

    void onItemCopyFileClick(File file);

    void onItemMoveFileClick(File file);

}
