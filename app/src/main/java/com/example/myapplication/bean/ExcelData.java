package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.List;

public class ExcelData implements Serializable {

    /**
     * filterType : 71363
     * growthRankListDTO : {"actionType":1,"columnA":"21.2%","columnB":"22.2%","columnC":"23.2%","columnD":"24.2%","columnE":"25.2%","columnF":"21.2%","columnG":"21.2%","columnH":"21.2%","columnI":"21.2%","columnJ":"21.2%","columnK":"21.2%","columnL":"21.2%","num":"10000","orgName":"全国"}
     * growthRankListDTOPageInfo : {"endRow":0,"firstPage":0,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":true,"isLastPage":false,"lastPage":0,"list":[{"drillDownDTO":{"actionUrl":"测试内容el44","paramKeys":["string1","string2","string3","string4","string5"],"requestUrl":"测试内容34j0"},"jumpDTO":{"actionUrl":"测试内容9o88","icon":"测试内容3qii","paramKeys":["string1","string2","string3","string4","string5"],"requestUrl":"测试内容5fyw"},"actionType":1,"columnA":"21.2%","columnB":"22.2%","columnC":"23.2%","columnD":"24.2%","columnE":"25.2%","columnF":"21.2%","columnG":"21.2%","columnH":"21.2%","columnI":"21.2%","columnJ":"21.2%","columnK":"21.2%","columnL":"21.2%","num":"11000","orgName":"西南平台"},{"drillDownDTO":{"actionUrl":"测试内容el44","paramKeys":["string1","string2","string3","string4","string5"],"requestUrl":"测试内容34j0"},"jumpDTO":{"actionUrl":"测试内容9o88","icon":"测试内容3qii","paramKeys":["string1","string2","string3","string4","string5"],"requestUrl":"测试内容5fyw"},"actionType":1,"columnA":"21.2%","columnB":"22.2%","columnC":"23.2%","columnD":"24.2%","columnE":"25.2%","columnF":"21.2%","columnG":"21.2%","columnH":"21.2%","columnI":"21.2%","columnJ":"21.2%","columnK":"21.2%","columnL":"21.2%","num":"11000","orgName":"上海平台"}],"navigateFirstPage":0,"navigateLastPage":0,"navigatePages":0,"nextPage":0,"pageNum":1,"pageSize":5,"pages":1,"prePage":0,"size":0,"startRow":0,"total":0}
     * ralatedInfoDTO : {"hasGoodsInfo":true,"hasRankInfo":true}
     * tableHeadList : [{"isOrderBy":2,"key":"orgName","orderBy":"","value":"组织"},{"isOrderBy":2,"key":"columnA","orderBy":"","value":"49周"},{"isOrderBy":2,"key":"columnB","orderBy":"","value":"50周"},{"isOrderBy":2,"key":"columnC","orderBy":"","value":"51周"},{"isOrderBy":2,"key":"columnD","orderBy":"","value":"52周"}]
     * tableTitle : 列表
     */

    public Object growthRankListDTO;//固定行Object
    public GrowthRankListDTOPageInfoBean growthRankListDTOPageInfo;//数据对象
    public RelatedInfoDTOBean relatedInfoDTO;
    public String tableTitle;
    public List<TableHeadListBean> tableHeadList;
    public TableOrderByDTO tableOrderByDTO;
    public String msgLabel;
    /**
     * filterDTO : {"calendarType":80380,"dropDownItems":[{"id":27131,"title":"测试内容8g58"}],"filterType":38070}
     */

    public FilterDTOBean filterDTO;

    public static class GrowthRankListDTOPageInfoBean implements Serializable {
        public boolean isFirstPage;
        public boolean isLastPage;
        public int lastPage;
        public int pageNum;
        public int pageSize;
        public int total;
        public List<Object> list;
    }

    public static class RelatedInfoDTOBean implements Serializable {
        /**
         * hasGoodsInfo : true
         * hasRankInfo : true
         */

        public boolean hasGoodsInfo;
        public boolean hasRankInfo;
    }


    public static class TableHeadListBean implements Serializable {
        /**
         * isOrderBy : 2
         * key : orgName
         * value : 组织
         */

        public int isOrderBy;
        public String key;
        public String value;
    }

    public static class TableOrderByDTO implements Serializable {
        public int ascendingOrder;//升降序 1升序 2降序
        public String orderBy;//排序字段
    }

    public static class FilterDTOBean implements Serializable {
        /**
         * calendarType : 80380
         * dropDownItems : [{"id":27131,"title":"测试内容8g58"}]
         * filterType : 38070
         */

        public int calendarType;
        public int filterType;
        public List<DropDownItemsBean> dropDownItems;
        public String filterOrgName;
        public String filterNum;

        public static class DropDownItemsBean implements Serializable {
            /**
             * id : 27131
             * title : 测试内容8g58
             */

            public int id;
            public String title;
        }
    }
}
