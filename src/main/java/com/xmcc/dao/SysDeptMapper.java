package com.xmcc.dao;

import com.xmcc.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    int checkByParentIdAndName(@Param("parentId") Integer parentId, @Param("deptid") Integer deptid,@Param("name")  String name);

    List<SysDept> findAll();

    List<SysDept> findchilddept(@Param("id") Integer id);
}