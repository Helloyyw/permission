package com.xmcc.service;

import com.xmcc.dao.SysAclModuleMapper;
import com.xmcc.exception.ParamException;
import com.xmcc.model.SysAclModule;
import com.xmcc.param.SaveAclModuleParam;
import com.xmcc.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysAclModuleService {
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysLogService sysLogService;
    /**
     * 添加操作
     *
     * @param saveAclModuleParam
     */
    public void save(SaveAclModuleParam saveAclModuleParam) {
        //参数验证
        BeanValidator.check(saveAclModuleParam);
        //名字重复验证
        if (checkExistAclName(saveAclModuleParam.getParentId(), saveAclModuleParam.getId(), saveAclModuleParam.getName()) > 0) {
            throw new ParamException("权限名重复");
        }
        //验证通过后，把数据封装到SysACLmodul实体类对象里面去::这里我们利用构建者来链式设置对象的属性
        SysAclModule sysAclModule = SysAclModule.builder().name(saveAclModuleParam.getName())
                .parentId(saveAclModuleParam.getParentId()).remark(saveAclModuleParam.getRemark()).seq(saveAclModuleParam.getSeq()).status(saveAclModuleParam.getStatus()).build();
        sysAclModule.setLevel(LevelUtils.calculate(saveAclModuleParam.getParentId(), getLevel(saveAclModuleParam.getParentId())));
        sysAclModule.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysAclModule.setOperateTime(new Date());
        sysAclModule.setOperator(RequestHolder.getSysUserHodler().getUsername());
        //保存数据  执行插入
        sysAclModuleMapper.insertSelective(sysAclModule);
        //保存日志操作
        sysLogService.saveAclModuleLog(null,sysAclModule);

    }

    //判断有误重复的权限名
    public int checkExistAclName(Integer parentId, Integer id, String name) {

        return sysAclModuleMapper.checkByParentIdAndName(parentId, id, name);
    }

    //通过传递的当前表的ParentId来获得他的父级的Level
    public String getLevel(Integer Id) {

        SysAclModule sysacl = sysAclModuleMapper.selectByPrimaryKey(Id);
        if (sysacl == null) {
            return null;
        }
        return sysacl.getLevel();
    }

    /**
     * 删除操作
     *
     * @param id
     * @return
     */
    public int deleteAcl(int id) {
        System.out.println(id + "===================");
        //首先根据id 获取到该条数据的信息
        SysAclModule beforedelAcl = sysAclModuleMapper.selectByPrimaryKey(id);
        //判断要删除的部门还存在不
        if (beforedelAcl != null) {
            //使用该部门的id来查询判断该部门是否存在子部门
            List<SysAclModule> childAcllist = sysAclModuleMapper.findchildAcl(id);
            System.out.println(childAcllist.size());
            if (childAcllist.size() == 0) {
                //如果没有子部门就直接删除
                sysAclModuleMapper.deleteByPrimaryKey(id);
                //保存日志操作
                sysLogService.saveAclModuleLog(beforedelAcl,null);
                return 1;
            } else {
                throw new ParamException("存在权限点，不能删除");
            }
        } else {
            throw new ParamException("该权限模块不存在");
        }
    }

    /**
     * 更新操作
     */
    public int update(SaveAclModuleParam saveAclModuleParam) {
        //参数验证
        BeanValidator.check(saveAclModuleParam);
        //验证是否存在重名
        if (checkExistAclName(saveAclModuleParam.getParentId(), saveAclModuleParam.getId(), saveAclModuleParam.getName()) > 0) {
            throw new ParamException("名字重复");
        }
        //保存一份更新前的数据
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(saveAclModuleParam.getId());
        if (before == null) {
            throw new ParamException("要更新的权限信息不存在");
        }
        //把要更新的数据封装到实体类
        SysAclModule after = SysAclModule.builder().id(saveAclModuleParam.getId()).name(saveAclModuleParam.getName())
          .parentId(saveAclModuleParam.getParentId()).remark(saveAclModuleParam.getRemark()).seq(saveAclModuleParam.getSeq()).status(saveAclModuleParam.getStatus()).build();
        after.setLevel(LevelUtils.calculate(saveAclModuleParam.getParentId(), getLevel(saveAclModuleParam.getParentId())));
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        after.setOperateTime(new Date());
        after.setOperator(RequestHolder.getSysUserHodler().getUsername());
        //递归更新
        dGUpdateAcl(before, after);
        //保存日志操作
        sysLogService.saveAclModuleLog(before,after);
        return 1;
    }

//递归更新
    public void dGUpdateAcl(SysAclModule before, SysAclModule after) {
       String oldLevel = before.getLevel();
       String newlevel = after.getLevel();
       if(!oldLevel.equals(newlevel)){
           //不是同一层的数据就去寻找更新前的id去匹配子集的parentID
           List<SysAclModule> chidlevelList = sysAclModuleMapper.findchildAcl(before.getId());
           if(chidlevelList != null){
                //遍历子集和修改他们的level
               for(SysAclModule it: chidlevelList){
                   //先保存一份当前层级的部门信息
                   SysAclModule nowAcllevel = new SysAclModule();
                   BeanUtils.copyProperties(chidlevelList,nowAcllevel);
                   //获取当前层level
                   String nowlevel = it.getLevel();
                   //获取新的拼接后的level
                   String newnewLevel = newlevel+"."+after.getId();
                   //修改最新的leve;l
                   it.setLevel(newnewLevel);
                   sysAclModuleMapper.updateByPrimaryKey(it);
                   dGUpdateAcl(nowAcllevel,it);
               }
           }
      }
        sysAclModuleMapper.updateByPrimaryKey(after);
    }


}


