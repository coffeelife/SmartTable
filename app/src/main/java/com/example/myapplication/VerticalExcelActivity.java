package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.ExcelView.ExcelParseUtils;
import com.example.myapplication.ExcelView.SmartExcelView;
import com.example.myapplication.bean.ExcelData;
import com.example.myapplication.utils.GsonUtil;
import com.google.gson.JsonObject;

import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 竖向表格
 */
public class VerticalExcelActivity extends AppCompatActivity {
    private SmartExcelView excelVertical,excelVertical2,excelVertical3,excelVertical4,excelVertical5,excelVertical6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_vertical);
        initView();
        initDatas();
        initDatas2();
        initDatas3();
        initDatas4();
        initDatas5();
        initDatas6();
    }

    private void initView() {
        excelVertical = findViewById(R.id.view_excel);
        excelVertical2 = findViewById(R.id.view_excel2);
        excelVertical3 = findViewById(R.id.view_excel3);
        excelVertical4 = findViewById(R.id.view_excel4);
        excelVertical5 = findViewById(R.id.view_excel5);
        excelVertical6 = findViewById(R.id.view_excel6);
    }

    private void initDatas() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        excelVertical.showVerticalExcel(formData);
    }

    private void initDatas2() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical2.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        excelVertical2.showVerticalExcel(formData);
    }

    private void initDatas3() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical3.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        excelVertical3.showVerticalExcel(formData);
    }

    private void initDatas4() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical4.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        excelVertical4.showVerticalExcel(formData);
    }

    private void initDatas5() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical5.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        excelVertical5.showVerticalExcel(formData);
    }

    private void initDatas6() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical6.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        excelVertical6.showVerticalExcel(formData);
    }
}
