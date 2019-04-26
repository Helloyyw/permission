package com.xmcc.dao;

import com.xmcc.beans.PageBeans;
import com.xmcc.dto.SeachLogDto;
import com.xmcc.model.SysLog;
import com.xmcc.model.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    int selectCountByDto(@Param("seachLogDto") SeachLogDto seachLogDto);

    List<SysLog> findByDtoAandLimt(@Param("seachLogDto")SeachLogDto seachLogDto,@Param("page") PageBeans<SysLog> page);
}