package com.example.myapplication.ExcelView;

import android.text.TextUtils;

import com.example.myapplication.bean.ExcelData;
import com.example.myapplication.bean.ExcelParamsBean;
import com.example.myapplication.utils.ArrayUtils;
import com.example.myapplication.utils.GsonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelParseUtils {

    public static class ExcelFormData implements Serializable {
        public static final String TAG = ExcelParseUtils.ExcelFormData.class.getSimpleName();
        public int ascendingOrder;//升降序 1升序 2降序
        public String orderBy;//排序字段
        public int totalNum;
        public boolean isLastPage;
        public boolean isFirstpage;
        public ExcelData.FilterDTOBean filterDTOBean;
        public String tableName;
        public String[] titles;
        public String[] titleDatas;
        public String[] titleStatus;
        public String[][] dataArray;
        public String[][] dataStatus;
        public List<Integer> isOrders;
        public List<String> keys;
        public boolean isSort = true;
        public boolean isDetails = false;
        public boolean isFixed = false;
        public ExcelParamsBean headParamsBean;
        public List<ExcelParamsBean> paramsBeans;
        public boolean isMultiLine = false;
    }

    public static ExcelFormData parse(ExcelData excelData) {
        if (excelData == null) return null;
        int rowSize = 0;
        int cowSize = 0;

        ExcelFormData formData = new ExcelFormData();
        formData.isOrders = new ArrayList<>();
        formData.keys = new ArrayList<>();
        formData.paramsBeans = new ArrayList<>();

        formData.tableName = excelData.tableTitle;
        formData.filterDTOBean = excelData.filterDTO;
        if (!ArrayUtils.isEmpty(excelData.tableHeadList))
            rowSize = excelData.tableHeadList.size();

        formData.titles = new String[rowSize];
        formData.titleDatas = new String[rowSize];

        if (excelData.relatedInfoDTO != null) {
            formData.isSort = excelData.relatedInfoDTO.hasRankInfo;
        } else {
            formData.isSort = false;
        }

        if (excelData.tableOrderByDTO != null) {
            formData.orderBy = excelData.tableOrderByDTO.orderBy;
            formData.ascendingOrder = excelData.tableOrderByDTO.ascendingOrder;
        } else {
            formData.orderBy = null;
            formData.ascendingOrder = 1;
        }

        if (excelData.growthRankListDTOPageInfo != null) {
            formData.isLastPage = excelData.growthRankListDTOPageInfo.isLastPage;
            formData.isFirstpage = excelData.growthRankListDTOPageInfo.isFirstPage;
            formData.totalNum = excelData.growthRankListDTOPageInfo.total;
            if (!ArrayUtils.isEmpty(excelData.growthRankListDTOPageInfo.list))
                cowSize = excelData.growthRankListDTOPageInfo.list.size();
        } else {
            formData.isLastPage = true;
            formData.totalNum = 0;
        }

        formData.dataArray = new String[rowSize][cowSize];
        formData.dataStatus = new String[rowSize][cowSize];

        for (int i = 0; i < rowSize; i++) {
            if (!ArrayUtils.isEmpty(excelData.tableHeadList) && excelData.tableHeadList.size() > i) {
                ExcelData.TableHeadListBean bean = excelData.tableHeadList.get(i);
                if (bean != null) {
                    formData.titles[i] = bean.value;
                    formData.isOrders.add(bean.isOrderBy);

                    if (!TextUtils.isEmpty(bean.key)) {
                        String[] allkeys = bean.key.split(",");
                        if (allkeys.length > 1) {//多排数据解析方法
                            formData.isMultiLine = true;
                            formData.keys.add(allkeys[0]);
                            if (excelData.growthRankListDTO != null) {
                                JsonObject jsonObject = GsonUtil.beanToJsonObject(excelData.growthRankListDTO);
                                StringBuffer sb = new StringBuffer();
                                StringBuffer sbStatus = new StringBuffer();
                                for (int k = 0; k < allkeys.length; k++) {
                                    String valueStr = jsonObject.get(allkeys[k]) == null || TextUtils.isEmpty(jsonObject.get(allkeys[k]).getAsString()) ? "" : jsonObject.get(allkeys[k]).getAsString();
                                    String valueStatus = jsonObject.get(allkeys[k] + "Status") == null || TextUtils.isEmpty(jsonObject.get(allkeys[k] + "Status").getAsString()) ? "" : jsonObject.get(allkeys[k] + "Status").getAsString();

                                    if (k == (allkeys.length - 1)) {
                                        sb.append(TextUtils.isEmpty(valueStr) ? "" : valueStr);
                                        sbStatus.append(TextUtils.isEmpty(valueStatus) ? "0" : valueStatus);
                                    } else {
                                        sb.append(TextUtils.isEmpty(valueStr) ? "\n" : valueStr + "\n");
                                        sbStatus.append(TextUtils.isEmpty(valueStatus) ? "0," : valueStatus + ",");
                                    }
                                }
                                formData.titleDatas[i] = sb.toString();
                                formData.titleStatus[i] = sbStatus.toString();
                                formData.headParamsBean = GsonUtil.getEntity(jsonObject.toString(), ExcelParamsBean.class);
                                if (formData.headParamsBean != null) {
                                    if (formData.filterDTOBean != null) {
                                        formData.filterDTOBean.filterNum = formData.headParamsBean.num;
                                        formData.filterDTOBean.filterOrgName = formData.headParamsBean.orgName;
                                    }
                                    if (formData.headParamsBean.jumpDTO != null) {
                                        formData.isDetails = true;
                                        JsonObject obj = GsonUtil.beanToJsonObject(formData.headParamsBean.jumpDTO.paramMap);
                                        HashMap<String, String> params = new HashMap<>();
                                        if (obj != null) {
                                            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                                                if (jsonObject.get(entry.getKey()) != null)
                                                    params.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
                                                else
                                                    params.put(entry.getKey(), entry.getValue().getAsString());
                                            }
                                            formData.headParamsBean.jumpDTO.params = params;
                                        }
                                    }
                                    if (formData.headParamsBean.drillDownDTO != null) {
                                        JsonObject obj = GsonUtil.beanToJsonObject(formData.headParamsBean.drillDownDTO.paramMap);
                                        if (obj != null) {
                                            HashMap<String, String> params = new HashMap<>();
                                            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                                                if (jsonObject.get(entry.getKey()) != null)
                                                    params.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
                                                else
                                                    params.put(entry.getKey(), entry.getValue().getAsString());
                                            }
                                            formData.headParamsBean.drillDownDTO.params = params;
                                        }
                                    }
                                }
                                formData.isFixed = true;
                            }

                            for (int j = 0; j < cowSize; j++) {
                                if (excelData.growthRankListDTOPageInfo != null) {
                                    if (!ArrayUtils.isEmpty(excelData.growthRankListDTOPageInfo.list) && excelData.growthRankListDTOPageInfo.list.size() > j) {
                                        List<Object> jsonStrList = excelData.growthRankListDTOPageInfo.list;
                                        if (jsonStrList.get(j) != null) {
                                            JsonObject jsonObject = GsonUtil.beanToJsonObject(jsonStrList.get(j));
                                            StringBuffer sb = new StringBuffer();
                                            StringBuffer sbStatus = new StringBuffer();
                                            for (int k = 0; k < allkeys.length; k++) {
                                                String valueStr = jsonObject.get(allkeys[k]) == null || TextUtils.isEmpty(jsonObject.get(allkeys[k]).getAsString()) ? "" : jsonObject.get(allkeys[k]).getAsString();
                                                String valueStatus = jsonObject.get(allkeys[k] + "Status") == null || TextUtils.isEmpty(jsonObject.get(allkeys[k] + "Status").getAsString()) ? "" : jsonObject.get(allkeys[k] + "Status").getAsString();

                                                if (k == (allkeys.length - 1)) {
                                                    sb.append(TextUtils.isEmpty(valueStr) ? "" : valueStr);
                                                    sbStatus.append(TextUtils.isEmpty(valueStatus) ? "0" : valueStatus);
                                                } else {
                                                    sb.append(TextUtils.isEmpty(valueStr) ? "\n" : valueStr + "\n");
                                                    sbStatus.append(TextUtils.isEmpty(valueStatus) ? "0," : valueStatus + ",");
                                                }
                                            }
                                            formData.dataArray[i][j] = sb.toString();
                                            formData.dataStatus[i][j] = sbStatus.toString();
                                        }
                                    }
                                }
                            }
                        } else {//单排数据解析方法
                            formData.keys.add(bean.key);
                            if (excelData.growthRankListDTO != null) {
                                JsonObject jsonObject = GsonUtil.beanToJsonObject(excelData.growthRankListDTO);
                                formData.titleDatas[i] = (jsonObject.get(bean.key) == null || TextUtils.isEmpty(jsonObject.get(bean.key).getAsString())) ? "" : jsonObject.get(bean.key).getAsString();
                                formData.headParamsBean = GsonUtil.getEntity(jsonObject.toString(), ExcelParamsBean.class);
                                if (formData.headParamsBean != null) {
                                    if (formData.filterDTOBean != null) {
                                        formData.filterDTOBean.filterNum = formData.headParamsBean.num;
                                        formData.filterDTOBean.filterOrgName = formData.headParamsBean.orgName;
                                    }
                                    if (formData.headParamsBean.jumpDTO != null) {
                                        formData.isDetails = true;
                                        JsonObject obj = GsonUtil.beanToJsonObject(formData.headParamsBean.jumpDTO.paramMap);
                                        HashMap<String, String> params = new HashMap<>();
                                        if (obj != null) {
                                            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                                                if (jsonObject.get(entry.getKey()) != null)
                                                    params.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
                                                else
                                                    params.put(entry.getKey(), entry.getValue().getAsString());
                                            }
                                            formData.headParamsBean.jumpDTO.params = params;
                                        }
                                    }
                                    if (formData.headParamsBean.drillDownDTO != null) {
                                        JsonObject obj = GsonUtil.beanToJsonObject(formData.headParamsBean.drillDownDTO.paramMap);
                                        if (obj != null) {
                                            HashMap<String, String> params = new HashMap<>();
                                            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                                                if (jsonObject.get(entry.getKey()) != null)
                                                    params.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
                                                else
                                                    params.put(entry.getKey(), entry.getValue().getAsString());
                                            }
                                            formData.headParamsBean.drillDownDTO.params = params;
                                        }
                                    }
                                }
                                formData.isFixed = true;

                            }

                            for (int j = 0; j < cowSize; j++) {
                                if (excelData.growthRankListDTOPageInfo != null) {
                                    if (!ArrayUtils.isEmpty(excelData.growthRankListDTOPageInfo.list) && excelData.growthRankListDTOPageInfo.list.size() > j) {
                                        List<Object> jsonStrList = excelData.growthRankListDTOPageInfo.list;
                                        if (jsonStrList.get(j) != null) {
                                            JsonObject jsonObject = GsonUtil.beanToJsonObject(jsonStrList.get(j));
                                            formData.dataArray[i][j] = (jsonObject.get(bean.key) == null || TextUtils.isEmpty(jsonObject.get(bean.key).getAsString())) ? "" : jsonObject.get(bean.key).getAsString();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int j = 0; j < cowSize; j++) {
            if (excelData.growthRankListDTOPageInfo != null) {
                if (!ArrayUtils.isEmpty(excelData.growthRankListDTOPageInfo.list) && excelData.growthRankListDTOPageInfo.list.size() > j) {
                    List<Object> jsonStrList = excelData.growthRankListDTOPageInfo.list;
                    if (jsonStrList.get(j) != null) {
                        JsonObject jsonObject = GsonUtil.beanToJsonObject(jsonStrList.get(j));
                        ExcelParamsBean paramsBean = GsonUtil.getEntity(jsonObject.toString(), ExcelParamsBean.class);
                        if (paramsBean != null) {
                            if (paramsBean.jumpDTO != null) {
                                formData.isDetails = true;
                                JsonObject obj = GsonUtil.beanToJsonObject(paramsBean.jumpDTO.paramMap);
                                HashMap<String, String> params = new HashMap<>();
                                if (obj != null) {
                                    for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                                        if (jsonObject.get(entry.getKey()) != null)
                                            params.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
                                        else
                                            params.put(entry.getKey(), entry.getValue().getAsString());
                                    }
                                    paramsBean.jumpDTO.params = params;
                                }
                            }
                            if (paramsBean.drillDownDTO != null) {
                                JsonObject obj = GsonUtil.beanToJsonObject(paramsBean.drillDownDTO.paramMap);
                                if (obj != null) {
                                    HashMap<String, String> params = new HashMap<>();
                                    for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                                        if (jsonObject.get(entry.getKey()) != null)
                                            params.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
                                        else
                                            params.put(entry.getKey(), entry.getValue().getAsString());
                                    }
                                    paramsBean.drillDownDTO.params = params;
                                }
                            }
                        }
                        formData.paramsBeans.add(paramsBean);
                    }
                }
            }
        }

        return formData;
    }

    //标题添加一条数据
    public static String[] addTableTitles(String[] titles, boolean isFormat) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length + 1];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                if (i == 0) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titleFormat(titles[i - 1]);// mTitles[i-1];
                }
            } else {
                if (i == 0) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titles[i - 1];// mTitles[i-1];
                }
            }
        }
        return tempTitles;
    }

    //标题添加一条数据  添加第三列图标列
    public static String[] addTableDetailsTitles(String[] titles, boolean isFormat) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length + 2];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                if (i == 0) {
                    tempTitles[i] = "";
                } else if (i == 1) {
                    tempTitles[i] = titleFormat(titles[i - 1]);
                } else if (i == 2) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titleFormat(titles[i - 2]);// mTitles[i-1];
                }
            } else {
                if (i == 0) {
                    tempTitles[i] = "";
                } else if (i == 1) {
                    tempTitles[i] = titles[i - 1];// mTitles[i-1];
                } else if (i == 2) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titles[i - 2];// mTitles[i-1];
                }
            }
        }
        return tempTitles;
    }

    //标题添加一条数据  添加第三列图标列
    public static String[] addTableDetailsTitles(String[] titles, boolean isFormat, int count) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length + 2];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                if (i == 0) {
                    tempTitles[i] = "";
                } else if (i == 1) {
                    tempTitles[i] = titleFormat(titles[i - 1], count);
                } else if (i == 2) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titleFormat(titles[i - 2], count);// mTitles[i-1];
                }
            } else {
                if (i == 0) {
                    tempTitles[i] = "";
                } else if (i == 1) {
                    tempTitles[i] = titles[i - 1];// mTitles[i-1];
                } else if (i == 2) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titles[i - 2];// mTitles[i-1];
                }
            }
        }
        return tempTitles;
    }

    //标题添加一条数据
    public static String[] addTableNoSortTitles(String[] titles, boolean isFormat) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                tempTitles[i] = titleFormat(titles[i]);// mTitles[i-1];
            } else {
                tempTitles[i] = titles[i];// mTitles[i-1];
            }
        }
        return tempTitles;
    }

    //标题添加一条数据 没有排序,存在商品分析入口
    public static String[] addTableNoSortDetailsTitles(String[] titles, boolean isFormat) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length + 1];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                if (i == 0)
                    tempTitles[i] = titleFormat(titles[i]);
                else if (i == 1)
                    tempTitles[i] = "";
                else
                    tempTitles[i] = titleFormat(titles[i - 1]);// mTitles[i-1];
            } else {
                if (i == 0)
                    tempTitles[i] = titles[i];
                else if (i == 1)
                    tempTitles[i] = "";
                else
                    tempTitles[i] = titles[i - 1];// mTitles[i-1];
            }
        }
        return tempTitles;
    }

    //标题添加一条数据 没有排序,存在商品分析入口
    public static String[] addTableNoSortDetailsTitles(String[] titles, boolean isFormat, int count) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length + 1];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                if (i == 0)
                    tempTitles[i] = titleFormat(titles[i], count);
                else if (i == 1)
                    tempTitles[i] = "";
                else
                    tempTitles[i] = titleFormat(titles[i - 1], count);// mTitles[i-1];
            } else {
                if (i == 0)
                    tempTitles[i] = titles[i];
                else if (i == 1)
                    tempTitles[i] = "";
                else
                    tempTitles[i] = titles[i - 1];// mTitles[i-1];
            }
        }
        return tempTitles;
    }

    //标题添加一条数据
    public static String[] addTableTitles(String[] titles, boolean isFormat, int count) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length + 1];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                if (i == 0) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titleFormat(titles[i - 1], count);// mTitles[i-1];
                }
            } else {
                if (i == 0) {
                    tempTitles[i] = "";
                } else {
                    tempTitles[i] = titles[i - 1];// mTitles[i-1];
                }
            }
        }
        return tempTitles;
    }

    //标题添加一条数据
    public static String[] addTableNoSortTitles(String[] titles, boolean isFormat, int count) {
        //数据添加一列排序
        String[] tempTitles = new String[titles.length];
        for (int i = 0; i < tempTitles.length; i++) {
            if (isFormat) {
                tempTitles[i] = titleFormat(titles[i], count);// mTitles[i-1];
            } else {
                tempTitles[i] = titles[i];// mTitles[i-1];
            }
        }
        return tempTitles;
    }

    //标题数据添加方法
    public static String[] addTableTitleDatas(String[] titles) {
        //数据添加一列排序
        if (!ArrayUtils.isEmpty(titles)) {
            String[] tempTitleDatas = new String[titles.length + 1];
            for (int i = 0; i < tempTitleDatas.length; i++) {
                if (i == 0) {
                    tempTitleDatas[i] = "";
                } else {
                    tempTitleDatas[i] = titles[i - 1];// mTitles[i-1];
                }
            }
            return tempTitleDatas;
        } else {
            return null;
        }
    }

    //标题数据添加方法 添加第三列图标列
    public static String[] addTableDetailsTitleDatas(String[] titles) {
        //数据添加一列排序
        String[] tempTitleDatas = new String[titles.length + 2];
        for (int i = 0; i < tempTitleDatas.length; i++) {
            if (i == 0) {
                tempTitleDatas[i] = "";
            } else if (i == 1) {
                tempTitleDatas[i] = titles[i - 1];
            } else if (i == 2) {
                tempTitleDatas[i] = "";
            } else {
                tempTitleDatas[i] = titles[i - 2];// mTitles[i-1];
            }
        }
        return tempTitleDatas;
    }

    //标题数据添加方法 添加第二列图标列 没有排序列
    public static String[] addTableNoSortDetailsTitleDatas(String[] titles) {
        //数据添加一列排序
        String[] tempTitleDatas = new String[titles.length + 1];
        for (int i = 0; i < tempTitleDatas.length; i++) {
            if (i == 0) {
                tempTitleDatas[i] = titles[i];
            } else if (i == 1) {
                tempTitleDatas[i] = "";
            } else {
                tempTitleDatas[i] = titles[i - 1];// mTitles[i-1];
            }
        }
        return tempTitleDatas;
    }

    //标题数据添加方法
    public static String[] addTableTitleNoSortNoDetailsDatas(String[] titles) {
        //数据添加一列排序
        String[] tempTitleDatas = new String[titles.length];
        for (int i = 0; i < tempTitleDatas.length; i++) {
            tempTitleDatas[i] = titles[i];// mTitles[i-1];
        }
        return tempTitleDatas;
    }

    //标题数据添加方法
    public static String[] addTableTitleNoSortDetailsDatas(String[] titles) {
        //数据添加一列排序
        String[] tempTitleDatas = new String[titles.length + 1];
        for (int i = 0; i < tempTitleDatas.length; i++) {
            if (i == 0) {
                tempTitleDatas[i] = titles[i];
            } else if (i == 1) {
                tempTitleDatas[i] = "";
            } else {
                tempTitleDatas[i] = titles[i - 1];// mTitles[i-1];
            }
        }
        return tempTitleDatas;
    }

    //第一条不固定的时候 数据正序添加方法
    public static String[][] addTableDatas(String[][] datas) {
        String[][] tempDatas = new String[datas.length + 1][datas[0].length];
        for (int i = 0; i < tempDatas.length; i++) {
            int count = 1;
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (i == 0) {
                    tempDatas[i][j] = String.valueOf(count);
                    count++;
                } else {
                    tempDatas[i][j] = datas[i - 1][j];
                }
            }
        }
        return tempDatas;
    }

    //第一条不固定的时候 数据正序添加方法 添加第三列图标列
    public static String[][] addTableDetailsDatas(String[][] datas) {
        String[][] tempDatas = new String[datas.length + 2][datas[0].length];
        for (int i = 0; i < tempDatas.length; i++) {
            int count = 1;
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (i == 0) {
                    tempDatas[i][j] = String.valueOf(count);
                    count++;
                } else if (i == 1) {
                    tempDatas[i][j] = datas[i - 1][j];
                } else if (i == 2) {
                    tempDatas[i][j] = "";
                } else {
                    tempDatas[i][j] = datas[i - 2][j];
                }
            }
        }
        return tempDatas;
    }

    //第一条不固定的时候 数据正序添加方法
    public static String[][] addTableNoSortDatas(String[][] datas) {
        String[][] tempDatas = new String[datas.length][datas[0].length];
        for (int i = 0; i < tempDatas.length; i++) {
            for (int j = 0; j < tempDatas[0].length; j++) {
                tempDatas[i][j] = datas[i][j];
            }
        }
        return tempDatas;
    }

    //第一条不固定的时候 数据正序添加方法
    public static String[][] addTableNoSortDetailsDatas(String[][] datas) {
        String[][] tempDatas = new String[datas.length + 1][datas[0].length];
        for (int i = 0; i < tempDatas.length; i++) {
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (i == 0)
                    tempDatas[i][j] = datas[i][j];
                else if (i == 1)
                    tempDatas[i][j] = "";
                else
                    tempDatas[i][j] = datas[i - 1][j];
            }
        }
        return tempDatas;
    }

    //第一条固定的时候 数据正序添加方法
    public static String[][] addTableNoSortDatas(String[] titleDatas, String[][] datas) {
        String[][] tempDatas = new String[datas.length][datas[0].length + 1];
        for (int i = 0; i < tempDatas.length; i++) {
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (j == 0) {
                    tempDatas[i][j] = titleDatas[i];
                } else {
                    tempDatas[i][j] = datas[i][j - 1];
                }
            }
        }
        return tempDatas;
    }

    //第一条固定的时候 数据正序添加方法
    public static String[][] addTableDatas(String[] titleDatas, String[][] datas) {
        String[][] tempDatas = new String[datas.length + 1][datas[0].length + 1];
        for (int i = 0; i < tempDatas.length; i++) {
            int count = 0;
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (i == 0) {
                    tempDatas[i][j] = String.valueOf(count);
                    count++;
                } else {
                    if (j == 0) {
                        tempDatas[i][j] = titleDatas[i - 1];
                    } else {
                        tempDatas[i][j] = datas[i - 1][j - 1];
                    }
                }
            }
        }
        return tempDatas;
    }

    //第一条固定的时候 数据倒序添加方法
    public static String[][] addTableDatasRev(String[] titleDatas, String[][] datas, int total) {
        String[][] tempDatas = new String[datas.length + 1][datas[0].length + 1];
        for (int i = 0; i < tempDatas.length; i++) {
            int count = total;
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (i == 0) {
                    if (j == 0)
                        tempDatas[i][j] = String.valueOf(0);
                    else {
                        tempDatas[i][j] = String.valueOf(count);
                        count--;
                    }
                } else {
                    if (j == 0) {
                        tempDatas[i][j] = titleDatas[i - 1];
                    } else {
                        tempDatas[i][j] = datas[i - 1][j - 1];
                    }
                }
            }
        }
        return tempDatas;
    }

    //第一条不固定的时候 数据倒序添加方法
    public static String[][] addTableDatasRev(String[][] datas, int total) {
        String[][] tempDatas = new String[datas.length + 1][datas[0].length];
        for (int i = 0; i < tempDatas.length; i++) {
            int count = total;
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (i == 0) {
                    tempDatas[i][j] = String.valueOf(count);
                    count--;
                } else {
                    tempDatas[i][j] = datas[i - 1][j];
                }
            }
        }
        return tempDatas;
    }

    //第一条不固定的时候 数据倒序添加方法 添加第三列图标列
    public static String[][] addTableDetailsDatasRev(String[][] datas, int total) {
        String[][] tempDatas = new String[datas.length + 2][datas[0].length];
        for (int i = 0; i < tempDatas.length; i++) {
            int count = total;
            for (int j = 0; j < tempDatas[0].length; j++) {
                if (i == 0) {
                    tempDatas[i][j] = String.valueOf(count);
                    count--;
                } else if (i == 1) {
                    tempDatas[i][j] = datas[i - 1][j];
                } else if (i == 2) {
                    tempDatas[i][j] = "";
                } else {
                    tempDatas[i][j] = datas[i - 2][j];
                }
            }
        }
        return tempDatas;
    }

    public static String titleFormat(String title, int size) {
        String formatTitle = title;
        if (!TextUtils.isEmpty(title)) {
            if (title.length() > size * 2)
                title = title.substring(0, (size * 2 - 1)) + "…";
            if (title.length() > size) {
                int count = title.length() % size == 0 ? title.length() / size : title.length() / size + 1;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < count; i++) {
                    String substring;
                    if (i == count - 1) {
                        substring = title.substring(size * i, title.length());
                        sb.append(substring);
                    } else {
                        substring = title.substring(size * i, size * (i + 1));
                        sb.append(substring);
                        sb.append("\n");
                    }
                }
                formatTitle = sb.toString();
            }
        }
        return formatTitle;
    }

    public static String titleFormat(String title) {
        String formatTitle = title;
        if (!TextUtils.isEmpty(title)) {
            if (title.length() > 6)
                title = title.substring(0, 5) + "…";
            if (title.length() > 3) {
                int count = title.length() % 3 == 0 ? title.length() / 3 : title.length() / 3 + 1;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < count; i++) {
                    String substring;
                    if (i == count - 1) {
                        substring = title.substring(3 * i, title.length());
                        sb.append(substring);
                    } else {
                        substring = title.substring(3 * i, 3 * (i + 1));
                        sb.append(substring);
                        sb.append("\n");
                    }
                }
                formatTitle = sb.toString();
            }
        }
        return formatTitle;
    }

    public static String[][] mergeArray(String[][] arryFirst, String[][] arryTwo) {
        String[][] total = null;
        if (arryFirst != null && arryTwo != null) {
            List<List<String>> list = new ArrayList<>();
            for (int i = 0; i < arryFirst.length; i++) {//循环添加每列数据
                List<String> tempList = new ArrayList<>();
                for (int j = 0; j < arryFirst[i].length; j++) {
                    tempList.add(TextUtils.isEmpty(arryFirst[i][j]) ? "" : arryFirst[i][j]);
                }
                for (int j = 0; j < arryTwo[i].length; j++) {
                    tempList.add(TextUtils.isEmpty(arryTwo[i][j]) ? "" : arryTwo[i][j]);
                }
                list.add(tempList);
            }
            if (!ArrayUtils.isEmpty(list)) {
                total = new String[list.size()][list.get(0).size()];
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < list.get(i).size(); j++) {
                        total[i][j] = list.get(i).get(j);
                    }
                }
                return total;
            }
        }
        return total;
    }
}
