package com.xmcc.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SeachLogDto {
    public  Integer type;
    public String beforeSeg;
    public String afterSeg;
    public String operator;
    public Date fromTime;//yyyy-MM-dd HH-mm-ss
    public Date toTime;
}
