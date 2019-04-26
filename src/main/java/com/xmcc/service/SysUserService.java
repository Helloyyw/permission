package com.xmcc.service;

import com.xmcc.beans.PageBeans;
import com.xmcc.dao.SysUserMapper;
import com.xmcc.exception.ParamException;
import com.xmcc.model.SysUser;
import com.xmcc.param.AddUserParam;
import com.xmcc.param.UserParam;
import com.xmcc.utils.BeanValidator;
import com.xmcc.utils.IpUtil;
import com.xmcc.utils.Md5Util;
import com.xmcc.utils.RequestHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysUserService {
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;
//登录验证
    public SysUser checkLogin(UserParam userParam) {
        //验证参数
        BeanValidator.check(userParam);
        //获取用户名密码查询数据库
        SysUser sysUser = sysUserMapper.findByName(userParam.getUsername());
        System.out.println(sysUser + "=======================================");
        String errMsg = "";
        if (sysUser == null) {
            errMsg = "用户不存在";
            //把用户输入的密码转码后和数据库里面的密码进行对比成功了就登录
        } else if (!(sysUser.getPassword().equals(Md5Util.encrypt(userParam.getPassword())))) {
            errMsg = "密码错误";
            System.out.println(sysUser + "=======================================");
        } else if (sysUser.getStatus() != 1) {
            errMsg = "用户异常";
        } else {
            //成功登陆

            return sysUser;
        }
        //如果没有进入else则抛出参数异常的信息
        throw new ParamException(errMsg);
    }

    //根据部门id来实现分页查询用户
    @RequestMapping("/page.json")
    @ResponseBody
    public PageBeans<SysUser> pagesByDeptId(int deptId, PageBeans<SysUser> page) {
        //验证参数
        BeanValidator.check(page);
        //根据当前部门id查询该部门下的用户是否存在
        int count = sysUserMapper.findByDpetId(deptId);//就是用户的总条数
        System.out.println("用户的总条数" + count);
        if (count > 0) { //该部门下存在有用户
            //创建一个分页对象
            PageBeans<SysUser> pageBean = new PageBeans<>();
            //把查询的分页对象存入集合中
            List<SysUser> sysUsers = sysUserMapper.findBydeptIdAandLimt(deptId, page);
            //把集合加入到分页的对象中
            pageBean.setData(sysUsers);
            pageBean.setTotal(count);
            return pageBean;
        }
        return new PageBeans<SysUser>();
    }

    //添加用户功能
    public int addUser(AddUserParam addUserParam) {
        //参数验证
        BeanValidator.check(addUserParam);
        System.out.println("参数验证成功！");
        //判断是否存在相同的用户名
        SysUser oldUser = sysUserMapper.findByNameId(addUserParam.getUsername(),addUserParam.getId());
        if (oldUser == null) {
            //把addUserParam转换成sysUser对象
            SysUser newUser = SysUser.builder().id(addUserParam.getId()).mail(addUserParam.getMail())
                    .deptId(addUserParam.getDeptId()).remark(addUserParam.getRemark())
                    .status(addUserParam.getStatus()).telephone(addUserParam.getTelephone()).username(addUserParam.getUsername()).build();
            newUser.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
            newUser.setOperateTime(new Date());
            newUser.setOperator(RequestHolder.getSysUserHodler().getUsername());
            newUser.setPassword(Md5Util.encrypt("12345678"));
            //保存用户的信息
            sysUserMapper.insert(newUser);
            //记录日志信息
            sysLogService.saveUserLog(null,newUser);
         //邮件开始发送初始化以后的密码
        String emailFrom ="15696049710@163.com";
         String toEmail = newUser.getMail();
                SimpleMailMessage message = new SimpleMailMessage();
                //发件人的邮箱地址
                message.setFrom(emailFrom);
                //收件人的邮箱地址
                message.setTo(toEmail);
                //邮件主题
                message.setSubject("余银万的测试数据：");
                //邮件内容
                message.setText("你好，你的初始化密码是:"+newUser.getPassword());
                //发送邮件
                javaMailSender.send(message);
                System.out.println("发送成功");
            return 1;
        }
        return -1;
    }

    /**
     * 修改用户
     */
    public int UpdateUser(AddUserParam addUserParam) {
        //参数验证
       BeanValidator.check(addUserParam);
       //判断是否存在相同的用户名
        SysUser checkUserName = sysUserMapper.findByNameId(addUserParam.getUsername(),addUserParam.getId());
        //待更新的用户对象
        SysUser oldUser = sysUserMapper.selectByPrimaryKey(addUserParam.getId());
        if (checkUserName == null){
            //把addUserParam转换成sysUser对象
            SysUser updateUser = SysUser.builder().id(addUserParam.getId()).mail(addUserParam.getMail())
                    .deptId(addUserParam.getDeptId()).remark(addUserParam.getRemark())
                    .status(addUserParam.getStatus()).telephone(addUserParam.getTelephone()).username(addUserParam.getUsername()).build();
            updateUser.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
            updateUser.setOperateTime(new Date());
            updateUser.setOperator(RequestHolder.getSysUserHodler().getUsername());
            updateUser.setPassword(Md5Util.encrypt("12345678"));
            System.out.println("带更新的用户对象：   "+updateUser);
           int result = sysUserMapper.updateByPrimaryKey(updateUser);
           //记录日志信息
            sysLogService.saveUserLog(oldUser,updateUser);
            return result;
        }
        return -1;
    }
}
