package com.xmcc.utils;

import java.util.ArrayList;
import java.util.List;

public class StringToList {
    public static List<Integer> stingToList(String str){
        if(str==null || str.equals("")){
         return  new ArrayList<>();
        }
           List<Integer> newList = new ArrayList<>();
           String[] arr = str.split(",");
           for(int i = 0;i<arr.length;i++){
               newList.add(Integer.parseInt(arr[i]));
           }

        return newList;
    }
 }
