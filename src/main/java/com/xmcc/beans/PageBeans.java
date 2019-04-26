package com.xmcc.beans;

import com.xmcc.model.SysUser;
import lombok.Getter;
import lombok.Setter;
import sun.awt.SunHints;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PageBeans<T> {
    @Setter
    @Getter
    @Min(value = 0, message = "当前页面不合法")
    private int pageNo =1;//页码
    @Setter
    @Getter
    @Min(value = 1, message = "大于等于1")
    private int pageSize =10;//每页最多放的条数

    private int offset =1;//每次查询的起点
    @Setter
    @Getter
    @Min(value = 0, message = "大于等于1")
    private int total = 0;//总条数

    public int getOffset() {

        return (pageNo-1)*pageSize;
    }
    public void setOffset(int offset)
    {

        this.offset = offset;
    }
    //存放列表集合
    @Setter
    @Getter
    private List<T> data = new ArrayList<>();


}
