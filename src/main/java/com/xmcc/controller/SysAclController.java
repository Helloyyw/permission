package com.xmcc.controller;

import com.xmcc.beans.PageBeans;
import com.xmcc.model.SysAcl;
import com.xmcc.param.AclParam;
import com.xmcc.param.AddUserParam;
import com.xmcc.service.SysAclService;
import com.xmcc.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sys/acl")
public class SysAclController {
    @Resource
    private SysAclService sysAclService;

    /**
     * 分页
     * @param aclModuleId
     * @param page
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData pageBeansById(int aclModuleId, PageBeans<SysAcl> page) {
        System.out.println("id= =================== " + aclModuleId);
        PageBeans<SysAcl> data = sysAclService.pagesByAclModuleId(aclModuleId, page);
        if (data == null) {
            return JsonData.fail("参数有误");
        }
        return JsonData.success(data);
    }
    /**
     * 添加操作
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData sysAcldd(AclParam aclParam) {
        if (sysAclService.addAcl(aclParam) == -1) {
            return JsonData.fail("用户状态不合法");
        }
        return JsonData.success();
    }

    /**
     * 删除操作
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData sysAclDelete(AclParam aclParam) {

        if (sysAclService.delete(aclParam) != 1) {
            return JsonData.fail("参数异常");
        }
        return JsonData.success();
    }

    /**
     * 修改操作
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData UpdateAcl(AclParam aclParam) {

        if (sysAclService.UpdateAcl(aclParam) == -1) {
            return JsonData.fail("参数异常");
        }
        return JsonData.success(sysAclService.UpdateAcl(aclParam));
    }
}
