package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.HashMap;

public class ExcelParamsBean implements Serializable {

    public String orgName;
    public String num;
    //跳转参数

    public JumpDTOBean jumpDTO;
    public JumpDTOBean drillDownDTO;

    public static class JumpDTOBean implements Serializable {
        public static String TAG = JumpDTOBean.class.getSimpleName();

        //时间筛选参数

        public int actionType;
        public String icon;
        public String requestUrl;
        public Object paramMap;
        public HashMap<String,String> params;
    }
}
