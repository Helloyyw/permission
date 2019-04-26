package com.xmcc.dto;

import com.xmcc.model.SysAcl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysAclDto extends SysAcl {
    //角色拥有的权限模块
    private  boolean checked =false;
    //用户可以操作的权限点
    private boolean hasAcl = false;


    //把SysAcl封装成SysAclDto
    public static SysAclDto  adpter(SysAcl sysAcl){
        SysAclDto sysAclDto = new SysAclDto();//使用BeanUtils工具import org.springframework.beans.BeanUtils;
        BeanUtils.copyProperties(sysAcl,sysAclDto);
        return  sysAclDto;
    }






}
