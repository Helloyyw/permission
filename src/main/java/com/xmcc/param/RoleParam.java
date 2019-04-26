package com.xmcc.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleParam {
    private  Integer id;
    @NotBlank(message = "不可以为空")
    @Length(min = 2,max = 20,message = "称长度需要在2-20个字之间")
    private String name;
    @NotBlank(message = "长度需要在150个字以内")
    private  String remark;
    private  Integer status=1;
    private  Integer type=1;

}
