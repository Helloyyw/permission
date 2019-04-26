package com.xmcc.utils;

import com.xmcc.model.SysUser;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {
    //绑定当前登陆用户的信息
    private static ThreadLocal<SysUser> sysUserHodler = new ThreadLocal<>();
    //绑定当前的Request信息
    private static ThreadLocal<HttpServletRequest> requestHodler = new ThreadLocal<>();

    //把sysuser添加进来
    public static void addSysUserHodler(SysUser sysUser) {
        sysUserHodler.set(sysUser);
    }

    //把Request添加进来
    public static void addRequestHodler(HttpServletRequest request) {
        requestHodler.set(request);
    }

    //在需要用的地方在set出来
    public static SysUser getSysUserHodler() {
        return sysUserHodler.get();
    }

    public static HttpServletRequest getRequestHodler() {
        return requestHodler.get();
    }

//用完了还需要释放
    public static void remove1(){
        sysUserHodler.remove();
        requestHodler.remove();
    }
}
