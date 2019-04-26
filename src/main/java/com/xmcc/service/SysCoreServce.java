package com.xmcc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xmcc.beans.CachePrefix;
import com.xmcc.dao.SysAclMapper;
import com.xmcc.dao.SysRoleAclMapper;
import com.xmcc.dao.SysRoleMapper;
import com.xmcc.dao.SysRoleUserMapper;
import com.xmcc.model.SysAcl;
import com.xmcc.utils.JsonMapper;
import com.xmcc.utils.RequestHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class SysCoreServce {
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysCacheService sysCacheService;

    //根据用户的id去角色-用户关联表，去查询角色的id的集合。然后用角色的id集合，去查询角色权限关联表对应的权限集合id，最后查询权限点集合
    public List<SysAcl> getUserAclList() {
        //获取用户id
        int userid = RequestHolder.getSysUserHodler().getId();
        //查询角色的id集合
        List<Integer> roleIdList = sysRoleUserMapper.findRoleIdByuserId(userid);
        if (roleIdList.size() == 0) {
            return new ArrayList<>();
        }
        //判断是否是超级管理员Admin
        if (userid == 1) {
            //给他所有权限点集合
            return sysAclMapper.findAll();
        }
        //不是超级管理员就去查询当前用户的拥有的权限点
        //根据角色的id集合去查询权限的id集合
        List<Integer> AclIdList = sysRoleAclMapper.findAclIdByRoleId(roleIdList);
        if (AclIdList == null) {
            return new ArrayList<>();
        }
        //根据角色的id集合去权限表查询权限集合
        List<SysAcl> userAclList = sysAclMapper.findAclByAclId(AclIdList);

        return userAclList;
    }
    //通过前端传过来的roleId来获取所点击的角色拥有哪些权限点
    public List<SysAcl> getAclListByRoleId(int roleId) {
        //这里传过来只有一个角色id为了配合上面写好的sql语句我把它加到一个集合中 去查询数据库、
        List<Integer> roleIdlist1 = new ArrayList<>();
        roleIdlist1.add(roleId);
        //然后去查询aclId的集合
        List<Integer> AclIdByRoleid = sysRoleAclMapper.findAclIdByRoleId(roleIdlist1);
        if (AclIdByRoleid.size() == 0) {
            return new ArrayList<>();
        }
        //如果不为空直接返回通过aclid查询出来的acl集合
        return sysAclMapper.findAclByAclId(AclIdByRoleid);
    }

    //判断是不是超级管理员
    public boolean isAdmin() {
        int userid = RequestHolder.getSysUserHodler().getId();
        //判断是否是超级管理员Admin
        if (userid == 1) {
            //给他所有权限点集合
            return true;
        }
        return false;
    }

    //判断用户有没有操作的权限
    public boolean hasAcl(String url) {
        //判断用户是不是超级管理员
        if (isAdmin()) {
            return true;
        }
        //注意：这里还需要判断当前url是否是权限点 如果不是则人人都能访问
        SysAcl sysAcl = sysAclMapper.findUrlByUrl(url);
        //如果当前url为空，
        if (sysAcl == null) {
            return true;
        }
        //不是超级管理员需要判断当前用户是否拥有执行该url的权限
        //获取当前用户的所有权限点
        //   List<SysAcl> currentUser = getUserAclList();
        List<SysAcl> currentUser = getUserAclFormCache();//从缓存里面拿
        if (currentUser.contains(sysAcl)) {
            return true;
        }
        return false;
    }
   //从缓存中拿出数据
   public   List<SysAcl> getUserAclFormCache(){
       List<SysAcl> sysAclList =   JsonMapper.string2Obj(sysCacheService.getInfoFromCach("userAcl"
               , CachePrefix.USER_ACLS), new TypeReference<List<SysAcl>>() {});
       if(sysAclList == null){
           List<SysAcl> currentUser = getUserAclList();
           String currentUserString = JsonMapper.obj2String(currentUser);
           sysCacheService.saveCache(currentUserString,60*60*24,"userAcl", CachePrefix.USER_ACLS);
           return currentUser;
       }
       return sysAclList;
   }
}
