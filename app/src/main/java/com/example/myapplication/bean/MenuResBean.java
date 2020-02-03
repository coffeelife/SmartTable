package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.List;

public class MenuResBean implements Serializable{

    private boolean hasTips;
    private boolean hasPerformance;
    private List<CommonAppListBean> commonAppList;
    private List<MenuDTOListBean> menuDTOList;

    public boolean isHasPerformance() {
        return hasPerformance;
    }

    public void setHasPerformance(boolean hasPerformance) {
        this.hasPerformance = hasPerformance;
    }

    public boolean isHasTips() {
        return hasTips;
    }

    public void setHasTips(boolean hasTips) {
        this.hasTips = hasTips;
    }

    public List<CommonAppListBean> getCommonAppList() {
        return commonAppList;
    }

    public void setCommonAppList(List<CommonAppListBean> commonAppList) {
        this.commonAppList = commonAppList;
    }

    public List<MenuDTOListBean> getMenuDTOList() {
        return menuDTOList;
    }

    public void setMenuDTOList(List<MenuDTOListBean> menuDTOList) {
        this.menuDTOList = menuDTOList;
    }

    public static class CommonAppListBean implements Serializable{
        /**
         * icon : 1
         * menuType : 0
         * action : http://124.251.111.171:8088/wxapi/wxclientmenu/3e2ec450337849b6b60715ebe7863762
         * id : 466
         * name : 待办
         * type : 2
         */

        private String icon;
        private int menuType;
        private String action;
        private int id;
        private String name;
        private int type;
        private int appTipsCount;

        public int getAppTipsCount() {
            return appTipsCount;
        }

        public void setAppTipsCount(int appTipsCount) {
            this.appTipsCount = appTipsCount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getMenuType() {
            return menuType;
        }

        public void setMenuType(int menuType) {
            this.menuType = menuType;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class MenuDTOListBean implements Serializable{

        private String action;
        private String icon;
        private int id;
        private String name;
        private List<ChildBean> child;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean implements Serializable{
            /**
             * action : 1
             * icon : 1
             * id : 461
             * menuType : false
             * name : 门店业绩
             */

            private String action;
            private String icon;
            private int id;
            private int menuType;
            private String name;
            private boolean isSelect;
            private int type;
            private int appTipsCount;

            public int getAppTipsCount() {
                return appTipsCount;
            }

            public void setAppTipsCount(int appTipsCount) {
                this.appTipsCount = appTipsCount;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMenuType() {
                return menuType;
            }

            public void setMenuType(int menuType) {
                this.menuType = menuType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
