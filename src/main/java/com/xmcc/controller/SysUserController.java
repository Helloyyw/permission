package com.xmcc.controller;

import com.xmcc.beans.PageBeans;
import com.xmcc.model.SysUser;
import com.xmcc.param.AddUserParam;
import com.xmcc.param.UserParam;
import com.xmcc.service.SysUserService;
import com.xmcc.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    /**
     * 登录模块
     * @param request
     * @return
     */
    @RequestMapping("/login.page")
    public ModelAndView login(UserParam userParam, HttpServletRequest request) {
        SysUser sysUser = sysUserService.checkLogin(userParam);
        request.getSession().setAttribute("sysUser", sysUser);
        return new ModelAndView("admin");
    }

    /**
     * 退出功能
     * @param request
     * @return
     */
    @RequestMapping("/logout.page")
    public ModelAndView logout(HttpServletRequest request) {
        System.out.println("点击退出登录");

        request.getSession().removeAttribute("sysUser");
        return new ModelAndView("signin");
    }
    /**
     * 按照部门id查询用户表分页显示
     * @param
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData sysUserPages(int deptId, PageBeans<SysUser> page){
          PageBeans<SysUser> data = sysUserService.pagesByDeptId( deptId, page);
        return JsonData.success(data);
    }
    /**
     * x新增用户
     */
@RequestMapping("/save.json")
@ResponseBody
    public JsonData sysUserAdd(AddUserParam addUserParam){
    if(sysUserService.addUser(addUserParam) == -1){
        return JsonData.fail("用户状态不合法");
    }

    return JsonData.success();
    }

    /**
     * 更新用户
     * @param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData sysUserUpdate(AddUserParam addUserParam){
        System.out.println("执行更新从头roller");
        if(sysUserService.UpdateUser(addUserParam) == -1){
            return JsonData.fail("用户状态不合法");
        }
        return JsonData.success();
    }
/**
 * 无权限时候应该访问的提示页面
 *
 */
@RequestMapping("/noAuth.page")
    public ModelAndView page(){
    return new ModelAndView("noAuth");
}
}
