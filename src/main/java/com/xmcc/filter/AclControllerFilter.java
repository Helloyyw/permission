package com.xmcc.filter;

import com.xmcc.commons.ApplicationContextHelper;
import com.xmcc.model.SysUser;
import com.xmcc.service.SysCoreServce;
import com.xmcc.utils.JsonData;
import com.xmcc.utils.JsonMapper;
import com.xmcc.utils.RequestHolder;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class AclControllerFilter implements Filter {
    //    无权限访问页
    private final static String noAouth = "/sys/user/noAuth.page";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取当前的url
        String url = request.getRequestURI();
        System.out.println("获取当前的url"+url);
        //判断当前的url如果包含 登录页、无权限访问页则自动放行
        if (url.contains("login") || url.contains(noAouth)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //获取用户
        SysUser sysUser = RequestHolder.getSysUserHodler();
        //如果用户为空
        if (sysUser == null) {
            //进行相应的处理
            noAuth(request, response);
            return;
        }
        //利用工具类加载Service层的相关类对象调用判断方法
        SysCoreServce sysCoreServce = ApplicationContextHelper.popBean(SysCoreServce.class);

        //判断是否用户有相应的权限,如果没有相应的权限就执行返回前端相应的提示
        if (!sysCoreServce.hasAcl(url)) {
            //进行相应的处理
            noAuth(request, response);
            return;
        }
        //放行
        filterChain.doFilter(request, response);
        return;
    }
    //如果没有相应的权限或者用户名我为空就执行该方法
    /**
     * 该方法用于给前端返回信息，一种一json数据形式一种一页面的信息形式
     * @param request
     * @param response
     */
    private void  noAuth(HttpServletRequest request,HttpServletResponse response)  {
        String uri = request.getRequestURI().toString();
        if (uri.endsWith(".json")) {
            JsonData jsonData = JsonData.fail("没有该权限，请联系管理员！");
            //然后把json数据返回到前端显示
            /**
             * 注意：这里要返回前端的是json对象 所以就要-定义一个请求头信息
             */
            response.setHeader("Content-Type","application/json");
            try {
                response.getWriter().print(JsonMapper.obj2String(jsonData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            response.setHeader("Content-Type","text/html");
            try {
                response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                        + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                        + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                        + "window.location.href='" + noAouth + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void destroy() {

    }

 }


