package com.xmcc.controller;

import com.xmcc.dto.SysAclLevelDto;
import com.xmcc.param.SaveAclModuleParam;
import com.xmcc.service.SysAclModuleService;
import com.xmcc.service.SysTreeService;
import com.xmcc.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/aclModule")
public class SysAclMoudleController {

    @Resource
    private SysTreeService sysTreeService;
    @Resource
    SysAclModuleService sysAclModuleService;
    /**
     * 进入权限管理页面
     *
     * @return
     */
    @RequestMapping("/acl.page")
    public ModelAndView toAcl() {

        return new ModelAndView("acl");
    }
/**
 * 新增操作
 */
@RequestMapping("/save.json")
@ResponseBody
public JsonData save(SaveAclModuleParam saveAclModuleParam) {
     sysAclModuleService.save(saveAclModuleParam);
    return JsonData.success();
}
    /**
     * 查询权限树
     */

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData aclTree() {
       List<SysAclLevelDto> data =  sysTreeService.aclTree();
        return JsonData.success(data);
    }
    /**
     * 删除功能
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteAcl(int id) {
        if(sysAclModuleService.deleteAcl(id)!=1){
            return  JsonData.fail("待删除的权限模块不存在");
        };
        return JsonData.success();
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAcl(SaveAclModuleParam saveAclModuleParam) {
        System.out.println("hahahahah");
        if(sysAclModuleService.update(saveAclModuleParam)!=1){
            return  JsonData.fail("待删除的权限模块不存在");
        }
        return JsonData.success();
    }


}
