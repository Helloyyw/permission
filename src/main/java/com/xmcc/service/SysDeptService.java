package com.xmcc.service;


import com.xmcc.dao.SysDeptMapper;
import com.xmcc.exception.ParamException;
import com.xmcc.model.SysDept;
import com.xmcc.param.DeptParam;
import com.xmcc.utils.BeanValidator;
import com.xmcc.utils.IpUtil;
import com.xmcc.utils.LevelUtils;
import com.xmcc.utils.RequestHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysLogService sysLogService;
    /**
     * 保存用户
     *
     * @param deptParam:表示做登录验证的参数类对象
     */
    public void save(DeptParam deptParam) {
        //首先我们要进行对输入框的参数验证根据validation提供的jar包工具类直接用
        BeanValidator.check(deptParam);
        //然后我们要验证同一个部门下面是否还有部门名字是相同的  ：重新写个方法
        if ((checkExistDeptName(deptParam.getParentId(), deptParam.getId(), deptParam.getName()) > 0)) {
            //如果查出来数据库有数据则返回参数异常
            throw new ParamException("部门名称重复异常");
        }
        System.out.println("验证通过===============");
        //验证通过后，把数据封装到SysDept实体类对象里面去::这里我们利用构建者来链式设置对象的属性
        SysDept sysDept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                .remark(deptParam.getRemark()).seq(deptParam.getSeq()).build();
        //设置该表的其它字段
        //parentLevel(层级)+parentId（父部门的id）自定义一个工具方法来计算Level
        // 这里要用父级部门的id来查询父级部门的level同样定义 一个方法
        // LevelUtils(deptParam.getParentId(),getLevel(deptParam.getParentId()));
        sysDept.setLevel(LevelUtils.calculate(deptParam.getParentId(), getLevel(deptParam.getParentId())));
        sysDept.setOperator(RequestHolder.getSysUserHodler().getUsername());//从一开始就把用户的信息都保存到主线程中去
        sysDept.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysDept.setOperateTime(new Date());
        System.out.println("要保存的数据："+sysDept.toString());
        //保存数据  执行插入
        sysDeptMapper.insertSelective(sysDept);
        //保存日志操作
        sysLogService.saveDeptLog(null,sysDept);
    }
    /**
     * @param parentId:需要添加的层级id
     * @param deptid:要添加的部门id，添加功能一般都是自动增长的
     * @param name:要添加的                     部门的名称
     * @return
     */
    public int checkExistDeptName(Integer parentId, Integer deptid, String name) {

        return sysDeptMapper.checkByParentIdAndName(parentId, deptid, name);
    }

    //通过传递的当前表的ParentId来获得他的父级的Level
    public String getLevel(Integer deptId) {

        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }
        return sysDept.getLevel();
    }

    /**
     * 更新部门信息（修改操作）
     */
    public int  update(DeptParam deptParam) {
        //验证参数
        BeanValidator.check(deptParam);
        //根据deptid查询更新前的数据
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        //判断更新前的部门是否存在
        if (before == null) {
            throw new ParamException("待更新的部门不存在");
        }
        //然后我们要验证同一个部门下面是否还有部门名字是相同的  ：重新写个方法
        if (checkExistDeptName(deptParam.getParentId(), deptParam.getId(), deptParam.getName()) > 0) {
            //如果查出来数据库有数据则返回参数异常
            throw new ParamException("部门名称重复异常");
        }
        //然后获取到要更新的数据准备修改
        //验证通过后，把数据封装到SysDept实体类对象里面去::这里我们利用构建者来链式设置对象的属性
        SysDept after = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId())
                .remark(deptParam.getRemark()).seq(deptParam.getSeq()).build();
        after.setLevel(LevelUtils.calculate(deptParam.getParentId(), getLevel(deptParam.getParentId())));
        after.setOperator(RequestHolder.getSysUserHodler().getUsername());//从一开始就把用户的信息都保存到主线程中去
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        after.setOperateTime(new Date());
        dGchildParentId(before, after);
        //日志记录
        sysLogService.saveDeptLog(before,after);
        return 1;
    }
    //递归更新子部门的parentId
    public void dGchildParentId(SysDept before, SysDept after) {
        //更新前的level
        String lodLevel = before.getLevel();
        System.out.println("更新前的lodLevel======"+lodLevel);
        //更新hou 的level
        String newLevel = after.getLevel();
        System.out.println("更新hou的lodLevel======"+newLevel);
        //直接更新父部门
        sysDeptMapper.updateByPrimaryKey(after);
        //定义一个List集合来存贮子部门
        //判断两次的level是否一样，如果一样就不用更新子部门

        if (!(lodLevel.equals(newLevel))) {
            //如果不一样就要更新他的子部门
        // 我们先根据更新前的id去查询到他的所有子部门
            List<SysDept> childList = sysDeptMapper.findchilddept(before.getId());
            System.out.println("子部门的集合：==========================" + childList.toString());
            //判断子部门集合是否为空，为空说明没有子部门了
            if (childList != null){
                //此时我需要循环遍历更新子部门的level
                for (int i = 0;i<childList.size();i++) {
                    //先保存一份当前层级的部门信息
                    SysDept nowDeptLevel = new SysDept();
                    BeanUtils.copyProperties(childList.get(i), nowDeptLevel);
                    //获取当前层的Level
                    String nowLevel = childList.get(i).getLevel();
                    //获取拼接后的LEVel
                    String newnewLevel = newLevel + "." + after.getId();
                    //然后修改当前层的level
                    childList.get(i).setLevel(newnewLevel);
                    //更新子部门
                    sysDeptMapper.updateByPrimaryKey(childList.get(i));
                    //然后递归
                    dGchildParentId(nowDeptLevel, childList.get(i));
                }
            }
        }
    }

    /**
     * 部门的删除操作
     */
    public void deleteDept(int id) {
        System.out.println("要删除的id==========="+id);
       //首先根据id 获取到部门的信息
        SysDept beforedelDept = sysDeptMapper.selectByPrimaryKey(id );
        //判断要删除的部门还存在不
        if(beforedelDept!=null){
            //使用该部门的id来查询判断该部门是否存在子部门
           List<SysDept> childDeptlist = sysDeptMapper.findchilddept(id);
            System.out.println("子部门集合==========="+childDeptlist.toString());
           if(childDeptlist.size()==0){
               //不能通过集合的初始值是否为空来判断，有无子部门，应该用它的长度
               //如果没有子部门就直接删除
               sysDeptMapper.deleteByPrimaryKey(id);
               //记录日志
               sysLogService.saveDeptLog(beforedelDept,null);
           }else {
               throw  new ParamException("存在子部门，不能删除");
           }
        }else{
            throw  new ParamException("该部门不存在");
        }

    }


}
