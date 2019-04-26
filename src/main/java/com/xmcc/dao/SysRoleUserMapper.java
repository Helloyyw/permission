package com.xmcc.dao;

import com.xmcc.model.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<Integer> findRoleIdByuserId(@Param("userid") int userid);

    List<Integer> getUserByRoleId(@Param("roleId")int roleId);

   int deleteUserByOldUserIds(@Param("roleId")int roleId);

    int addAll(@Param("sysRoleUsers")List<SysRoleUser> sysRoleUsers);
}