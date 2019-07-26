package com.ckw.contactindexview;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by ckw
 * on 2019/7/26.
 */
public class TreeUtils {
    //第一级别为0
    public static int getLevel(TreePoint treePoint,HashMap<String,TreePoint> map){
        if("0".equals(treePoint.getPARENTID())){
            return 0;
        }else{
            return 1+getLevel(getTreePoint(treePoint.getPARENTID(),map),map);
        }
    }



    public static TreePoint getTreePoint(String ID, HashMap<String,TreePoint> map){
        if(map.containsKey(ID)){
            return map.get(ID);
        }
        Log.e("xlc","ID:" + ID);
        return null;
    }
}
