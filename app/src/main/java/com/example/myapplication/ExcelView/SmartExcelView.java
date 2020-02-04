package com.example.myapplication.ExcelView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.draw.BitmapDrawFormat;
import com.bin.david.form.data.format.draw.FirTextDrawFormat;
import com.bin.david.form.data.format.draw.MultiLineDrawFormat;
import com.bin.david.form.data.format.draw.MultiLineDrawFormatNew;
import com.bin.david.form.data.format.draw.TextImageDrawFormat;
import com.bin.david.form.data.format.draw.TextWithColorDrawFormatNew;
import com.bin.david.form.data.format.grid.BaseAbstractGridFormat;
import com.bin.david.form.data.format.title.MTitleImageDrawFormat;
import com.bin.david.form.data.format.title.TitleImageDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.ArrayTableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.R;
import com.example.myapplication.bean.ExcelCallBackParams;
import com.example.myapplication.bean.ExcelParamsBean;
import com.example.myapplication.utils.ArrayUtils;
import com.example.myapplication.utils.BeanCloneUtil;
import com.example.myapplication.utils.DeviceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author gm
 * @apiNote mColors 字体颜色集合
 * @apiNote tableData tableData数据对象
 * @apiNote deafaultTextSize 默认字体大小 mScreenWidth 屏幕宽度 firColumnWdith 排序列宽度（第一列）secColumnWdith 名称列宽度（第二列）
 * @apiNote thirdColumnWdith 图标列宽度（第三列）columnTitleHorizontalPadding Title列左右边距 columnTitleVerticalPadding Title列上下边距
 * @apiNote verticalPadding内容列上下边距 horizontalPadding内容列左右边距 titleImageDrPadding Title列排序图标距文字边距
 * @apiNote secDpWidth 第二列名称列宽度值 txtLength 第二列名称列文字每行最大数量
 */
public class SmartExcelView extends FrameLayout {
    private SmartTable<String> table;
    private SmartTable<String> tableLand;
    private TextView tvTitle;
    private TextView tvBottom;
    private View layoutTitle;
    public LinearLayout llLand;
    private View titleView;
    public LinearLayout llMore;
    public LinearLayout llTableLand;
    public LinearLayout llTableVertical;
    private Context mContext;
    public SmartRefreshLayout smartRl;
    private int deafaultTextSize = 15, mScreenWidth = 0, firColumnWdith = 0,
            secColumnWdith = 0, thirdColumnWdith = 0, columnTitleHorizontalPadding = 0, columnTitleVerticalPadding = 0,
            verticalPadding = 0, horizontalPadding = 0, titleImageDrPadding = 0, secDpWidth = 120, txtlength = 10,
            columnW = 0, maxLength = 0, maxSecColWdith = 0;
    private int mColors[] = new int[3];
    private int mTitleColors[] = new int[2];
    private int mActionColor, mColor333, mColorCCF8F8F8, mColorCCFFF, mColorTrans;
    private ArrayTableData<String> tableData;//tableData数据对象
    private boolean isAuto = false;//是否标题小于5自动适配自动
    private ExcelParseUtils.ExcelFormData mExcelFormData;
    private boolean isVertical = false;
    Map<String, Bitmap> map = null;

    /**
     * 事件回调监听
     */
    public OnItemClickListener onItemClickListener;

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public SmartExcelView(@NonNull Context context) {
        super(context);
        init();
    }

