package com.xmcc.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AclParam {

    private  Integer id;
    @NotBlank(message = "不可以为空")
    @Length(min = 2,max = 20,message = "称长度需要在2-20个字之间")
    private String name;
    private  Integer aclModuleId;
    private  Integer seq;
    @Length(max = 150,message = "称长度需要在150个字之间")
    private String remark ;
    @Length(max = 60,min = 6,message = "长度需要在6-100个字符之间")
    private  String url;
    private  Integer status;
    private  Integer type;

}
