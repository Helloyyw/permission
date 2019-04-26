package com.xmcc.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserParam  {

    private  Integer id;
    @NotBlank(message = "用户名不可以为空")
    @Length(max = 20,min = 1,message = "用户名长度需要在20个字以内")
    private String username;
    private String password;
//    @NotBlank(message = "电话不可以为空")
//    @Length(max = 13,min = 1,message = "电话长度需要在13个字以内" )
    private String telephone;
//    @Length(max = 200,message = "备注长度需要在200个字以内" )
    private  String remark ;
    private  Integer deptId;
   @NotBlank(message = "必须指定用户的状态")
    private  String  status="1";
//    @NotBlank(message = "邮箱不可以为空")
//    @Length(max = 50,min = 1,message = "邮箱长度需要在50个字符以内" )
    private  String  mail;

}
