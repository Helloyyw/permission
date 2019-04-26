package com.xmcc.controller;

import com.xmcc.beans.PageBeans;
import com.xmcc.model.SysLog;
import com.xmcc.param.SeachLogParam;
import com.xmcc.service.SysLogService;
import com.xmcc.utils.JsonData;
import com.xmcc.utils.JsonMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView  page(){
        return new ModelAndView("log");
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData pageList(SeachLogParam seachLogParam , PageBeans<SysLog> page){
        PageBeans<SysLog> sysList = sysLogService.SearchPageList(seachLogParam,page);
        return JsonData.success(sysList);
    }

    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData rocover11(int id){
        System.out.println(id+"===============");
        sysLogService.recover(id);
        return JsonData.success();
    }
}
