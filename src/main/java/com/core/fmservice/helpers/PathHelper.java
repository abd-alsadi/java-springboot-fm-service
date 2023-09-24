package com.core.fmservice.helpers;

import com.core.fmservice.models.*;
public class PathHelper {
    public static String getPathDir(FileModel file){
        String path  = "";
        if(file.getOwner()!=null && !file.getOwner().equals(""))
        path+=file.getOwner();

        path+=file.getId();
        return path;
    }
}