    public SmartExcelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmartExcelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        //颜色初始化赋值
        mColors[0] = mContext.getResources().getColor(R.color.color_333333);
        mColors[1] = mContext.getResources().getColor(R.color.color_40BE95);
        mColors[2] = mContext.getResources().getColor(R.color.color_red_fa6469);
        mActionColor = mContext.getResources().getColor(R.color.color_1990FF);
        mColor333 = mContext.getResources().getColor(R.color.color_333333);
        mColorCCF8F8F8 = mContext.getResources().getColor(R.color.bg_color_CCF8F8F8);
        mColorCCFFF = mContext.getResources().getColor(R.color.color_ccffffff);
        mColorTrans = mContext.getResources().getColor(R.color.transparent);
//        mContext.getResources().getColor(R.color.color_1990FF)
        mTitleColors[0] = mColorCCFFF;
        mTitleColors[1] = mColorCCF8F8F8;
        mScreenWidth = DeviceUtil.getDeviceWidth(mContext);
        //初始化控件
        //初始化ExcelView布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_excel_new, this);
        llTableVertical = view.findViewById(R.id.ll_table_vertical);
        llTableLand = view.findViewById(R.id.ll_table_land);
        table = view.findViewById(R.id.form_excel);
        tvTitle = view.findViewById(R.id.tv_title);
        tvBottom = view.findViewById(R.id.tv_bottom);
        titleView = view.findViewById(R.id.title_view);
        layoutTitle = view.findViewById(R.id.layout_title);
        llMore = view.findViewById(R.id.ll_more);
        llLand = view.findViewById(R.id.ll_land);
        tableLand = view.findViewById(R.id.form_excel_land);
        smartRl = view.findViewById(R.id.smart_rl);
        llTableVertical.setVisibility(GONE);
        llTableLand.setVisibility(GONE);
        initCommonTableConfig();
    }

    private void initCommonTableConfig() {
        FontStyle.setDefaultDpTextSize(mContext, deafaultTextSize);
        FontStyle.setDefaultTextColor(mColor333);
        FontStyle titleFontStyle = new FontStyle();
        titleFontStyle.setDpTextSize(mContext, deafaultTextSize);
        titleFontStyle.setTextColor(mColor333);
        table.getConfig().setColumnTitleStyle(titleFontStyle);
        table.getConfig().setShowTableTitle(false);
        table.getConfig().setShowXSequence(false);//隐藏行序列
        table.getConfig().setShowYSequence(false);//隐藏类序列
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(mColorCCF8F8F8));
        table.getConfig().setColumnTitleHorizontalPadding(columnTitleHorizontalPadding);
        table.getConfig().setColumnTitleVerticalPadding(columnTitleVerticalPadding);
        table.getConfig().setVerticalPadding(verticalPadding);
        table.getConfig().setHorizontalPadding(horizontalPadding);
        table.getConfig().setMinTableWidth(mScreenWidth);
        table.setBackgroundColor(mColorTrans);
        table.getConfig().setTableTitleBgColors(mTitleColors);
        table.setBackgroundColor(mColorTrans);
        table.getConfig().setTableTitleBgColors(mTitleColors);

        tableLand.getConfig().setColumnTitleStyle(titleFontStyle);
        tableLand.getConfig().setShowTableTitle(false);
        tableLand.getConfig().setShowXSequence(false);//隐藏行序列
        tableLand.getConfig().setShowYSequence(false);//隐藏类序列
        tableLand.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(mColorCCF8F8F8));
        tableLand.getConfig().setColumnTitleHorizontalPadding(columnTitleHorizontalPadding);
        tableLand.getConfig().setColumnTitleVerticalPadding(columnTitleVerticalPadding);
        tableLand.getConfig().setVerticalPadding(verticalPadding);
        tableLand.getConfig().setHorizontalPadding(horizontalPadding);
        tableLand.getConfig().setMinTableWidth(mScreenWidth);
        tableLand.getConfig().setTableTitleBgColors(mTitleColors);
        tableLand.setBackgroundColor(mColorTrans);
        tableLand.getConfig().setTableTitleBgColors(mTitleColors);
    }

    /**
     * 设置通用excel 默认显示标题和不滚动 外部调取方法
     *
     * @param excelFormData 表格数据
     */
    public void showExcel(ExcelParseUtils.ExcelFormData excelFormData) {
        if (isVertical)
            showVerticalExcel(excelFormData);
        else
            showHorizontalExcel(excelFormData);
    }

    /**
     * 设置通用excel 默认显示标题和不滚动 外部调取方法
     *
     * @param excelFormData 表格数据
     * @param isVertical    是否是竖屏
     */
    public void showExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean isVertical) {
        if (isVertical)
            showVerticalExcel(excelFormData);
        else
            showHorizontalExcel(excelFormData);
    }

    /**
     * 设置通用excel 默认显示标题和不滚动 外部调取方法
     *
     * @param excelFormData 表格数据
     * @param hasTitle      是否有标题
     * @param isVertical    是否是竖屏
     */
    public void showExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean hasTitle, boolean isVertical) {
        if (isVertical)
            showVerticalExcel(excelFormData, hasTitle);
        else
            showHorizontalExcel(excelFormData);
    }

    /**
     * 设置通用excel 默认显示标题和不滚动 外部调取方法
     *
     * @param excelFormData     表格数据
     * @param hasTitle          是否有标题
     * @param canScrollVertical 是否可以滚动
     * @param isVertical        是否是竖屏
     */
    public void showExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean hasTitle, boolean canScrollVertical, boolean isVertical) {
        if (isVertical)
            showVerticalExcel(excelFormData, hasTitle, canScrollVertical);
        else
            showHorizontalExcel(excelFormData, canScrollVertical);
    }

    /**
     * 设置竖屏excel 默认显示标题和不滚动 外部调取方法
     *
     * @param excelFormData 表格数据
     */
    public void showVerticalExcel(ExcelParseUtils.ExcelFormData excelFormData) {
        showVerticalExcel(excelFormData, true);
    }

    /**
     * 设置竖屏excel 默认不滚动 外部调取方法
     *
     * @param hasTitle      是否显示标题
     * @param excelFormData 表格数据
     */
    public void showVerticalExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean hasTitle) {
        showVerticalExcel(excelFormData, hasTitle, false);
    }

    /**
     * 设置竖屏excel 外部调取方法
     *
     * @param hasTitle          是否显示标题
     * @param canScrollVertical 是否可以滚动
     * @param excelFormData     表格数据
     */
    public void showVerticalExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean hasTitle, boolean canScrollVertical) {
        llTableVertical.setVisibility(VISIBLE);
        llTableLand.setVisibility(GONE);
        layoutTitle.setVisibility(hasTitle ? VISIBLE : GONE);
        tvTitle.setText(excelFormData.tableName);
        tvBottom.setText("查看完整数据");
        isVertical = true;

        if (excelFormData != null) {
            mExcelFormData = BeanCloneUtil.cloneTo(excelFormData);
            if (mExcelFormData.isDetails) {//判断是否有图标列
                if (excelFormData.titles.length <= 4) {//如果小于等于4个，自适配宽度
                    isAuto = true;
                } else {//使用普通Excel
                    isAuto = false;
                }
            } else {
                if (excelFormData.titles.length <= 5) {//如果小于等于5个，自适配宽度
                    isAuto = true;
                } else {//使用普通Excel
                    isAuto = false;
                }
            }

            initVerticalExcel(excelFormData, canScrollVertical);
        }
    }

    /**
     * 设置竖屏excel方法 内部方法
     *
     * @param canScrollVertical 是否可以滚动
     * @param excelFormData     表格数据
     */
    private void initVerticalExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean canScrollVertical) {
        firColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 40);
        secColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 70);
        columnTitleHorizontalPadding = (int) DeviceUtil.dipToPx(getContext(), isAuto ? 0 : 5);//如果是自动不设置padding值
        columnTitleVerticalPadding = (int) DeviceUtil.dipToPx(getContext(), 12);
        verticalPadding = (int) DeviceUtil.dipToPx(getContext(), 12);
        horizontalPadding = (int) DeviceUtil.dipToPx(getContext(), isAuto ? 0 : 5);
        titleImageDrPadding = (int) DeviceUtil.dipToPx(getContext(), 0);
        if (mExcelFormData.isDetails) {
            thirdColumnWdith = DensityUtils.dp2px(getContext(), 20);
        }
        initVerticalTableConfig(canScrollVertical);
        setVerticalData();
    }

    /**
     * 初始化竖屏Excel布局属性
     *
     * @param canScrollVertical 是否可以滚动
     */
    private void initVerticalTableConfig(boolean canScrollVertical) {
        table.getConfig().setCanScrollVertical(canScrollVertical);//设置竖屏是否可以滚动
        table.getConfig().setColumnTitleHorizontalPadding(columnTitleHorizontalPadding);
        table.getConfig().setColumnTitleVerticalPadding(columnTitleVerticalPadding);
        table.getConfig().setVerticalPadding(verticalPadding);
        table.getConfig().setHorizontalPadding(horizontalPadding);

        //设置网格
        table.getConfig().setTableGridFormat(new BaseAbstractGridFormat() {
            @Override
            protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {
                return cellInfo.column.isShowColumnTitleVerticalLine();
            }

            @Override
            protected boolean isShowHorizontalLine(int col, int row, CellInfo cellInfo) {
                return false;
            }

            @Override
            protected boolean isShowColumnTitleVerticalLine(int col, Column column) {
                return column.isShowColumnTitleVerticalLine();
            }
        });

        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return mColorCCFFF;
                }
                return mColorCCF8F8F8;
            }
        };
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
    }

    List<String> titleListVertical;
    List<Integer> actionsVetical;
    /**
     * 设置竖屏表格数据
     */
    private void setVerticalData() {
        if (ArrayUtils.isEmpty(mExcelFormData.titles)) {
            return;
        }
        if (tableData != null)
            tableData.clear();
        String[] tempTitles;
        String[][] tempDatas;
        if (mExcelFormData.isSort) {//数据添加一列排序列
            if (mExcelFormData.isDetails) {
                tempTitles = ExcelParseUtils.addTableDetailsTitles(mExcelFormData.titles, true, 4);
                if (mExcelFormData.isFixed) {
                    if (mExcelFormData.ascendingOrder == 2)
                        tempDatas = ExcelParseUtils.addTableDetailsDatas(ExcelParseUtils.addTableDatas(mExcelFormData.titleDatas, mExcelFormData.dataArray));
                    else
                        tempDatas = ExcelParseUtils.addTableDetailsDatasRev(ExcelParseUtils.addTableDatas(mExcelFormData.titleDatas, mExcelFormData.dataArray), mExcelFormData.totalNum);
                } else {
                    if (mExcelFormData.ascendingOrder == 2)
                        tempDatas = ExcelParseUtils.addTableDetailsDatas(mExcelFormData.dataArray);
                    else
                        tempDatas = ExcelParseUtils.addTableDetailsDatasRev(mExcelFormData.dataArray, mExcelFormData.totalNum);
                }
                if (tempDatas != null && tempDatas[1] != null) {
                    for (int i = 0; i < tempDatas[1].length; i++) {
                        if (tempDatas[1][i].length() > 8)
                            tempDatas[1][i] = tempDatas[1][i].substring(0, 7) + "…";
                        if (tempDatas[1][i].length() > 4)
                            tempDatas[1][i] = ExcelParseUtils.titleFormat(tempDatas[1][i], 4);
                    }
                }
            } else {
                tempTitles = ExcelParseUtils.addTableTitles(mExcelFormData.titles, true, 4);
                if (mExcelFormData.isFixed) {
                    if (mExcelFormData.ascendingOrder == 2)
                        tempDatas = ExcelParseUtils.addTableDatas(mExcelFormData.titleDatas, mExcelFormData.dataArray);
                    else
                        tempDatas = ExcelParseUtils.addTableDatasRev(mExcelFormData.titleDatas, mExcelFormData.dataArray, mExcelFormData.totalNum);
                } else {
                    if (mExcelFormData.ascendingOrder == 2)
                        tempDatas = ExcelParseUtils.addTableDatas(mExcelFormData.dataArray);
                    else
                        tempDatas = ExcelParseUtils.addTableDatasRev(mExcelFormData.dataArray, mExcelFormData.totalNum);
                }
                if (tempDatas != null && tempDatas[1] != null) {
                    for (int i = 0; i < tempDatas[1].length; i++) {
                        if (tempDatas[1][i].length() > 8)
                            tempDatas[1][i] = tempDatas[1][i].substring(0, 7) + "…";
                        if (tempDatas[1][i].length() > 4)
                            tempDatas[1][i] = ExcelParseUtils.titleFormat(tempDatas[1][i], 4);
                    }
                }
            }
        } else {
            if (mExcelFormData.isDetails) {
                tempTitles = ExcelParseUtils.addTableNoSortDetailsTitles(mExcelFormData.titles, true, 4);
                if (mExcelFormData.isFixed) {
                    tempDatas = ExcelParseUtils.addTableNoSortDetailsDatas(ExcelParseUtils.addTableDatas(mExcelFormData.titleDatas, mExcelFormData.dataArray));
                } else {
                    tempDatas = ExcelParseUtils.addTableNoSortDetailsDatas(mExcelFormData.dataArray);
                }
                if (tempDatas != null && tempDatas[0] != null) {
                    for (int i = 0; i < tempDatas[0].length; i++) {
                        if (tempDatas[0][i].length() > 8)
                            tempDatas[0][i] = tempDatas[0][i].substring(0, 7) + "…";
                        if (tempDatas[0][i].length() > 4)
                            tempDatas[0][i] = ExcelParseUtils.titleFormat(tempDatas[0][i], 4);
                    }
                }
            } else {
                tempTitles = ExcelParseUtils.addTableNoSortTitles(mExcelFormData.titles, true, 4);
                if (mExcelFormData.isFixed) {
                    tempDatas = ExcelParseUtils.addTableNoSortDatas(mExcelFormData.titleDatas, mExcelFormData.dataArray);
                } else {
                    tempDatas = ExcelParseUtils.addTableNoSortDatas(mExcelFormData.dataArray);
                }
                if (tempDatas != null && tempDatas[0] != null) {
                    for (int i = 0; i < tempDatas[0].length; i++) {
                        if (tempDatas[0][i].length() > 8)
                            tempDatas[0][i] = tempDatas[0][i].substring(0, 7) + "…";
                        if (tempDatas[0][i].length() > 4)
                            tempDatas[0][i] = ExcelParseUtils.titleFormat(tempDatas[0][i], 4);
                    }
                }
            }
        }
        titleListVertical = Arrays.asList(tempTitles);
        tableData = ArrayTableData.create(mExcelFormData.tableName, tempTitles, tempDatas, null);

        actionsVetical = new ArrayList<>();
        if (mExcelFormData.isFixed) {
            if (mExcelFormData.headParamsBean != null && mExcelFormData.headParamsBean.drillDownDTO != null)
                actionsVetical.add(mExcelFormData.headParamsBean.drillDownDTO.actionType);
            else
                actionsVetical.add(0);
        }
        if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans)) {
            for (int i = 0; i < mExcelFormData.paramsBeans.size(); i++) {
                ExcelParamsBean bean = mExcelFormData.paramsBeans.get(i);
                if (bean != null && bean.drillDownDTO != null) {
                    actionsVetical.add(bean.drillDownDTO.actionType);
                } else {
                    actionsVetical.add(0);
                }
            }
        }
        if (isAuto) {
            if (mExcelFormData.isSort) {
                if (mExcelFormData.isDetails) {
                    if (mExcelFormData.titles.length > 1)
                        columnW = (mScreenWidth - firColumnWdith - secColumnWdith - thirdColumnWdith) / (mExcelFormData.titles.length - 1);
                } else {
                    if (mExcelFormData.titles.length > 1)
                        columnW = (mScreenWidth - firColumnWdith - secColumnWdith) / (mExcelFormData.titles.length - 1);
                }
            } else {
                secColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 80);
                if (mExcelFormData.isDetails) {
                    if (mExcelFormData.titles.length > 1)
                        columnW = (mScreenWidth - firColumnWdith - secColumnWdith - thirdColumnWdith) / (mExcelFormData.titles.length - 1);
                } else {
                    if (mExcelFormData.titles.length > 1)
                        columnW = (mScreenWidth - secColumnWdith) / (mExcelFormData.titles.length - 1);
                }
            }
        }

        List<Column<String>> columns = tableData.getArrayColumns();
        map = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            Column<String> curColumn = columns.get(i);
            if (mExcelFormData.isSort) {
                if (mExcelFormData.isDetails) {
                    if (i == 0) {
                        curColumn.setDrawFormat(new FirTextDrawFormat<String>(mContext, firColumnWdith));
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setTextAlign(Paint.Align.RIGHT);
                    } else if (i == 1) {
                        curColumn.setFixed(true);
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.LEFT);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mActionColor, actionsVetical));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsVetical)) {
                                        if (actionsVetical.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            if (mExcelFormData.isFixed) {
                                                if (position == 0)
                                                    params.value = TextUtils.isEmpty(mExcelFormData.titleDatas[0]) ? "" : mExcelFormData.titleDatas[0];
                                                else
                                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position - 1]) ? "" : mExcelFormData.dataArray[0][position - 1];

                                            } else {
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            }

                                            if (mExcelFormData.isFixed) {
                                                if (position == 0) {
                                                    if (mExcelFormData.headParamsBean != null)
                                                        params.jumpDTOBean = mExcelFormData.headParamsBean.drillDownDTO;
                                                } else {
                                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > (position - 1) && mExcelFormData.paramsBeans.get(position - 1) != null)
                                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position - 1).drillDownDTO;
                                                }
                                            } else {
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            }
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                        if (i > 0 && !ArrayUtils.isEmpty(mExcelFormData.isOrders)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(0) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                    } else if (i == 2) {
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setShowColumnTitleVerticalLine(true);
                        int size = DensityUtils.dp2px(getContext(), 15);
                        curColumn.setDrawFormat(new BitmapDrawFormat<String>(size, size) {
                            @Override
                            protected Bitmap getBitmap(final String s, String value, final int position) {
                                //网络图加载方法
//                                if (map.get(s) == null) {
//                                    if (mExcelFormData.paramsBeans != null) {
//                                        ExcelParamsBean bean = mExcelFormData.paramsBeans.get(position);
//                                        if (bean != null && bean.jumpDTO != null) {
//                                            //加载网络图的方法
//                                            Glide.with(mContext).asBitmap()
//                                                    .load(bean.jumpDTO.icon).apply(new RequestOptions().error(R.drawable.icon_menu_default)
//                                                    .placeholder(R.drawable.icon_menu_default))
//                                                    .into(new SimpleTarget<Bitmap>() {
//                                                        @Override
//                                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                                            map.put(s + position, resource);
//                                                            table.invalidate();
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                }
//                                return map.get(s + position);
                                //本地图加载方法
                                return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.biaoqian);
                            }
                        });
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    ExcelCallBackParams params = new ExcelCallBackParams();
                                    params.s = TextUtils.isEmpty(s) ? "" : s;
                                    params.positon = position;
                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).jumpDTO;
                                    onItemClickListener.onImageItemClick(column, params);
                                }
                            }
                        });
                    } else {
                        curColumn.setTitleAlign(Paint.Align.RIGHT);
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        if (isAuto) curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                        if (i > 0 && !ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 2)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i - 2) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                    }
                } else {
                    if (i == 0) {
                        curColumn.setDrawFormat(new FirTextDrawFormat<String>(mContext, firColumnWdith));
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setTextAlign(Paint.Align.RIGHT);
                    } else if (i == 1) {
                        curColumn.setFixed(true);
                        curColumn.setShowColumnTitleVerticalLine(true);
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.LEFT);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mActionColor, actionsVetical));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsVetical)) {
                                        if (actionsVetical.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            if (mExcelFormData.isFixed) {
                                                if (position == 0)
                                                    params.value = TextUtils.isEmpty(mExcelFormData.titleDatas[0]) ? "" : mExcelFormData.titleDatas[0];
                                                else
                                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position - 1]) ? "" : mExcelFormData.dataArray[0][position - 1];

                                            } else {
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            }

                                            if (mExcelFormData.isFixed) {
                                                if (position == 0) {
                                                    if (mExcelFormData.headParamsBean != null)
                                                        params.jumpDTOBean = mExcelFormData.headParamsBean.drillDownDTO;
                                                } else {
                                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > (position - 1) && mExcelFormData.paramsBeans.get(position - 1) != null)
                                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position - 1).drillDownDTO;
                                                }
                                            } else {
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            }
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        curColumn.setTitleAlign(Paint.Align.RIGHT);
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        if (isAuto) curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                    }

                    if (i > 0 && !ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                        curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                    } else {
                        curColumn.setSorted(false);
                    }
                }

            } else {
                if (mExcelFormData.isDetails) {
                    if (!isAuto) secColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 80);
                    if (i == 0) {
                        curColumn.setFixed(true);
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mActionColor, actionsVetical, (int) DeviceUtil.dipToPx(getContext(), 10)));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsVetical)) {
                                        if (actionsVetical.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            if (mExcelFormData.isFixed) {
                                                if (position == 0)
                                                    params.value = TextUtils.isEmpty(mExcelFormData.titleDatas[0]) ? "" : mExcelFormData.titleDatas[0];
                                                else
                                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position - 1]) ? "" : mExcelFormData.dataArray[0][position - 1];

                                            } else {
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            }
                                            if (mExcelFormData.isFixed) {
                                                if (position == 0) {
                                                    if (mExcelFormData.headParamsBean != null)
                                                        params.jumpDTOBean = mExcelFormData.headParamsBean.drillDownDTO;
                                                } else {
                                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > (position - 1) && mExcelFormData.paramsBeans.get(position - 1) != null)
                                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position - 1).drillDownDTO;
                                                }
                                            } else {
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            }
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(0) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                    } else if (i == 1) {
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setShowColumnTitleVerticalLine(true);
                        int size = DensityUtils.dp2px(getContext(), 15);
                        curColumn.setDrawFormat(new BitmapDrawFormat<String>(size, size) {
                            @Override
                            protected Bitmap getBitmap(final String s, String value, final int position) {
                                if (map.get(s) == null) {
                                    if (mExcelFormData.paramsBeans != null) {
                                        ExcelParamsBean bean = mExcelFormData.paramsBeans.get(position);
                                        if (bean != null && bean.jumpDTO != null) {
                                            Glide.with(mContext).asBitmap()
                                                    .load(bean.jumpDTO.icon).apply(new RequestOptions().error(R.drawable.icon_menu_default)
                                                    .placeholder(R.drawable.icon_menu_default))
                                                    .into(new SimpleTarget<Bitmap>() {
                                                        @Override
                                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                            map.put(s + position, resource);
                                                            table.invalidate();
                                                        }
                                                    });
                                        }
                                    }
                                }
                                return map.get(s + position);
                            }
                        });
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    ExcelCallBackParams params = new ExcelCallBackParams();
                                    params.s = TextUtils.isEmpty(s) ? "" : s;
                                    params.positon = position;
                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).jumpDTO;
                                    onItemClickListener.onImageItemClick(column, params);
                                }
                            }
                        });
                    } else {
                        curColumn.setTitleAlign(Paint.Align.RIGHT);
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        if (isAuto) curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                    }
                } else {
                    if (!isAuto) secColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 80);
                    if (i == 0) {
                        curColumn.setFixed(true);
                        curColumn.setShowColumnTitleVerticalLine(true);
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mActionColor, actionsVetical, (int) DeviceUtil.dipToPx(getContext(), 10)));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsVetical)) {
                                        if (actionsVetical.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            if (mExcelFormData.isFixed) {
                                                if (position == 0)
                                                    params.value = TextUtils.isEmpty(mExcelFormData.titleDatas[0]) ? "" : mExcelFormData.titleDatas[0];
                                                else
                                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position - 1]) ? "" : mExcelFormData.dataArray[0][position - 1];

                                            } else {
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            }
                                            if (mExcelFormData.isFixed) {
                                                if (position == 0) {
                                                    if (mExcelFormData.headParamsBean != null)
                                                        params.jumpDTOBean = mExcelFormData.headParamsBean.drillDownDTO;
                                                } else {
                                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > (position - 1) && mExcelFormData.paramsBeans.get(position - 1) != null)
                                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position - 1).drillDownDTO;
                                                }
                                            } else {
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            }
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        curColumn.setTitleAlign(Paint.Align.RIGHT);
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        if (isAuto) curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                    }

                    if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > i) {
                        curColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                    } else {
                        curColumn.setSorted(false);
                    }
                }

            }
        }

        if (!TextUtils.isEmpty(mExcelFormData.orderBy) && !ArrayUtils.isEmpty(mExcelFormData.keys)) {
            int orderPos = 0;
            if (mExcelFormData.isSort) {
                if (mExcelFormData.isDetails)
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 2;
                else
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 1;
            } else {
                if (mExcelFormData.isDetails)
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 1;
                else
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy);
            }
            tableData.setSortColumn(columns.get(orderPos));
            if (mExcelFormData.ascendingOrder == 1)
                columns.get(orderPos).setReverseSort(false);
            else
                columns.get(orderPos).setReverseSort(true);
        }

        int size = DensityUtils.dp2px(mContext, 15);
        tableData.setTitleDrawFormat(new MTitleImageDrawFormat(size, size, TitleImageDrawFormat.RIGHT, titleImageDrPadding) {
            @Override
            protected Context getContext() {
                return mContext;
            }

            @Override
            protected int getResourceID(Column column) {
                setDirection(TextImageDrawFormat.RIGHT);
                if (!column.isParent()) {
                    if (column.isSorted()) {
                        if (tableData.getSortColumn() == column) {
                            if (column.isReverseSort()) {
                                return R.drawable.ic_excel_select_bottom;
                            } else {
                                return R.drawable.ic_excel_select_top;
                            }
                        } else {
                            setDirection(TextImageDrawFormat.RIGHT);
                            return R.drawable.ic_excel_noselect;
                        }
                    }
                }
                return 0;
            }
        });

        //table title点击事件
        table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                if (!columnInfo.column.isParent()) {
                    if (columnInfo.column.isSorted()) {
                        table.setSortColumn(columnInfo.column, !columnInfo.column.isReverseSort());
                        if (!ArrayUtils.isEmpty(titleListVertical)) {
                            if (!ArrayUtils.isEmpty(titleListVertical)) {
                                int pos = titleListVertical.indexOf(columnInfo.value);
                                if (onItemClickListener != null) {
                                    if (mExcelFormData.isSort) {
                                        if (mExcelFormData.isDetails)
                                            onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 2), columnInfo.column.isReverseSort() ? 2 : 1);
                                        else
                                            onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 1), columnInfo.column.isReverseSort() ? 2 : 1);
                                    } else {
                                        if (mExcelFormData.isDetails)
                                            onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 1), columnInfo.column.isReverseSort() ? 2 : 1);
                                        else
                                            onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos), columnInfo.column.isReverseSort() ? 2 : 1);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        });

        table.setTableData(tableData);
    }

    /**
     * 分页添加数据方法
     *
     * @param excelFormData
     */
    public void addVerticalData(ExcelParseUtils.ExcelFormData excelFormData) {
        if (mExcelFormData != null && excelFormData != null) {
            mExcelFormData.paramsBeans.addAll(excelFormData.paramsBeans);
            if (mExcelFormData.dataArray != null && excelFormData.dataArray != null)
                mExcelFormData.dataArray = ExcelParseUtils.mergeArray(mExcelFormData.dataArray, excelFormData.dataArray);

            if (excelFormData.dataStatus != null && excelFormData.dataStatus != null)
                mExcelFormData.dataStatus = ExcelParseUtils.mergeArray(mExcelFormData.dataStatus, excelFormData.dataStatus);

            setVerticalData();
        }
    }

    /**
     * 初始化横屏Excel布局属性
     *
     * @param excelFormData 表格数据 默认可以滚动
     */
    public void showHorizontalExcel(ExcelParseUtils.ExcelFormData excelFormData) {
        showHorizontalExcel(excelFormData, true);
    }

    /**
     * 初始化横屏Excel布局属性
     *
     * @param canScrollVertical 是否可以滚动
     */
    public void showHorizontalExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean canScrollVertical) {
        isVertical = false;
        llTableVertical.setVisibility(GONE);
        llTableLand.setVisibility(VISIBLE);
        if (excelFormData != null) {
            mExcelFormData = BeanCloneUtil.cloneTo(excelFormData);
            if (excelFormData.isMultiLine)
                initMultiLineExcel(canScrollVertical);
            else
                initHorizontalExcel(canScrollVertical);
        }
    }

    /**
     * 初始化Excel属性
     *
     * @param canScrollVertical 是否可以滚动
     */
    private void initHorizontalExcel(boolean canScrollVertical) {
        mScreenWidth = DeviceUtil.getDeviceWidth(mContext);
        firColumnWdith = (int) DensityUtils.dp2px(getContext(), 40);
        thirdColumnWdith = (int) DensityUtils.dp2px(getContext(), 0);
        columnTitleHorizontalPadding = (int) DeviceUtil.dipToPx(getContext(), 8);
        columnTitleVerticalPadding = (int) DeviceUtil.dipToPx(getContext(), 12);
        verticalPadding = (int) DeviceUtil.dipToPx(getContext(), 12);
        horizontalPadding = (int) DeviceUtil.dipToPx(getContext(), 0);
        titleImageDrPadding = (int) DeviceUtil.dipToPx(getContext(), 0);
        if (mExcelFormData.isSort)
            secColumnWdith = (int) DensityUtils.dp2px(getContext(), secDpWidth);
        else
            secColumnWdith = secColumnWdith + (int) DeviceUtil.dipToPx(getContext(), 30);
        initHorizontalTableConfig(canScrollVertical);
        setHorizontalData();
        tableLand.getMatrixHelper().flingTop(200);
    }

    /**
     * 初始化横屏Excel布局属性
     *
     * @param canScrollVertical 是否可以滚动  默认可以滚动
     */
    private void initHorizontalTableConfig(boolean canScrollVertical) {
        tableLand.getConfig().setCanScrollVertical(canScrollVertical);
        tableLand.getConfig().setColumnTitleHorizontalPadding(columnTitleHorizontalPadding);
        tableLand.getConfig().setColumnTitleVerticalPadding(columnTitleVerticalPadding);
        tableLand.getConfig().setVerticalPadding(verticalPadding);
        tableLand.getConfig().setHorizontalPadding(horizontalPadding);
        if (mExcelFormData.isFixed) {
            ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
                @Override
                public int getBackGroundColor(CellInfo cellInfo) {
                    if (cellInfo.row % 2 == 0) {
                        return mColorCCF8F8F8;
                    }
                    return mColorCCFFF;
                }
            };
            tableLand.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        } else {
            ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
                @Override
                public int getBackGroundColor(CellInfo cellInfo) {
                    if (cellInfo.row % 2 == 0) {
                        return mColorCCFFF;
                    }
                    return mColorCCF8F8F8;
                }
            };
            tableLand.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        }

        //设置网格
        tableLand.getConfig().setTableGridFormat(new BaseAbstractGridFormat() {
            @Override
            protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {
                return cellInfo.column.isShowColumnTitleVerticalLine();
            }

            @Override
            protected boolean isShowHorizontalLine(int col, int row, CellInfo cellInfo) {
                return false;
            }

            @Override
            protected boolean isShowColumnTitleVerticalLine(int col, Column column) {
                return column.isShowColumnTitleVerticalLine();
            }

            @Override
            protected boolean isShowColumnTitleHorizontalLine(int col, Column column) {
                return false;
            }
        });
    }

    /**
     * 设置横屏表格数据
     */
    private void setHorizontalData() {
        if (ArrayUtils.isEmpty(mExcelFormData.titles)) {
            return;
        }
        if (tableData != null)
            tableData.clear();
        if (mExcelFormData.dataArray != null && mExcelFormData.dataArray[0] != null) {
            for (int i = 0; i < mExcelFormData.dataArray[0].length; i++) {
                if (!TextUtils.isEmpty(mExcelFormData.dataArray[0][i])) {
                    if (maxLength < mExcelFormData.dataArray[0][i].length())
                        maxLength = mExcelFormData.dataArray[0][i].length();
                }
            }
        }
        if (mExcelFormData.isFixed) {
            if (!ArrayUtils.isEmpty(mExcelFormData.titleDatas) && !TextUtils.isEmpty(mExcelFormData.titleDatas[0])) {
                if (maxLength < mExcelFormData.titleDatas[0].length())
                    maxLength = mExcelFormData.titleDatas[0].length();
            }

        }
        if (mExcelFormData.isSort) {
            if (maxLength > 0) {
                if (maxLength < txtlength)
                    secColumnWdith = DensityUtils.dp2px(getContext(), maxLength * 16);
                else
                    secColumnWdith = DensityUtils.dp2px(getContext(), txtlength * 16);
            }
        } else {
            if (maxLength > 0) {
                if (maxLength < txtlength)
                    secColumnWdith = DensityUtils.dp2px(getContext(), maxLength * 16) + DensityUtils.dp2px(getContext(), 30);
                else
                    secColumnWdith = DensityUtils.dp2px(getContext(), txtlength * 16) + DensityUtils.dp2px(getContext(), 30);
            }
        }

        if (mExcelFormData.isDetails) {
            thirdColumnWdith = DensityUtils.dp2px(getContext(), 20);
        }
        //数据添加一列排序列
        String[] tempTitles;
        String[] tempTitleDatas = null;
        String[][] tempDatas;
        if (mExcelFormData.isSort) {
            if (mExcelFormData.isDetails) {
                //数据添加一列排序列
                tempTitles = ExcelParseUtils.addTableDetailsTitles(mExcelFormData.titles, false);
                if (mExcelFormData.isFixed)
                    tempTitleDatas = ExcelParseUtils.addTableDetailsTitleDatas(mExcelFormData.titleDatas);

                if (mExcelFormData.ascendingOrder == 2)
                    tempDatas = ExcelParseUtils.addTableDetailsDatas(mExcelFormData.dataArray);
                else
                    tempDatas = ExcelParseUtils.addTableDetailsDatasRev(mExcelFormData.dataArray, mExcelFormData.totalNum);

                if (tempDatas != null && tempDatas[1] != null) {
                    for (int i = 0; i < tempDatas[1].length; i++) {
                        if (tempDatas[1][i].length() > 20)
                            tempDatas[1][i] = tempDatas[1][i].substring(0, 19) + "…";
                        if (tempDatas[1][i].length() > 10)
                            tempDatas[1][i] = ExcelParseUtils.titleFormat(tempDatas[1][i], 10);
                    }
                }
            } else {
                //数据添加一列排序列
                tempTitles = ExcelParseUtils.addTableTitles(mExcelFormData.titles, false);
                if (mExcelFormData.isFixed && mExcelFormData.titleDatas != null)
                    tempTitleDatas = ExcelParseUtils.addTableTitleDatas(mExcelFormData.titleDatas);

                if (mExcelFormData.ascendingOrder == 2)
                    tempDatas = ExcelParseUtils.addTableDatas(mExcelFormData.dataArray);
                else
                    tempDatas = ExcelParseUtils.addTableDatasRev(mExcelFormData.dataArray, mExcelFormData.totalNum);

                if (tempDatas != null && tempDatas[1] != null) {
                    for (int i = 0; i < tempDatas[1].length; i++) {
                        if (tempDatas[1][i].length() > 20)
                            tempDatas[1][i] = tempDatas[1][i].substring(0, 19) + "…";
                        if (tempDatas[1][i].length() > 10)
                            tempDatas[1][i] = ExcelParseUtils.titleFormat(tempDatas[1][i], 10);
                    }
                }
            }
        } else {
            if (mExcelFormData.isDetails) {
                //数据添加一列排序列
                tempTitles = ExcelParseUtils.addTableNoSortDetailsTitles(mExcelFormData.titles, false);
                if (mExcelFormData.isFixed && mExcelFormData.titleDatas != null)
                    tempTitleDatas = ExcelParseUtils.addTableNoSortDetailsTitleDatas(mExcelFormData.titleDatas);

                tempDatas = ExcelParseUtils.addTableNoSortDetailsDatas(mExcelFormData.dataArray);
                if (tempDatas != null && tempDatas[0] != null) {
                    for (int i = 0; i < tempDatas[0].length; i++) {
                        if (tempDatas[0][i].length() > 20)
                            tempDatas[0][i] = tempDatas[0][i].substring(0, 19) + "…";
                        if (tempDatas[0][i].length() > 10)
                            tempDatas[0][i] = ExcelParseUtils.titleFormat(tempDatas[0][i], 10);
                    }
                }
            } else {
                tempTitles = ExcelParseUtils.addTableNoSortTitles(mExcelFormData.titles, false);
                if (mExcelFormData.isFixed && mExcelFormData.titleDatas != null)
                    tempTitleDatas = mExcelFormData.titleDatas;

                tempDatas = mExcelFormData.dataArray;
                if (tempDatas != null && tempDatas[0] != null) {
                    for (int i = 0; i < tempDatas[0].length; i++) {
                        if (tempDatas[0][i].length() > 20)
                            tempDatas[0][i] = tempDatas[0][i].substring(0, 19) + "…";
                        if (tempDatas[0][i].length() > 10)
                            tempDatas[0][i] = ExcelParseUtils.titleFormat(tempDatas[0][i], 10);
                    }
                }
            }
        }
        List<String> titleList = Arrays.asList(tempTitles);

        if (mExcelFormData.isFixed) {
            tableData = ArrayTableData.create(mExcelFormData.tableName, tempTitles, tempTitleDatas, tempDatas, null);
            setParentColumns(tableData, titleList);
        } else {
            tableData = ArrayTableData.create(mExcelFormData.tableName, tempTitles, tempDatas, null);
            setColumns(tableData, titleList);
        }
    }

    List<Integer> actionsParent;

    /**
     * 设置横屏有固定行(有父亲行)每条属性
     */
    private void setParentColumns(final ArrayTableData<String> tableData, final List<String> titleList) {
        List<Column<String>> columns = tableData.getArrayColumns();
        map = new HashMap<>();

        actionsParent = new ArrayList<>();
        if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans)) {
            for (int i = 0; i < mExcelFormData.paramsBeans.size(); i++) {
                ExcelParamsBean bean = mExcelFormData.paramsBeans.get(i);
                if (bean != null && bean.drillDownDTO != null) {
                    actionsParent.add(bean.drillDownDTO.actionType);
                } else {
                    actionsParent.add(0);
                }
            }
        }

        for (int i = 0; i < columns.size(); i++) {
            Column<String> curParentColumn = columns.get(i);
            List<Column> childColumnList = curParentColumn.getChildren();

            if (!ArrayUtils.isEmpty(childColumnList)) {
                Column<String> curColumn = childColumnList.get(0);
                if (mExcelFormData.isSort) {
                    if (mExcelFormData.isDetails) {
                        int columnW = (mScreenWidth - secColumnWdith - firColumnWdith - thirdColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                        if (i == 0) {
                            curColumn.setDrawFormat(new FirTextDrawFormat<String>(mContext, firColumnWdith));
                            curParentColumn.setFixed(true);
                            curParentColumn.setSorted(false);
                            curColumn.setTextAlign(Paint.Align.RIGHT);
                            curParentColumn.setTextAlign(Paint.Align.RIGHT);
                        } else if (i == 1) {
                            curParentColumn.setFixed(true);
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
                            curColumn.setTextAlign(Paint.Align.LEFT);
                            curColumn.setTitleAlign(Paint.Align.LEFT);
                            curParentColumn.setTitleAlign(Paint.Align.CENTER);
                            curParentColumn.setTextAlign(Paint.Align.LEFT);
                            curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mActionColor, actionsParent));
                            curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    if (onItemClickListener != null) {
                                        if (!ArrayUtils.isEmpty(actionsParent)) {
                                            if (actionsParent.get(position) != 0) {
                                                ExcelCallBackParams params = new ExcelCallBackParams();
                                                params.s = TextUtils.isEmpty(s) ? "" : s;
                                                params.positon = position;
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                                ;
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                                onItemClickListener.onColumnItemClick(column, params);
                                            }
                                        }
                                    }
                                }
                            });
                        } else if (i == 2) {
                            curParentColumn.setFixed(true);
                            curParentColumn.setSorted(false);
                            curColumn.setShowColumnTitleVerticalLine(true);
                            curParentColumn.setShowColumnTitleVerticalLine(true);
                            int size = DensityUtils.dp2px(getContext(), 15);
                            curColumn.setDrawFormat(new BitmapDrawFormat<String>(size, size) {
                                @Override
                                protected Bitmap getBitmap(final String s, String value, final int position) {
                                    if (map.get(s) == null) {
                                        if (mExcelFormData.paramsBeans != null) {
                                            ExcelParamsBean bean = mExcelFormData.paramsBeans.get(position);
                                            if (bean != null && bean.jumpDTO != null) {
                                                Glide.with(mContext).asBitmap()
                                                        .load(bean.jumpDTO.icon).apply(new RequestOptions().error(R.drawable.icon_menu_default)
                                                        .placeholder(R.drawable.icon_menu_default))
                                                        .into(new SimpleTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                                map.put(s + position, resource);
                                                                table.invalidate();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                    return map.get(s + position);
                                }
                            });
                            curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    if (onItemClickListener != null) {
                                        ExcelCallBackParams params = new ExcelCallBackParams();
                                        params.s = TextUtils.isEmpty(s) ? "" : s;
                                        params.positon = position;
                                        params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                        if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                            params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).jumpDTO;
                                        onItemClickListener.onImageItemClick(column, params);
                                    }
                                }
                            });
                        } else {
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 2)) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i - 2) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
                            if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                                curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                            }
                        }
                    } else {
                        int columnW = (mScreenWidth - secColumnWdith - firColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                        if (i == 0) {
                            curColumn.setDrawFormat(new FirTextDrawFormat<String>(mContext, firColumnWdith));
                            curParentColumn.setFixed(true);
                            curParentColumn.setSorted(false);
                            curColumn.setTextAlign(Paint.Align.RIGHT);
                            curParentColumn.setTextAlign(Paint.Align.RIGHT);
                        } else if (i == 1) {
                            curParentColumn.setFixed(true);
                            curColumn.setShowColumnTitleVerticalLine(true);
                            curParentColumn.setShowColumnTitleVerticalLine(true);
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
                            curColumn.setTextAlign(Paint.Align.LEFT);
                            curColumn.setTitleAlign(Paint.Align.LEFT);
                            curParentColumn.setTitleAlign(Paint.Align.CENTER);
                            curParentColumn.setTextAlign(Paint.Align.LEFT);
                            curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsParent));
                            curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    if (onItemClickListener != null) {
                                        if (!ArrayUtils.isEmpty(actionsParent)) {
                                            if (actionsParent.get(position) != 0) {
                                                ExcelCallBackParams params = new ExcelCallBackParams();
                                                params.s = TextUtils.isEmpty(s) ? "" : s;
                                                params.positon = position;
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                                onItemClickListener.onColumnItemClick(column, params);
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
                            if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                                curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                            }
                        }
                    }
                } else {
                    if (mExcelFormData.isDetails) {
                        int columnW = (mScreenWidth - secColumnWdith - thirdColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                        if (i == 0) {
                            curParentColumn.setFixed(true);
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
//                            curParentColumn.setSorted(false);
                            curColumn.setTextAlign(Paint.Align.LEFT);
                            curColumn.setTitleAlign(Paint.Align.CENTER);
                            curParentColumn.setTitleAlign(Paint.Align.CENTER);
                            curParentColumn.setTextAlign(Paint.Align.LEFT);
                            curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsParent, (int) DeviceUtil.dipToPx(getContext(), 15), true));
                            curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    if (onItemClickListener != null) {
                                        if (!ArrayUtils.isEmpty(actionsParent)) {
                                            if (actionsParent.get(position) != 0) {
                                                ExcelCallBackParams params = new ExcelCallBackParams();
                                                params.s = TextUtils.isEmpty(s) ? "" : s;
                                                params.positon = position;
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                                onItemClickListener.onColumnItemClick(column, params);
                                            }
                                        }
                                    }
                                }
                            });
                        } else if (i == 1) {
                            curParentColumn.setFixed(true);
                            curParentColumn.setSorted(false);
                            curColumn.setShowColumnTitleVerticalLine(true);
                            curParentColumn.setShowColumnTitleVerticalLine(true);
                            int size = DensityUtils.dp2px(getContext(), 15);
                            curColumn.setDrawFormat(new BitmapDrawFormat<String>(size, size) {
                                @Override
                                protected Bitmap getBitmap(final String s, String value, final int position) {
                                    if (map.get(s) == null) {
                                        if (mExcelFormData.paramsBeans != null) {
                                            ExcelParamsBean bean = mExcelFormData.paramsBeans.get(position);
                                            if (bean != null && bean.jumpDTO != null) {
                                                Glide.with(mContext).asBitmap()
                                                        .load(bean.jumpDTO.icon).apply(new RequestOptions().error(R.drawable.icon_menu_default)
                                                        .placeholder(R.drawable.icon_menu_default))
                                                        .into(new SimpleTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                                map.put(s + position, resource);
                                                                table.invalidate();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                    return map.get(s + position);
                                }
                            });
                            curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    if (onItemClickListener != null) {
                                        ExcelCallBackParams params = new ExcelCallBackParams();
                                        params.s = TextUtils.isEmpty(s) ? "" : s;
                                        params.positon = position;
                                        params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                        if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                            params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).jumpDTO;
                                        onItemClickListener.onImageItemClick(column, params);
                                    }
                                }
                            });
                        } else {
                            if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                                curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                            }
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
                        }
                    } else {
                        int columnW = (mScreenWidth - secColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                        if (i == 0) {
                            curColumn.setTextAlign(Paint.Align.LEFT);
                            curColumn.setTitleAlign(Paint.Align.CENTER);
                            curParentColumn.setTitleAlign(Paint.Align.CENTER);
                            curParentColumn.setTextAlign(Paint.Align.LEFT);
                            curParentColumn.setFixed(true);
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
                            curColumn.setShowColumnTitleVerticalLine(true);
                            curParentColumn.setShowColumnTitleVerticalLine(true);
                            curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsParent, (int) DeviceUtil.dipToPx(getContext(), 15), true));
                            curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    if (onItemClickListener != null) {
                                        if (!ArrayUtils.isEmpty(actionsParent)) {
                                            if (actionsParent.get(position) != 0) {
                                                ExcelCallBackParams params = new ExcelCallBackParams();
                                                params.s = TextUtils.isEmpty(s) ? "" : s;
                                                params.positon = position;
                                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                                onItemClickListener.onColumnItemClick(column, params);
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                                curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                            }
                            if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                                curParentColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                            } else {
                                curParentColumn.setSorted(false);
                            }
                        }

                    }
                }
            }
        }

        if (!TextUtils.isEmpty(mExcelFormData.orderBy) && !ArrayUtils.isEmpty(mExcelFormData.keys)) {
            int orderPos = 0;
            if (mExcelFormData.isSort) {
                if (mExcelFormData.isDetails)
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 2;
                else
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 1;
            } else {
                if (mExcelFormData.isDetails)
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 1;
                else
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy);
            }
            tableData.setSortColumn(columns.get(orderPos));
            if (mExcelFormData.ascendingOrder == 1)
                columns.get(orderPos).setReverseSort(false);
            else
                columns.get(orderPos).setReverseSort(true);
        }

        int size = DensityUtils.dp2px(mContext, deafaultTextSize);
        tableData.setTitleDrawFormat(new MTitleImageDrawFormat(size, size, TitleImageDrawFormat.RIGHT, titleImageDrPadding, mExcelFormData.isFixed) {
            @Override
            protected Context getContext() {
                return mContext;
            }

            @Override
            protected int getResourceID(Column column) {
                setDirection(TextImageDrawFormat.RIGHT);
                if (column.isParent()) {
                    if (column.isSorted()) {
                        if (tableData.getSortColumn() == column) {
                            if (column.isReverseSort()) {
                                return R.drawable.ic_excel_select_bottom;
                            } else {
                                return R.drawable.ic_excel_select_top;
                            }
                        } else {
                            setDirection(TextImageDrawFormat.RIGHT);
                            return R.drawable.ic_excel_noselect;
                        }
                    }
                }
                return 0;
            }
        });

        //table title点击事件
        tableLand.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                if (columnInfo.column.isParent()) {
                    if (columnInfo.column.isSorted()) {
                        tableLand.setSortColumn(columnInfo.column, !columnInfo.column.isReverseSort());
                        if (!ArrayUtils.isEmpty(titleList)) {
                            int pos = titleList.indexOf(columnInfo.value);
                            if (onItemClickListener != null) {
                                if (mExcelFormData.isSort) {
                                    if (mExcelFormData.isDetails)
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 2), columnInfo.column.isReverseSort() ? 2 : 1);
                                    else
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 1), columnInfo.column.isReverseSort() ? 2 : 1);
                                } else {
                                    if (mExcelFormData.isDetails)
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 1), columnInfo.column.isReverseSort() ? 2 : 1);
                                    else
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos), columnInfo.column.isReverseSort() ? 2 : 1);
                                }
                            }
                        }
                    }
                }
            }
        });

        tableLand.setTableData(tableData);
    }

    List<Integer> actionsFixed;
    /**
     * 设置横屏有固定行每条属性
     */
    private void setColumns(final ArrayTableData<String> tableData, final List<String> titleList) {
        List<Column<String>> columns = tableData.getArrayColumns();
        map = new HashMap<>();
        actionsFixed = new ArrayList<>();
        if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans)) {
            for (int i = 0; i < mExcelFormData.paramsBeans.size(); i++) {
                ExcelParamsBean bean = mExcelFormData.paramsBeans.get(i);
                if (bean != null && bean.drillDownDTO != null) {
                    actionsFixed.add(bean.drillDownDTO.actionType);
                } else {
                    actionsFixed.add(0);
                }
            }
        }

        for (int i = 0; i < columns.size(); i++) {
            Column<String> curColumn = columns.get(i);
            if (mExcelFormData.isSort) {
                if (mExcelFormData.isDetails) {
                    int columnW = (mScreenWidth - secColumnWdith - firColumnWdith - thirdColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                    if (i == 0) {
                        curColumn.setDrawFormat(new FirTextDrawFormat<String>(mContext, firColumnWdith));
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setTextAlign(Paint.Align.RIGHT);
                        curColumn.setTitleAlign(Paint.Align.RIGHT);
                    } else if (i == 1) {
                        curColumn.setFixed(true);
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.LEFT);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsFixed));
//                    curColumn.setDrawFormat(new MTextDrawFormat<>(mContext.getResources().getColor(R.color.color_4A90E2), actions));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsFixed)) {
                                        if (actionsFixed.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                    } else if (i == 2) {
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setShowColumnTitleVerticalLine(true);
                        int size = DensityUtils.dp2px(getContext(), 15);
                        curColumn.setDrawFormat(new BitmapDrawFormat<String>(size, size) {
                            @Override
                            protected Bitmap getBitmap(final String s, String value, final int position) {
                                if (map.get(s) == null) {
                                    if (mExcelFormData.paramsBeans != null) {
                                        ExcelParamsBean bean = mExcelFormData.paramsBeans.get(position);
                                        if (bean != null && bean.jumpDTO != null) {
                                            Glide.with(mContext).asBitmap()
                                                    .load(bean.jumpDTO.icon).apply(new RequestOptions().error(R.drawable.icon_menu_default)
                                                    .placeholder(R.drawable.icon_menu_default))
                                                    .into(new SimpleTarget<Bitmap>() {
                                                        @Override
                                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                            map.put(s + position, resource);
                                                            table.invalidate();
                                                        }
                                                    });
                                        }
                                    }
                                }
                                return map.get(s + position);
                            }
                        });
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                ExcelCallBackParams params = new ExcelCallBackParams();
                                params.s = TextUtils.isEmpty(s) ? "" : s;
                                params.positon = position;
                                params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                    params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).jumpDTO;
                                onItemClickListener.onImageItemClick(column, params);
                            }
                        });
                    } else {
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 2)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i - 2) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                        if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                            curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                        }
                    }
                } else {
                    int columnW = (mScreenWidth - secColumnWdith - firColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                    if (i == 0) {
                        curColumn.setDrawFormat(new FirTextDrawFormat<String>(mContext, firColumnWdith));
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setTextAlign(Paint.Align.RIGHT);
                        curColumn.setTitleAlign(Paint.Align.RIGHT);
                    } else if (i == 1) {
                        curColumn.setFixed(true);
                        curColumn.setShowColumnTitleVerticalLine(true);
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.LEFT);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mActionColor, actionsFixed));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsFixed)) {
                                        if (actionsFixed.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                        if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                            curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                        }
                    }
                }
            } else {
                if (mExcelFormData.isDetails) {
                    int columnW = (mScreenWidth - secColumnWdith - thirdColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                    if (i == 0) {
                        curColumn.setFixed(true);
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsFixed, (int) DeviceUtil.dipToPx(getContext(), 15), true));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsFixed)) {
                                        if (actionsFixed.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                    } else if (i == 1) {
                        curColumn.setFixed(true);
                        curColumn.setSorted(false);
                        curColumn.setShowColumnTitleVerticalLine(true);
                        int size = DensityUtils.dp2px(getContext(), 15);
                        curColumn.setDrawFormat(new BitmapDrawFormat<String>(size, size) {
                            @Override
                            protected Bitmap getBitmap(final String s, String value, final int position) {
                                if (map.get(s) == null) {
                                    if (mExcelFormData.paramsBeans != null) {
                                        ExcelParamsBean bean = mExcelFormData.paramsBeans.get(position);
                                        if (bean != null && bean.jumpDTO != null) {
                                            Glide.with(mContext).asBitmap()
                                                    .load(bean.jumpDTO.icon).apply(new RequestOptions().error(R.drawable.icon_menu_default)
                                                    .placeholder(R.drawable.icon_menu_default))
                                                    .into(new SimpleTarget<Bitmap>() {
                                                        @Override
                                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                            map.put(s + position, resource);
                                                            table.invalidate();
                                                        }
                                                    });
                                        }
                                    }
                                }
                                return map.get(s + position);
                            }
                        });
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    ExcelCallBackParams params = new ExcelCallBackParams();
                                    params.s = TextUtils.isEmpty(s) ? "" : s;
                                    params.positon = position;
                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).jumpDTO;
                                    onItemClickListener.onImageItemClick(column, params);
                                }
                            }
                        });
                    } else {
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                            curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                        }
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                    }
                } else {
                    int columnW = (mScreenWidth - secColumnWdith - columnTitleHorizontalPadding * 2 * (mExcelFormData.titles.length - 1)) / (mExcelFormData.titles.length - 1);
                    if (i == 0) {
                        curColumn.setFixed(true);
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                        curColumn.setShowColumnTitleVerticalLine(true);
                        curColumn.setTextAlign(Paint.Align.LEFT);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsFixed, (int) DeviceUtil.dipToPx(getContext(), 15), true));
                        curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                            @Override
                            public void onClick(Column<String> column, String value, String s, int position) {
                                if (onItemClickListener != null) {
                                    if (!ArrayUtils.isEmpty(actionsFixed)) {
                                        if (actionsFixed.get(position) != 0) {
                                            ExcelCallBackParams params = new ExcelCallBackParams();
                                            params.s = TextUtils.isEmpty(s) ? "" : s;
                                            params.positon = position;
                                            params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                            if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                                params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                            onItemClickListener.onColumnItemClick(column, params);
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        curColumn.setTextAlign(Paint.Align.CENTER);
                        curColumn.setTitleAlign(Paint.Align.CENTER);
                        if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                            curColumn.setDrawFormat(new MultiLineDrawFormat<String>(columnW));
                        }
                        if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                            curColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                        } else {
                            curColumn.setSorted(false);
                        }
                    }

                }
            }
        }

        if (!TextUtils.isEmpty(mExcelFormData.orderBy) && !ArrayUtils.isEmpty(mExcelFormData.keys)) {
            int orderPos = 0;
            if (mExcelFormData.isSort) {
                if (mExcelFormData.isDetails)
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 2;
                else
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 1;
            } else {
                if (mExcelFormData.isDetails)
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 1;
                else
                    orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy);
            }
            tableData.setSortColumn(columns.get(orderPos));
            if (mExcelFormData.ascendingOrder == 1)
                columns.get(orderPos).setReverseSort(false);
            else
                columns.get(orderPos).setReverseSort(true);
        }

        int size = DensityUtils.dp2px(mContext, deafaultTextSize);
        tableData.setTitleDrawFormat(new MTitleImageDrawFormat(size, size, TitleImageDrawFormat.RIGHT, titleImageDrPadding, mExcelFormData.isFixed) {
            @Override
            protected Context getContext() {
                return mContext;
            }

            @Override
            protected int getResourceID(Column column) {
                setDirection(TextImageDrawFormat.RIGHT);
                if (!column.isParent()) {
                    if (column.isSorted()) {
                        if (tableData.getSortColumn() == column) {
                            if (column.isReverseSort()) {
                                return R.drawable.ic_excel_select_bottom;
                            } else {
                                return R.drawable.ic_excel_select_top;
                            }
                        } else {
                            setDirection(TextImageDrawFormat.RIGHT);
                            return R.drawable.ic_excel_noselect;
                        }
                    }
                }
                return 0;
            }
        });

        //table title点击事件
        tableLand.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                if (!columnInfo.column.isParent()) {
                    if (columnInfo.column.isSorted()) {
                        tableLand.setSortColumn(columnInfo.column, !columnInfo.column.isReverseSort());
                        if (!ArrayUtils.isEmpty(titleList)) {
                            int pos = titleList.indexOf(columnInfo.value);
                            if (onItemClickListener != null) {
                                if (mExcelFormData.isSort) {
                                    if (mExcelFormData.isDetails)
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 2), columnInfo.column.isReverseSort() ? 2 : 1);
                                    else
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 1), columnInfo.column.isReverseSort() ? 2 : 1);
                                } else {
                                    if (mExcelFormData.isDetails)
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 1), columnInfo.column.isReverseSort() ? 2 : 1);
                                    else
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos), columnInfo.column.isReverseSort() ? 2 : 1);
                                }
                            }
                        }
                    }
                }
            }
        });

        tableLand.setTableData(tableData);
    }

    /**
     * 横屏分页添加数据方法
     *
     * @param excelFormData
     */
    public void addHorizontalData(ExcelParseUtils.ExcelFormData excelFormData) {
        if (mExcelFormData != null && excelFormData != null) {
            mExcelFormData.paramsBeans.addAll(excelFormData.paramsBeans);
            if (mExcelFormData.dataArray != null && excelFormData.dataArray != null)
                mExcelFormData.dataArray = ExcelParseUtils.mergeArray(mExcelFormData.dataArray, excelFormData.dataArray);

            if (excelFormData.dataStatus != null && excelFormData.dataStatus != null)
                mExcelFormData.dataStatus = ExcelParseUtils.mergeArray(mExcelFormData.dataStatus, excelFormData.dataStatus);

            setHorizontalData();
        }
    }

    /**
     * 初始化横屏 多行数据Excel布局属性
     *
     * @param excelFormData 表格数据 默认可以滚动
     */
    public void showMultiLineDataExcel(ExcelParseUtils.ExcelFormData excelFormData) {
        showMultiLineDataExcel(excelFormData, true);
    }

    /**
     * 初始化横屏 多行数据Excel布局属性
     *
     * @param excelFormData 表格数据 默认可以滚动
     */
    public void showMultiLineDataExcel(ExcelParseUtils.ExcelFormData excelFormData, boolean canScrollVertical) {
        llTableVertical.setVisibility(GONE);
        llTableLand.setVisibility(VISIBLE);
        isVertical = false;
        if (excelFormData != null) {
            mExcelFormData = BeanCloneUtil.cloneTo(excelFormData);
            initMultiLineExcel(canScrollVertical);
        }
    }

    private void initMultiLineExcel(boolean canScrollVertical) {
        columnTitleHorizontalPadding = (int) DeviceUtil.dipToPx(getContext(), 5);
        columnTitleVerticalPadding = (int) DeviceUtil.dipToPx(getContext(), 12);
        firColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 40);
        maxSecColWdith = (int) DeviceUtil.dipToPx(getContext(), 160);
        if (mExcelFormData.isSort)
            secColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 120);
        else
            secColumnWdith = (int) DeviceUtil.dipToPx(getContext(), 160);
        verticalPadding = (int) DeviceUtil.dipToPx(getContext(), 12);
        horizontalPadding = (int) DeviceUtil.dipToPx(getContext(), 5);
        titleImageDrPadding = (int) DeviceUtil.dipToPx(getContext(), 0);
        initMultilineTableConfig(canScrollVertical);
        setMultiLineData();
        tableLand.getMatrixHelper().flingTop(200);
    }

    private void initMultilineTableConfig(boolean canScrollVertical) {
        tableLand.getConfig().setColumnTitleHorizontalPadding(columnTitleHorizontalPadding);
        tableLand.getConfig().setColumnTitleVerticalPadding(columnTitleVerticalPadding);
        tableLand.getConfig().setVerticalPadding(verticalPadding);
        tableLand.getConfig().setHorizontalPadding(horizontalPadding);
        tableLand.getConfig().setMinTableWidth(mScreenWidth);
        tableLand.getConfig().setCanScrollVertical(canScrollVertical);
        //设置网格
        tableLand.getConfig().setTableGridFormat(new BaseAbstractGridFormat() {
            @Override
            protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {
                return cellInfo.column.isShowColumnTitleVerticalLine();
            }

            @Override
            protected boolean isShowHorizontalLine(int col, int row, CellInfo cellInfo) {
                return false;
            }

            @Override
            protected boolean isShowColumnTitleVerticalLine(int col, Column column) {
                return column.isShowColumnTitleVerticalLine();
            }
        });
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return mColorCCF8F8F8;
                }
                return mColorCCFFF;
            }
        };
        tableLand.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        tableLand.setBackgroundColor(mColorTrans);
    }

    List<String> titleListMulti;
    List<Integer> actionsMulti;
    private void setMultiLineData() {
        if (ArrayUtils.isEmpty(mExcelFormData.titles)) {
            return;
        }
        if (tableData != null)
            tableData.clear();

        if (mExcelFormData.dataArray != null && mExcelFormData.dataArray[0] != null) {
            for (int i = 0; i < mExcelFormData.dataArray[0].length; i++) {
                if (!TextUtils.isEmpty(mExcelFormData.dataArray[0][i])) {
                    if (maxLength < mExcelFormData.dataArray[0][i].length())
                        maxLength = mExcelFormData.dataArray[0][i].length();
                }
            }
        }
        if (maxLength > 0) {
            if (maxLength < txtlength)
                secColumnWdith = (int) DensityUtils.dp2px(getContext(), maxLength * 16);
            else
                secColumnWdith = (int) DensityUtils.dp2px(getContext(), txtlength * 16);
        }

        if (secColumnWdith > maxSecColWdith) secColumnWdith = maxSecColWdith;
        String[] tempTitles;
        String[][] tempDatas;
        if (mExcelFormData.isSort) {
            //数据添加一列排序列
            tempTitles = ExcelParseUtils.addTableTitles(mExcelFormData.titles, false);
            if (mExcelFormData.ascendingOrder == 2)
                tempDatas = ExcelParseUtils.addTableDatas(mExcelFormData.dataArray);
            else
                tempDatas = ExcelParseUtils.addTableDatasRev(mExcelFormData.dataArray, mExcelFormData.totalNum);
//            if (tempDatas != null && tempDatas[1] != null) {
//                for (int i = 0; i < tempDatas[1].length; i++) {
//                    if (tempDatas[1][i].length() > 16)
//                        tempDatas[1][i] = tempDatas[1][i].substring(0, 15) + "…";
//                    if (tempDatas[1][i].length() > 8)
//                        tempDatas[1][i] = ExcelDataUtils.titleFormat(tempDatas[1][i], 8);
//                }
//            }
        } else {
            //数据添加一列排序列
            tempTitles = ExcelParseUtils.addTableNoSortTitles(mExcelFormData.titles, false);
//            if (titleDatas != null) {
//                tempDatas = ExcelDataUtils.addTableNoSortDatas(titleDatas, mDataArray);
//            } else {
            tempDatas = ExcelParseUtils.addTableNoSortDatas(mExcelFormData.dataArray);
//            }
//            if (tempDatas != null && tempDatas[0] != null) {
//                for (int i = 0; i < tempDatas[0].length; i++) {
//                    if (tempDatas[0][i].length() > 16)
//                        tempDatas[0][i] = tempDatas[0][i].substring(0, 15) + "…";
//                    if (tempDatas[0][i].length() > 8)
//                        tempDatas[0][i] = ExcelDataUtils.titleFormat(tempDatas[0][i], 8);
//                }
//            }
        }

        titleListMulti = Arrays.asList(tempTitles);

        for (int i = 0; i < tempDatas.length; i++) {
            for (int j = 0; j < tempDatas[i].length; j++) {
                if (i > 0)
                    tempDatas[i][j] = tempDatas[i][j];
            }
        }

        tableData = ArrayTableData.create(mExcelFormData.tableName, tempTitles, tempDatas, null);
        List<Column<String>> columns = tableData.getArrayColumns();
        actionsMulti = new ArrayList<>();
        if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans)) {
            for (int i = 0; i < mExcelFormData.paramsBeans.size(); i++) {
                ExcelParamsBean bean = mExcelFormData.paramsBeans.get(i);
                if (bean != null && bean.drillDownDTO != null) {
                    actionsMulti.add(bean.drillDownDTO.actionType);
                } else {
                    actionsMulti.add(0);
                }
            }
        }
        for (int i = 0; i < columns.size(); i++) {
            Column<String> curColumn = columns.get(i);
            if (mExcelFormData.isSort) {
                int columnW = (mScreenWidth - firColumnWdith - secColumnWdith - columnTitleHorizontalPadding * 2 * mExcelFormData.titles.length) / (mExcelFormData.titles.length - 1);
                if (i == 0) {
                    curColumn.setDrawFormat(new FirTextDrawFormat<String>(mContext, firColumnWdith));
                    curColumn.setFixed(true);
                    curColumn.setSorted(false);
                    curColumn.setTitleAlign(Paint.Align.RIGHT);
                    curColumn.setTextAlign(Paint.Align.RIGHT);
                } else if (i == 1) {
                    curColumn.setFixed(true);
                    curColumn.setShowColumnTitleVerticalLine(true);
                    if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                        curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                    } else {
                        curColumn.setSorted(false);
                    }
                    curColumn.setTextAlign(Paint.Align.LEFT);
                    curColumn.setTitleAlign(Paint.Align.LEFT);
                    curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsMulti));
                    curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                        @Override
                        public void onClick(Column<String> column, String value, String s, int position) {
                            if (onItemClickListener != null) {
                                if (!ArrayUtils.isEmpty(actionsMulti)) {
                                    if (actionsMulti.get(position) != 0) {
                                        ExcelCallBackParams params = new ExcelCallBackParams();
                                        params.s = TextUtils.isEmpty(s) ? "" : s;
                                        params.positon = position;
                                        params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                        if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                            params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                        onItemClickListener.onColumnItemClick(column, params);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    curColumn.setTextAlign(Paint.Align.CENTER);
                    curColumn.setTitleAlign(Paint.Align.CENTER);
                    if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                        curColumn.setSorted(mExcelFormData.isOrders.get(i - 1) == 1 ? true : false);
                    } else {
                        curColumn.setSorted(false);
                    }

                    if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                        curColumn.setDrawFormat(new TextWithColorDrawFormatNew<String>(columnW, mColors, mExcelFormData.dataStatus));
                    } else {
                        curColumn.setDrawFormat(new TextWithColorDrawFormatNew<String>(mColors, mExcelFormData.dataStatus));
                    }
//                    if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
//                        curColumn.setDrawFormat(new TextWithColorDrawFormat<>(columnW, mColors));
//                    } else {
//                        curColumn.setDrawFormat(new TextWithColorDrawFormat<>(mColors));
//                    }
                    curColumn.setTitleAlign(Paint.Align.CENTER);
                    curColumn.setTextAlign(Paint.Align.CENTER);
                }
            } else {
                int columnW = (mScreenWidth - secColumnWdith - columnTitleHorizontalPadding * 2 * mExcelFormData.titles.length) / (mExcelFormData.titles.length - 1);
                if (i == 0) {
                    curColumn.setFixed(true);
                    if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > 0) {
                        curColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                    } else {
                        curColumn.setSorted(false);
                    }
                    curColumn.setShowColumnTitleVerticalLine(true);
                    curColumn.setTextAlign(Paint.Align.LEFT);
                    curColumn.setTitleAlign(Paint.Align.CENTER);
                    curColumn.setDrawFormat(new MultiLineDrawFormatNew<String>(secColumnWdith, mContext.getResources().getColor(R.color.color_1990FF), actionsMulti, (int) DeviceUtil.dipToPx(getContext(), 10)));
                    curColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                        @Override
                        public void onClick(Column<String> column, String value, String s, int position) {
                            if (onItemClickListener != null) {
                                if (actionsMulti.get(position) != 0) {
                                    ExcelCallBackParams params = new ExcelCallBackParams();
                                    params.s = TextUtils.isEmpty(s) ? "" : s;
                                    params.positon = position;
                                    params.value = TextUtils.isEmpty(mExcelFormData.dataArray[0][position]) ? "" : mExcelFormData.dataArray[0][position];
                                    if (!ArrayUtils.isEmpty(mExcelFormData.paramsBeans) && mExcelFormData.paramsBeans.size() > position && mExcelFormData.paramsBeans.get(position) != null)
                                        params.jumpDTOBean = mExcelFormData.paramsBeans.get(position).drillDownDTO;
                                    onItemClickListener.onColumnItemClick(column, params);
                                }
                            }
                        }
                    });
                } else {
                    if (mExcelFormData.titles.length < 6 && mExcelFormData.titles.length > 1) {
                        curColumn.setDrawFormat(new TextWithColorDrawFormatNew<String>(columnW, mColors, mExcelFormData.dataStatus));
                    } else {
                        curColumn.setDrawFormat(new TextWithColorDrawFormatNew<String>(mColors, mExcelFormData.dataStatus));
                    }

                    curColumn.setTitleAlign(Paint.Align.CENTER);
                    curColumn.setTextAlign(Paint.Align.CENTER);
                }

                if (!ArrayUtils.isEmpty(mExcelFormData.isOrders) && mExcelFormData.isOrders.size() > (i - 1)) {
                    curColumn.setSorted(mExcelFormData.isOrders.get(i) == 1 ? true : false);
                } else {
                    curColumn.setSorted(false);
                }
            }
        }

        if (!TextUtils.isEmpty(mExcelFormData.orderBy) && !ArrayUtils.isEmpty(mExcelFormData.keys)) {
            int orderPos = 0;
            if (mExcelFormData.isSort) {
                orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy) + 1;
            } else {
                orderPos = mExcelFormData.keys.indexOf(mExcelFormData.orderBy);
            }
            tableData.setSortColumn(columns.get(orderPos));
            if (mExcelFormData.ascendingOrder == 1)
                columns.get(orderPos).setReverseSort(false);
            else
                columns.get(orderPos).setReverseSort(true);
        }

        int size = DensityUtils.dp2px(mContext, 15);
        tableData.setTitleDrawFormat(new MTitleImageDrawFormat(size, size, TitleImageDrawFormat.RIGHT, titleImageDrPadding) {
            @Override
            protected Context getContext() {
                return mContext;
            }

            @Override
            protected int getResourceID(Column column) {
                setDirection(TextImageDrawFormat.RIGHT);
                if (!column.isParent()) {
                    if (column.isSorted()) {
                        if (tableData.getSortColumn() == column) {
                            if (column.isReverseSort()) {
                                return R.drawable.ic_excel_select_bottom;
                            } else {
                                return R.drawable.ic_excel_select_top;
                            }
                        } else {
                            setDirection(TextImageDrawFormat.RIGHT);
                            return R.drawable.ic_excel_noselect;
                        }
                    }
                }
                return 0;
            }
        });

        //table title点击事件
        tableLand.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                if (!columnInfo.column.isParent()) {
                    if (columnInfo.column.isSorted()) {
                        tableLand.setSortColumn(columnInfo.column, !columnInfo.column.isReverseSort());
                        if (!ArrayUtils.isEmpty(titleListMulti)) {
                            if (!ArrayUtils.isEmpty(titleListMulti)) {
                                int pos = titleListMulti.indexOf(columnInfo.value);
                                if (onItemClickListener != null) {
                                    if (mExcelFormData.isSort) {
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos - 1), columnInfo.column.isReverseSort() ? 2 : 1);
                                    } else {
                                        onItemClickListener.onTitleItemClick(columnInfo, pos, mExcelFormData.keys.get(pos), columnInfo.column.isReverseSort() ? 2 : 1);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        });

        tableLand.setTableData(tableData);
    }

    /**
     * 多行添加数据方法
     *
     * @param excelFormData
     */
    public void addMultiData(ExcelParseUtils.ExcelFormData excelFormData) {
        if (mExcelFormData != null && excelFormData != null) {
            mExcelFormData.paramsBeans.addAll(excelFormData.paramsBeans);
            if (mExcelFormData.dataArray != null && excelFormData.dataArray != null)
                mExcelFormData.dataArray = ExcelParseUtils.mergeArray(mExcelFormData.dataArray, excelFormData.dataArray);

            if (excelFormData.dataStatus != null && excelFormData.dataStatus != null)
                mExcelFormData.dataStatus = ExcelParseUtils.mergeArray(mExcelFormData.dataStatus, excelFormData.dataStatus);

            setMultiLineData();
        }
    }

    /**
     * 通用添加数据方法
     *
     * @param excelFormData
     */
    public void addExcelData(ExcelParseUtils.ExcelFormData excelFormData) {
        if (isVertical) {
            addVerticalData(excelFormData);
        } else {

            if (mExcelFormData.isMultiLine)
                addMultiData(excelFormData);
            else
                addHorizontalData(excelFormData);
        }
    }

    public void setTitleVisiable(boolean isShow) {
        layoutTitle.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setBottomVisiable(boolean isShow) {
        llMore.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setTitleLabelColor(int color) {
        titleView.setBackgroundColor(color);
    }

    public void setLandVisiable(boolean isShow) {
        llLand.setVisibility(isShow ? VISIBLE : GONE);
    }

    public interface OnItemClickListener {
        //头部点击事件
        void onTitleItemClick(ColumnInfo columnInfo, int position, String key, int isReverse);

        //图标点击事件
        void onImageItemClick(Column<String> column, ExcelCallBackParams imageCallBackParams);

        //cell点击事件
        void onColumnItemClick(Column column, ExcelCallBackParams callBackParams);
    }

}
