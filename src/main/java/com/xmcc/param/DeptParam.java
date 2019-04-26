package com.xmcc.param;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class DeptParam {

    private  Integer id;
    @NotBlank(message = "名字不可以为空")
    @Length(max=15,min = 2,message = "长度需要在2-15个字之间")
    private String name;
    private  Integer parentId = 0;//让它默认表示第一层
    @NotNull(message = "不可以为空")
    private Integer  seq;
    @Length(max=150,message = "长度需要在150个字以内")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
