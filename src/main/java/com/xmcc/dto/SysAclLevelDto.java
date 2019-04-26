package com.xmcc.dto;

import com.xmcc.model.SysAcl;
import com.xmcc.model.SysAclModule;
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
public class SysAclLevelDto extends SysAclModule {
    //定义一个aclModuleList集合来存下层SysAclLevelDto
    List<SysAclLevelDto> aclModuleList = new ArrayList<>();
    //定义一个aclList集合来存在当前层的权限点
    List<SysAclDto> aclList = new ArrayList<>();


    //查询所有的SysAclModule转换成SysAclLevelDto
    public static SysAclLevelDto adapter(SysAclModule sysAclModule) {
        SysAclLevelDto dto = new SysAclLevelDto();
        //copy  sysAclModule到 dto
        //此处用到BeanUtil对象
        BeanUtils.copyProperties(sysAclModule, dto);
        return dto;
    }
}
