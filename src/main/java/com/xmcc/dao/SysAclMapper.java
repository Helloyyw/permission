package com.xmcc.dao;

import com.xmcc.beans.PageBeans;
import com.xmcc.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int selectByModuleId(@Param("id") int id);

    List<SysAcl> findByAclAndPage(@Param("id")int id, @Param("page")PageBeans<SysAcl> page);

    int findByName(@Param("name")String name,@Param("id")int id);

    List<SysAcl> findAclByAclId(@Param("aclIdList")List<Integer> aclIdList);

    List<SysAcl> findAll();

    SysAcl findUrlByUrl(@Param("url")String url);
}