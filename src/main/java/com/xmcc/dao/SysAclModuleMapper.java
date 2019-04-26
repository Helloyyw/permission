package com.xmcc.dao;

import com.xmcc.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    List<SysAclModule> findAll();

    int checkByParentIdAndName(@Param("parentId") Integer parentId,@Param("id")  Integer id,@Param("name")  String name);

    List<SysAclModule> findchildAcl(@Param("id") int id);
}