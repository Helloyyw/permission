package com.xmcc.controller;

import com.xmcc.dto.SysAclLevelDto;
import com.xmcc.model.SysRole;
import com.xmcc.model.SysRoleUser;
import com.xmcc.model.SysUser;
import com.xmcc.param.RoleParam;
import com.xmcc.service.SysRoleService;
import com.xmcc.service.SysRoleUserService;
import com.xmcc.service.SysTreeService;
import com.xmcc.utils.JsonData;
import com.xmcc.utils.StringToList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleUserService sysRoleUserService;

    @RequestMapping("/role.page")
    public ModelAndView toRolePage() {

        return new ModelAndView("role");
    }

    /**
     * 角色列表
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData roleList() {
        System.out.println("开始查询");
        List<SysRole> sysRoleList = sysRoleService.selectRoleList();

        return JsonData.success(sysRoleList);
    }

    /**
     * 添加角色
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData addRole(RoleParam roleParam) {
        System.out.println("开始");
        if (sysRoleService.addRole(roleParam) == -1) {
            return JsonData.fail("添加失败");
        }

        return JsonData.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(RoleParam roleParam) {
        System.out.println("开始");

        if (sysRoleService.updateRole(roleParam) == -1) {
            return JsonData.fail("添加失败");
        }

        return JsonData.success();
    }

    //角色权限树
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleAclTree(int roleId) {

        List<SysAclLevelDto> sysAclLevelDtos = sysTreeService.RoleAclTree(roleId);

        return JsonData.success(sysAclLevelDtos);
    }

    //角色权限树更新
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData updateRoleAclTree(@RequestParam("roleId") int roleId, String aclIds) {
        //手写工具类把string转化为List<Integer>集合
        List<Integer> aclIdsList = StringToList.stingToList(aclIds);
        sysTreeService.updateRoleAclTree(roleId, aclIdsList);

        return JsonData.success("操作成功");
    }

    //角色下面的用户查询
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(int roleId) {
        Map<String, List<SysUser>> map = sysRoleUserService.getMapSysRoleUser(roleId);
        return JsonData.success(map);
    }

    //更新角色下面的用户查询
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData updateUsers(int roleId, String userIds) {
        List<Integer> updateUserId = StringToList.stingToList(userIds);
      sysRoleUserService.updateUserRole(roleId,updateUserId);
        return JsonData.success("操作成功");
    }
}
