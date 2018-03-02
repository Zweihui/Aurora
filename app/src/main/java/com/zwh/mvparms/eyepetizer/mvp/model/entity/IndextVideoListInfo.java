package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import com.jess.arms.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15 0015.
 */

public class IndextVideoListInfo implements Serializable {
    private int count;
    private int total;
    private int refreshCount;
    private String nextPageUrl;
    private List<ItemList> itemList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(int refreshCount) {
        this.refreshCount = refreshCount;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public List<ItemList> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemList> itemList) {
        this.itemList = itemList;
    }

    public static class ItemList {
        private String type;
        private DataBean data;
        private Object tag;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public static class DataBean {
            private String dataType;
            private VideoListInfo.Video content;

            public String getDataType() {
                return dataType;
            }

            public void setDataType(String dataType) {
                this.dataType = dataType;
            }

            public VideoListInfo.Video getContent() {
                return content;
            }

            public void setContent(VideoListInfo.Video content) {
                this.content = content;
            }
        }
    }

    public long getDateFromNextPageUrl() {
        if (!StringUtils.isEmpty(nextPageUrl)) {
            String date = nextPageUrl.substring(nextPageUrl.indexOf("date=") + 5, nextPageUrl.indexOf("&"));
            try {
                return Long.parseLong(date);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getNumFromNextPageUrl() {
        if (!StringUtils.isEmpty(nextPageUrl)) {
            String date = nextPageUrl.substring(nextPageUrl.indexOf("num=") + 4, nextPageUrl.length());
            try {
                return Integer.parseInt(date);
            } catch (Exception e) {
                return 0;
            }

        } else {
            return 0;
        }
    }
}
