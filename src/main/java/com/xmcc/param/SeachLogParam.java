package com.xmcc.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeachLogParam {
    public  Integer type;
    public String beforeSeg;
    public String afterSeg;
    public String operator;
    public String fromTime;
    public String toTime;
}
