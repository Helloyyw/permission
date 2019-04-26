package com.xmcc.service;

import com.xmcc.beans.LogType;
import com.xmcc.dao.SysLogMapper;
import com.xmcc.dao.SysRoleUserMapper;
import com.xmcc.dao.SysUserMapper;
import com.xmcc.model.SysLogWithBLOBs;
import com.xmcc.model.SysRoleUser;
import com.xmcc.model.SysUser;
import com.xmcc.utils.IpUtil;
import com.xmcc.utils.JsonMapper;
import com.xmcc.utils.RequestHolder;
import com.xmcc.utils.StringToList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SysRoleUserService {
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogMapper sysLogMapper;


    //角色与用户查询
    public List<SysUser> roleAndUser(int roleId) {
        //根据角色的id去查询该角色当前的用户id
        List<Integer> sysUserIdByRoId = sysRoleUserMapper.getUserByRoleId(roleId);
        if (sysUserIdByRoId.size() == 0) {
            return new ArrayList<>();
        }
        //根据当前用户id去查询当前用户
        List<SysUser> sysUsers = sysUserMapper.queryUser(sysUserIdByRoId);
        return sysUsers;
    }


    public Map<String, List<SysUser>> getMapSysRoleUser(int roleId) {
        //获得当前角色已经拥有的用户信息
        List<SysUser> selected = roleAndUser(roleId);
        //获取全部的用户集合
        List<SysUser> allListUsers = sysUserMapper.getAll();
        // 剩下的的用户集合
        List<SysUser> unselected = new ArrayList<>();
        //剩下的的用户集合等于所有的集合减去新的
        for (SysUser sysUser : allListUsers) {
            if (!selected.contains(sysUser) && sysUser.getStatus() == 1) {
                unselected.add(sysUser);
            }
        }
        Map<String, List<SysUser>> map = new HashMap<>();

        map.put("selected", selected);
        map.put("unselected", unselected);
        return map;
    }
    /**
     * 更新角色用户的关系
     */
    public void updateUserRole(int roleId, List<Integer> updateUserIds) {
        //根据角色id查询对应的用户集合
        List<Integer> oldSysUserIds = sysRoleUserMapper.getUserByRoleId(roleId);
        //先删除老集合的元素
        //对比2个集合的长度
        if (updateUserIds.size() == oldSysUserIds.size()) {
            //比较内容  先移除新集合中老集合的元素
            boolean b = updateUserIds.removeAll(oldSysUserIds);
            if (b) {
                return;
            }

        }
        sysRoleUserMapper.deleteUserByOldUserIds(roleId);
        //执行更新  然后查询添加新集合中的元素
        if (updateUserIds.size() != 0) {
            //添加新的数据
            List<SysRoleUser> sysRoleUsers = new ArrayList<>();
            for (int i = 0; i < updateUserIds.size(); i++) {
                SysRoleUser sysUser = SysRoleUser.builder().userId(updateUserIds.get(i)).roleId(roleId).operateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()))
                        .operateTime(new Date()).operator(RequestHolder.getSysUserHodler().getUsername()).build();
                sysRoleUsers.add(sysUser);
            }
            sysRoleUserMapper.addAll(sysRoleUsers);
            //日志记录
            saveRoleUserLog(roleId,oldSysUserIds,updateUserIds);
        }

    }
    //角色用户模块的日志记录
    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.SYS_ROLE_USER);
        sysLogWithBLOBs.setTargetId(roleId);
        //如果是添加操作的话before是为空的
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysLogWithBLOBs.setOperator(RequestHolder.getSysUserHodler().getUsername());
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);
        sysLogMapper.insert(sysLogWithBLOBs);
    }
}
