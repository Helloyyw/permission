package com.xmcc.filter;
import com.xmcc.model.SysUser;
import com.xmcc.utils.RequestHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //因为获取url需要用HttpServletRequest所以需要强制类型转换
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取当前的uri
        String uri = request.getRequestURI().toString();
        //判断地址然后根据地址决定放行还是拦截
        if(uri.contains("login") || uri.contains("signin")){
            filterChain.doFilter(request,response);
        }else {
            //获取登录的用户
            SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
            if(sysUser==null){
                response.sendRedirect("signin.jsp");
                return;
            }else{
                //把当前的reqeust和用户信息存进主线程中
                RequestHolder.addRequestHodler(request);
                RequestHolder.addSysUserHodler(sysUser);
                //如果用户不为null就放行
                filterChain.doFilter(request,response);
                return;
            }

        }

    }

    @Override
    public void destroy() {

    }
}
