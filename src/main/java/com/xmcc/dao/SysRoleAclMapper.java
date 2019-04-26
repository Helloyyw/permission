package com.xmcc.dao;

import com.xmcc.model.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    List<Integer> findAclIdByRoleId(@Param("roleIdList") List<Integer> roleIdList);

    void deleteAclByRoleId(@Param("roleId")int roleId);

    int insertToNewList(@Param("sysRoleAcls") List<SysRoleAcl> sysRoleAcls);
}