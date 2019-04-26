package com.xmcc.controller;

import com.xmcc.dto.SysDeptLevelDto;
import com.xmcc.param.DeptParam;
import com.xmcc.service.SysDeptService;
import com.xmcc.service.SysTreeService;
import com.xmcc.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/dept")
public class SysDeptContorller {
    @Resource
    private SysTreeService sysTreeService;

    @Resource
    private SysDeptService sysDeptService;

    /**
     * 登录主页面就加载部门表
     *
     * @return
     */
    @RequestMapping("/dept.page")
    public ModelAndView login() {

        return new ModelAndView("dept");
    }

    /**
     * 添加部门信息存入数据库
     *
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(DeptParam deptParam) {
        sysDeptService.save(deptParam);
        return JsonData.success();
    }

    /**
     * 查询部门表生成部门树结构
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData dodeptTree() {
        List<SysDeptLevelDto> deptList = sysTreeService.deptTree();
        return JsonData.success(deptList);
    }

    /**
     * 更新操作部门树
     *
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDeptTree(DeptParam deptParam) {
            if (sysDeptService.update(deptParam) != 1) {
            return JsonData.fail("部门名称长度需要在2-15个字之间");
        }
        return JsonData.success();
    }
    /**
     * 删除操作
     *
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteDept(int id) {
        sysDeptService.deleteDept(id);
        return JsonData.success();
    }


}
