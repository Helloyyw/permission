package com.xmcc.service;

import com.xmcc.dao.SysRoleMapper;
import com.xmcc.model.SysRole;
import com.xmcc.param.RoleParam;
import com.xmcc.utils.BeanValidator;
import com.xmcc.utils.IpUtil;
import com.xmcc.utils.RequestHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

@Service
public class SysRoleService {

    @Resource
    private SysLogService sysLogService;
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 角色列表
     */
    public List<SysRole> selectRoleList() {

        List<SysRole> sysRoleList = sysRoleMapper.findAll();
        System.out.println(sysRoleList + "==========");
        return sysRoleList;
    }

    /**
     * 添加列表
     */
    public int addRole(RoleParam roleParam) {
        //参数验证
        BeanValidator.check(roleParam);
        //判断name是不是相同
        int rename = sysRoleMapper.findByName(roleParam.getName());
        if (!(rename > 0)) {
            //封装到实体对象
            SysRole sysRole = SysRole.builder().name(roleParam.getName()).remark(roleParam.getRemark())
                    .status(roleParam.getStatus()).type(roleParam.getType()).build();
            sysRole.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
            sysRole.setOperateTime(new Date());
            sysRole.setOperator(RequestHolder.getSysUserHodler().getUsername());
            //执行插入
         int result = sysRoleMapper.insertSelective(sysRole);
         //记录日志
            sysLogService.saveRoleLog(null,sysRole);
          return   result;

        }
        return -1;
    }
    /**
     * 修改
     */
    public int updateRole(RoleParam roleParam) {
        //参数验证
        BeanValidator.check(roleParam);
        //查询修改前的对象
       SysRole reid = sysRoleMapper.selectByPrimaryKey(roleParam.getId());
        System.out.println(reid.getId()+"=========="+roleParam.getId());
        if ( !(reid == null) ){
            //封装到实体对象
            SysRole sysRole = SysRole.builder().id(roleParam.getId()).name(roleParam.getName()).remark(roleParam.getRemark())
                    .status(roleParam.getStatus()).type(roleParam.getType()).build();
            sysRole.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
            sysRole.setOperateTime(new Date());
            sysRole.setOperator(RequestHolder.getSysUserHodler().getUsername());
            //执行插入
            int result = sysRoleMapper.updateByPrimaryKey(sysRole);
            //记录日志
            sysLogService.saveRoleLog(reid,sysRole);
            return result;
        }
        return -1;
    }
}
