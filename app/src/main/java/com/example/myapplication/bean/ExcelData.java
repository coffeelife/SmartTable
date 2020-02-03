package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.List;

public class ExcelData implements Serializable {

    public Object growthRankListDTO;//固定行Object
    public GrowthRankListDTOPageInfoBean growthRankListDTOPageInfo;//数据对象
    public RelatedInfoDTOBean relatedInfoDTO;
    public String tableTitle;
    public List<TableHeadListBean> tableHeadList;
    public TableOrderByDTO tableOrderByDTO;
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
