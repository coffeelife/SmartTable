package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager;

import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.example.myapplication.ExcelView.ExcelParseUtils;
import com.example.myapplication.ExcelView.SmartExcelView;
import com.example.myapplication.bean.ExcelCallBackParams;
import com.example.myapplication.bean.ExcelData;
import com.example.myapplication.utils.GsonUtil;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.header.DropBoxHeader;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
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
        MaterialHeader header = new MaterialHeader(getBaseContext());

        excelLand.smartRl.setRefreshHeader(header);
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
        excelLand.setOnItemClickListener(new SmartExcelView.OnItemClickListener() {
            @Override
            public void onTitleItemClick(ColumnInfo columnInfo, int position, String key, int isReverse) {

            }

            @Override
            public void onImageItemClick(Column<String> column, ExcelCallBackParams imageCallBackParams) {
                Intent intent2 = new Intent(getBaseContext(), LandExcelActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent2);
            }

            @Override
            public void onColumnItemClick(Column column, ExcelCallBackParams callBackParams) {
                Intent intent2 = new Intent(getBaseContext(), LandExcelActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent2);
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
        excelLand.setOnItemClickListener(new SmartExcelView.OnItemClickListener() {
            @Override
            public void onTitleItemClick(ColumnInfo columnInfo, int position, String key, int isReverse) {

            }

            @Override
            public void onImageItemClick(Column<String> column, ExcelCallBackParams imageCallBackParams) {
                Intent intent2 = new Intent(getBaseContext(), LandExcelActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent2);
            }

            @Override
            public void onColumnItemClick(Column column, ExcelCallBackParams callBackParams) {
                Intent intent2 = new Intent(getBaseContext(), LandExcelActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent2);
            }
        });
    }

}
