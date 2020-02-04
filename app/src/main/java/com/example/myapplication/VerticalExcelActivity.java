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
    private SmartExcelView excelVertical,excelVertical2,excelVertical3,excelVertical4,excelVertical5,excelVertical6,excelVertical7;

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
        initDatas7();
    }

    private void initView() {
        excelVertical = findViewById(R.id.view_excel);
        excelVertical2 = findViewById(R.id.view_excel2);
        excelVertical3 = findViewById(R.id.view_excel3);
        excelVertical4 = findViewById(R.id.view_excel4);
        excelVertical5 = findViewById(R.id.view_excel5);
        excelVertical6 = findViewById(R.id.view_excel6);
        excelVertical7 = findViewById(R.id.view_excel7);
    }

    private void initDatas() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelVertical != null) excelVertical.showVerticalExcel(formData);
    }

    private void initDatas2() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical2.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelVertical2 != null)  excelVertical2.showVerticalExcel(formData);
    }

    private void initDatas3() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical3.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelVertical3 != null)  excelVertical3.showVerticalExcel(formData);
    }

    private void initDatas4() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical4.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelVertical4 != null)  excelVertical4.showVerticalExcel(formData);
    }

    private void initDatas5() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical5.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelVertical5 != null)  excelVertical5.showVerticalExcel(formData);
    }

    private void initDatas6() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical6.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelVertical6 != null)  excelVertical6.showVerticalExcel(formData);
    }

    private void initDatas7() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(),"vertical6.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelVertical7 != null)  excelVertical7.showVerticalExcel(formData);
    }
}
