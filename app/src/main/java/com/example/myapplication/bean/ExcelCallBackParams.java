package com.example.myapplication.bean;

import java.io.Serializable;

public class ExcelCallBackParams implements Serializable {
    public String levelNum;
    public int positon;
    public String value;
    public String itemId;
    public String itemName;
    public String bussnessId;
    public String orgName;
    public String action;
    public String s;//excel默认s返回值
    public String businessName;
    public ExcelParamsBean.JumpDTOBean jumpDTOBean;
}
