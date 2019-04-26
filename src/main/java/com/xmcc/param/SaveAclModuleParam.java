package com.xmcc.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveAclModuleParam {
    private Integer id;
    @NotBlank(message = "权限名不可以为空")
    @Length(max = 15,min = 2,message = "长度需要在2-15个字之间")
    private String name;
    private Integer parentId;
    @NotNull(message = "不可以为空")
    private Integer seq;
    @Length(max = 150,message = "长度需要在150个字以内")
    private String remark;
    @NotNull(message = "状态，1：正常，0：冻结")
    private Integer status = 1;
}
