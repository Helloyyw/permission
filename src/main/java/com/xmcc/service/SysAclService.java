package com.xmcc.service;
import com.xmcc.beans.PageBeans;
import com.xmcc.dao.SysAclMapper;
import com.xmcc.exception.ParamException;
import com.xmcc.model.SysAcl;
import com.xmcc.param.AclParam;
import com.xmcc.utils.BeanValidator;
import com.xmcc.utils.IpUtil;
import com.xmcc.utils.RequestHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SysAclService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;
    /**
     * 根据aclmoduleId分页
     *
     * @param id
     * @param page
     * @return
     */
    public PageBeans<SysAcl> pagesByAclModuleId(int id, PageBeans<SysAcl> page) {
//参数验证
        BeanValidator.check(page);
        //根据当前的id查询该权限模块下的权限点是否存在
        int count = sysAclMapper.selectByModuleId(id);
        if (count > 0) {
            //说明存在权限点
            PageBeans<SysAcl> pageBean = new PageBeans<>();
            //查询每个权限模块下的权限点集合
            List<SysAcl> sysAclList = sysAclMapper.findByAclAndPage(id, page);
            pageBean.setTotal(count);
            pageBean.setData(sysAclList);
            return pageBean;
        }

        return new PageBeans<SysAcl>();

    }

    /**
     * 添加权限点
     */
    public int addAcl(AclParam aclParam) {
        //判断参数
        BeanValidator.check(aclParam);
        //检查是否重名
        int re = sysAclMapper.findByName(aclParam.getName(),aclParam.getId());
        if (!(re > 0)) {
            //把aclParam转换成sysAcl对象
            SysAcl newAcl = SysAcl.builder().url(aclParam.getUrl()).aclModuleId(aclParam.getAclModuleId())
                    .name(aclParam.getName()).remark(aclParam.getRemark())
                    .seq(aclParam.getSeq()).status(aclParam.getStatus()).type(aclParam.getType()).build();
            newAcl.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
            newAcl.setOperateTime(new Date());
            newAcl.setOperator(RequestHolder.getSysUserHodler().getUsername());
            //SimpleDateFormat code = new SimpleDateFormat("yyyyMMddHHmmss_MM");
            newAcl.setCode(UUID.randomUUID().toString().substring(0, 20));
            //执行保存语句
            int result = sysAclMapper.insertSelective(newAcl);
            //记录日志
            sysLogService.saveAclLog(null,newAcl);
            return result;
        }
        return -1;
    }

    /**
     * 更新操作
     */
    public int UpdateAcl(AclParam aclParam) {
        //判断参数
        BeanValidator.check(aclParam);
        //检查是否重名
        int re = sysAclMapper.findByName(aclParam.getName(),aclParam.getId());
        if (!(re > 0)) {
            //把aclParam转换成sysAcl对象
            SysAcl newAcl = SysAcl.builder().id(aclParam.getId()).url(aclParam.getUrl()).aclModuleId(aclParam.getAclModuleId())
                    .name(aclParam.getName()).remark(aclParam.getRemark())
                    .seq(aclParam.getSeq()).status(aclParam.getStatus()).type(aclParam.getType()).build();
            newAcl.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
            newAcl.setOperateTime(new Date());
            newAcl.setOperator(RequestHolder.getSysUserHodler().getUsername());
            //SimpleDateFormat code = new SimpleDateFormat("yyyyMMddHHmmss_MM");
            newAcl.setCode(UUID.randomUUID().toString().substring(0, 20));
            //执行保存语句
            int result = sysAclMapper.updateByPrimaryKey(newAcl);
            //记录日志
            sysLogService.saveAclLog(sysAclMapper.selectByPrimaryKey(aclParam.getId()),newAcl);
            return  result;
        }
        return -1;
    }

    /**
     * 删除操作
     */

    public int delete(AclParam aclParam) {
        SysAcl sysAcl = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        if ( sysAcl == null) {
            throw new ParamException("该条数据不存在");
        } else {
           int result = sysAclMapper.deleteByPrimaryKey(aclParam.getId());
            //记录日志
            sysLogService.saveAclLog(sysAcl,null);
            return result;

        }
    }
}
