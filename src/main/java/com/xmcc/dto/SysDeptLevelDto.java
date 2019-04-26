package com.xmcc.dto;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.xmcc.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;


import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class SysDeptLevelDto extends SysDept {

    //定义一个list集合来存贮部门层级树
  public  List<SysDeptLevelDto> deptList = new ArrayList<>();

    //需要把SysDept封装成SysDeptLevelDto
    public static SysDeptLevelDto adapter(SysDept sysDept){
        SysDeptLevelDto dto = new SysDeptLevelDto();
        //copy  sysDept 到 dto
        //此处用到BeanUtil对象
        BeanUtils.copyProperties(sysDept,dto);
        return dto;
    }

}
