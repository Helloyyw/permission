package com.xmcc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xmcc.beans.LogType;
import com.xmcc.beans.PageBeans;
import com.xmcc.dao.*;
import com.xmcc.dto.SeachLogDto;
import com.xmcc.exception.ParamException;
import com.xmcc.model.*;
import com.xmcc.param.*;
import com.xmcc.utils.BeanValidator;
import com.xmcc.utils.IpUtil;
import com.xmcc.utils.JsonMapper;
import com.xmcc.utils.RequestHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SysLogService {
    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysAclService sysAclService;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclModuleService sysAclModuleService;
    @Resource
    private SysUserMapper sysUserMapper;
    /**
     * 保存用户操作的日志记录：
     */
    //部门的日志记录
    public SysLogWithBLOBs saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.SYS_DEPT);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        //如果是添加操作的话before是为空的
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysLogWithBLOBs.setOperator(RequestHolder.getSysUserHodler().getUsername());
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);
        sysLogMapper.insert(sysLogWithBLOBs);
        return sysLogWithBLOBs;
    }

    //用户的日志记录
    public void saveUserLog(SysUser before, SysUser after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.SYS_USER);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        //如果是添加操作的话before是为空的
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysLogWithBLOBs.setOperator(RequestHolder.getSysUserHodler().getUsername());
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);
        sysLogMapper.insert(sysLogWithBLOBs);
    }

    //权限点的日志记录
    public void saveAclLog(SysAcl before, SysAcl after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.SYS_ACL);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        //如果是添加操作的话before是为空的
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysLogWithBLOBs.setOperator(RequestHolder.getSysUserHodler().getUsername());
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);
        sysLogMapper.insert(sysLogWithBLOBs);
    }

    //权限模块的日志记录
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.SYS_ACL_MODULED);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        //如果是添加操作的话before是为空的
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysLogWithBLOBs.setOperator(RequestHolder.getSysUserHodler().getUsername());
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);
        sysLogMapper.insert(sysLogWithBLOBs);
    }

    //角色模块的日志记录
    public void saveRoleLog(SysRole before, SysRole after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.SYS_ROLE);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        //如果是添加操作的话before是为空的
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysLogWithBLOBs.setOperator(RequestHolder.getSysUserHodler().getUsername());
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);
        sysLogMapper.insert(sysLogWithBLOBs);
    }



    /**
     * 根据dto对象来查询分页
     */
    public PageBeans<SysLog> SearchPageList(SeachLogParam seachLogParam, PageBeans<SysLog> page) {
        //参数验证
        BeanValidator.check(page);
        //封装dto
        SeachLogDto seachLogDto = new SeachLogDto();
        //设置类型
        seachLogDto.setType(seachLogParam.getType());
        //设置查询条件
        if (StringUtils.isNotBlank(seachLogParam.getAfterSeg())) {
            seachLogDto.setAfterSeg("%" + seachLogParam.getAfterSeg() + "%");
        }
        if (StringUtils.isNotBlank(seachLogParam.getBeforeSeg())) {
            seachLogDto.setAfterSeg("%" + seachLogParam.getBeforeSeg() + "%");
        }
        if (StringUtils.isNotBlank(seachLogParam.getOperator())) {
            seachLogDto.setAfterSeg("%" + seachLogParam.getOperator() + "%");
        }
        //设置时间
        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        try {
            if (StringUtils.isNotBlank(seachLogParam.getFromTime())) {

                seachLogDto.setFromTime(sdt.parse(seachLogParam.getFromTime()));
            }
            if (StringUtils.isNotBlank(seachLogParam.getToTime())) {

                seachLogDto.setFromTime(sdt.parse(seachLogParam.getToTime()));
            }
        } catch (ParseException e) {
            throw new ParamException("时间格式错误");
        }
        //查询是否存在dto的数据
        int count = sysLogMapper.selectCountByDto(seachLogDto);
        if (count > 0) {
            List<SysLog> pageList = sysLogMapper.findByDtoAandLimt(seachLogDto, page);
            page.setTotal(count);
            page.setData(pageList);
            return page;
        }
        return new PageBeans<SysLog>();
    }

    /**
     * 恢复功能
     */
    public void recover(int id) {
        //根据id获取log表中的对应的记录
        SysLogWithBLOBs log = sysLogMapper.selectByPrimaryKey(id);
        if (log == null) {
            throw new ParamException("带还原记录已经不存在了");
        }
        switch (log.getType()) {
            case LogType.SYS_DEPT:
                SysDept beforeSysDept = sysDeptMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeSysDept == null) {
                    throw new ParamException("带还原部门已经不存在了");
                }
            //新增和删除不做还原操作
                if(StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())){
                throw new ParamException("删除和还原不做恢复功能");
                }
                SysDept  afterDept = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysDept>() {});
                afterDept.setOperator(RequestHolder.getSysUserHodler().getUsername());
                afterDept.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
                afterDept.setOperateTime(new Date());
                //构建更新树需要的param参数
                DeptParam deptParam = new DeptParam();
                BeanUtils.copyProperties(afterDept,deptParam);
                //更新相应的部门树
                sysDeptService.update(deptParam);
                //sysDeptMapper.updateByPrimaryKey(afterDept);
                //写入日志记录
                  saveDeptLog(beforeSysDept,afterDept);
                break;
            case LogType.SYS_ACL:
                SysAcl beforeSysAcl= sysAclMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeSysAcl == null) {
                    throw new ParamException("带还原权限点已经不存在了");
                }
                //新增和删除不做还原操作
                if(StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())){
                    throw new ParamException("删除和还原不做恢复功能");
                }
                SysAcl  afterAcl = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysAcl>() {});
                afterAcl.setOperator(RequestHolder.getSysUserHodler().getUsername());
                afterAcl.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
                afterAcl.setOperateTime(new Date());
                //构建更新树需要的param参数
                AclParam aclParam = new AclParam();
                BeanUtils.copyProperties(afterAcl,aclParam);
                //更新相应的部门树
                sysAclService.UpdateAcl(aclParam);
                //写入日志记录
                saveAclLog(beforeSysAcl,afterAcl);
                break;
            case LogType.SYS_ACL_MODULED:
                SysAclModule beforeSysAclModule= sysAclModuleMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeSysAclModule == null) {
                    throw new ParamException("带还原权模块已经不存在了");
                }
                //新增和删除不做还原操作
                if(StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())){
                    throw new ParamException("删除和还原不做恢复功能");
                }
                SysAclModule  afterAclModule = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysAclModule>() {});
                afterAclModule.setOperator(RequestHolder.getSysUserHodler().getUsername());
                afterAclModule.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
                afterAclModule.setOperateTime(new Date());
                //构建更新树需要的param参数
                SaveAclModuleParam saveAclModuleParam = new SaveAclModuleParam();
                BeanUtils.copyProperties(afterAclModule,saveAclModuleParam);
                //更新相应的部门树
                sysAclModuleService.update(saveAclModuleParam);
                //写入日志记录
                saveAclModuleLog(beforeSysAclModule,afterAclModule);
                break;
            case LogType.SYS_ROLE:
                SysRole beforeSysRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeSysRole == null) {
                    throw new ParamException("带还原角色已经不存在了");
                }
                //新增和删除不做还原操作
                if(StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())){
                    throw new ParamException("删除和还原不做恢复功能");
                }
                SysRole  afterRole = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysRole>() {});
                afterRole.setOperator(RequestHolder.getSysUserHodler().getUsername());
                afterRole.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
                afterRole.setOperateTime(new Date());
                //更新相应的角色
                sysRoleMapper.updateByPrimaryKey(afterRole);
                //写入日志记录
                saveRoleLog(beforeSysRole,afterRole);
                break;
            case LogType.SYS_USER:
                SysUser beforeSysUser = sysUserMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeSysUser == null) {
                    throw new ParamException("带还原角色已经不存在了");
                }
                //新增和删除不做还原操作
                if(StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())){
                    throw new ParamException("删除和还原不做恢复功能");
                }
                SysUser  afterUser = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysUser>() {});
                afterUser.setOperator(RequestHolder.getSysUserHodler().getUsername());
                afterUser.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
                afterUser.setOperateTime(new Date());
                //更新相应的角色
                sysUserMapper.updateByPrimaryKey(afterUser);
                //写入日志记录
                saveUserLog(beforeSysUser,afterUser);
                break;
            case LogType.SYS_ROLE_ACL:
                SysRole beforeSysRole1 = sysRoleMapper.selectByPrimaryKey(log.getType());
                if (beforeSysRole1== null) {
                    throw new ParamException("带还原角色已经不存在了");
                }
                sysTreeService.updateRoleAclTree(log.getTargetId(),JsonMapper.string2Obj(log.getOldValue(), new TypeReference<List<Integer>>(){}));
                break;
            case LogType.SYS_ROLE_USER:
                SysRole beforeSysRole2 = sysRoleMapper.selectByPrimaryKey(log.getType());
                if (beforeSysRole2 == null) {
                    throw new ParamException("带还原角色已经不存在了");
                }
                sysRoleUserService.updateUserRole(log.getTargetId(),JsonMapper.string2Obj(log.getOldValue(), new TypeReference<List<Integer>>(){}));

                break;
        }

    }

}
