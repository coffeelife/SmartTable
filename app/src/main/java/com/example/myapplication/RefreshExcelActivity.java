package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.ExcelView.ExcelParseUtils;
import com.example.myapplication.ExcelView.SmartExcelView;
import com.example.myapplication.bean.ExcelData;
import com.example.myapplication.utils.GsonUtil;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 刷新表格
 */
public class RefreshExcelActivity extends AppCompatActivity {

    private SmartExcelView excelLand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_land);
        initView();
        initDatas();
    }

    private void initView() {
        excelLand = findViewById(R.id.excel_land);
        excelLand.smartRl.setEnableRefresh(true);
        excelLand.smartRl.setEnableLoadMore(true);
        excelLand.smartRl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initDatas();
            }
        });
        excelLand.smartRl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                addData();

            }
        });
    }

    private void initDatas() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(), "land.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelLand != null) excelLand.showExcel(formData);
        if (excelLand != null && excelLand.smartRl != null) excelLand.smartRl.finishRefresh();
    }

    private void addData() {
        String json = GsonUtil.getOriginalFundData(getBaseContext(), "land.json");
        JsonObject jsonObject = GsonUtil.getRootJsonObject(json);
        ExcelData data = GsonUtil.JsonObjectToBean(jsonObject, ExcelData.class);
        ExcelParseUtils.ExcelFormData formData = ExcelParseUtils.parse(data);
        if (excelLand != null) excelLand.addExcelData(formData);
        if (excelLand != null && excelLand.smartRl != null) excelLand.smartRl.finishLoadMore();
    }

}
