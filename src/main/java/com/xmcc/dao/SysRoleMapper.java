package com.xmcc.dao;

import com.xmcc.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> findAll();

    int findByName(@Param("name") String name);

    List<String> findNameByRoleId(@Param("roleIdList")List<Integer> roleIdList);
}