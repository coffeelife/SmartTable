package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.myapplication.adapter.MenuGridAdapter;
import com.example.myapplication.bean.MenuResBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView gvMenu;
    private MenuGridAdapter menuAdapter;
    private String[] menuArr = {"竖向表格", "横向表格", "带刷新表格", "固定列表格", "多行表格"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();

    }

    private void initView() {
        gvMenu = findViewById(R.id.gv_menu);
        final List<MenuResBean.CommonAppListBean> menus = new ArrayList<>();
        for (int i = 0; i < menuArr.length; i++) {
            MenuResBean.CommonAppListBean bean = new MenuResBean.CommonAppListBean();
            bean.setName(menuArr[i]);
            bean.setType(i);
            menus.add(bean);
        }

        menuAdapter = new MenuGridAdapter(menus, getBaseContext());
        gvMenu.setAdapter(menuAdapter);
        gvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),menus.get(position).getName(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDatas() {
    }


}
