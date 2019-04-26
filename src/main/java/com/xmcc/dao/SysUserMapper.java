package com.xmcc.dao;

import com.xmcc.beans.PageBeans;
import com.xmcc.model.SysRoleUser;
import com.xmcc.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

     SysUser findByName(@Param("username") String username);

    SysUser findByNameId(@Param("username") String username,@Param("userid")Integer userid);

    int findByDpetId(@Param("deptId") int deptid);

    List<SysUser> findBydeptIdAandLimt(@Param("deptId") int deptId,@Param("page")  PageBeans<SysUser> page);

    List<SysUser> queryUser(@Param("sysUserIdByRoId") List<Integer> sysUserIdByRoId);

    List<SysUser> getAll();


}