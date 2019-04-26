package com.xmcc.utils;

public class LevelUtils {

    public  static  final String ROOT ="0";//定义最层顶层级
    public static  final String point =".";

    //写一个方法来拼接leve
    public static  String calculate(Integer parentId,String Level){

    if(Level==null){
    return  ROOT;
    }

        return Level+point+parentId;
    }

}
