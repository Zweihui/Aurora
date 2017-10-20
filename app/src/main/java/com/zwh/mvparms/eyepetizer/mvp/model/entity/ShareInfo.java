package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class ShareInfo implements Serializable {

    /**
     * QQ : {"sourceType":"VIDEO","itemType":"WEB_PAGE","sharePlatform":"QQ","title":"塞维利亚街头乱舞 | 开眼 Eyepetizer","description":"塞维利亚街头乱舞乱记","imageUrl":"","link":"http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=qq&uid=0","callbackUrl":null}
     * WEIBO : {"sourceType":"VIDEO","itemType":"WEB_PAGE","sharePlatform":"WEIBO","title":"塞维利亚街头乱舞","description":"#开眼#「塞维利亚街头乱舞」- 塞维利亚街头乱舞乱记 -  @开眼精选视频  | 播放：http://wandou.im/3o988n 下载：http://t.cn/RaTgrT3","imageUrl":"","link":"http://wandou.im/3o988n","callbackUrl":null}
     * OTHERS : {"sourceType":"VIDEO","itemType":"WEB_PAGE","sharePlatform":"OTHERS","title":"塞维利亚街头乱舞 | 开眼 Eyepetizer","description":"塞维利亚街头乱舞乱记","imageUrl":"","link":"http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=others&uid=0","callbackUrl":null}
     * WECHAT_MOMENTS : {"sourceType":"VIDEO","itemType":"WEB_PAGE","sharePlatform":"WECHAT_MOMENTS","title":"塞维利亚街头乱舞 | 开眼 Eyepetizer","description":"塞维利亚街头乱舞乱记","imageUrl":"","link":"http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=wechat-moments&uid=0","callbackUrl":null}
     * WECHAT_FRIENDS : {"sourceType":"VIDEO","itemType":"WEB_PAGE","sharePlatform":"WECHAT_FRIENDS","title":"塞维利亚街头乱舞 | 开眼 Eyepetizer","description":"塞维利亚街头乱舞乱记","imageUrl":"","link":"http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=wechat-friends&uid=0","callbackUrl":null}
     */

    @SerializedName("QQ")
    private QQBean qq;
    @SerializedName("WEIBO")
    private WEIBOBean weibo;
    @SerializedName("OTHERS")
    private OTHERSBean others;
    @SerializedName("WECHAT_MOMENTS")
    private WECHATMOMENTSBean wechat_moments;
    @SerializedName("WECHAT_FRIENDS")
    private WECHATFRIENDSBean wechat_friends;

    public QQBean getQq() {
        return qq;
    }

    public void setQq(QQBean qq) {
        this.qq = qq;
    }

    public WEIBOBean getWeibo() {
        return weibo;
    }

    public void setWeibo(WEIBOBean weibo) {
        this.weibo = weibo;
    }

    public OTHERSBean getOthers() {
        return others;
    }

    public void setOthers(OTHERSBean others) {
        this.others = others;
    }

    public WECHATMOMENTSBean getWechat_moments() {
        return wechat_moments;
    }

    public void setWechat_moments(WECHATMOMENTSBean wechat_moments) {
        this.wechat_moments = wechat_moments;
    }

    public WECHATFRIENDSBean getWechat_friends() {
        return wechat_friends;
    }

    public void setWechat_friends(WECHATFRIENDSBean wechat_friends) {
        this.wechat_friends = wechat_friends;
    }

    public static class QQBean {
        /**
         * sourceType : VIDEO
         * itemType : WEB_PAGE
         * sharePlatform : QQ
         * title : 塞维利亚街头乱舞 | 开眼 Eyepetizer
         * description : 塞维利亚街头乱舞乱记
         * imageUrl :
         * link : http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=qq&uid=0
         * callbackUrl : null
         */

        private String sourceType;
        private String itemType;
        private String sharePlatform;
        private String title;
        private String description;
        private String imageUrl;
        private String link;
        private Object callbackUrl;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getSharePlatform() {
            return sharePlatform;
        }

        public void setSharePlatform(String sharePlatform) {
            this.sharePlatform = sharePlatform;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Object getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(Object callbackUrl) {
            this.callbackUrl = callbackUrl;
        }
    }

    public static class WEIBOBean {
        /**
         * sourceType : VIDEO
         * itemType : WEB_PAGE
         * sharePlatform : WEIBO
         * title : 塞维利亚街头乱舞
         * description : #开眼#「塞维利亚街头乱舞」- 塞维利亚街头乱舞乱记 -  @开眼精选视频  | 播放：http://wandou.im/3o988n 下载：http://t.cn/RaTgrT3
         * imageUrl :
         * link : http://wandou.im/3o988n
         * callbackUrl : null
         */

        private String sourceType;
        private String itemType;
        private String sharePlatform;
        private String title;
        private String description;
        private String imageUrl;
        private String link;
        private Object callbackUrl;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getSharePlatform() {
            return sharePlatform;
        }

        public void setSharePlatform(String sharePlatform) {
            this.sharePlatform = sharePlatform;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Object getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(Object callbackUrl) {
            this.callbackUrl = callbackUrl;
        }
    }

    public static class OTHERSBean {
        /**
         * sourceType : VIDEO
         * itemType : WEB_PAGE
         * sharePlatform : OTHERS
         * title : 塞维利亚街头乱舞 | 开眼 Eyepetizer
         * description : 塞维利亚街头乱舞乱记
         * imageUrl :
         * link : http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=others&uid=0
         * callbackUrl : null
         */

        private String sourceType;
        private String itemType;
        private String sharePlatform;
        private String title;
        private String description;
        private String imageUrl;
        private String link;
        private Object callbackUrl;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getSharePlatform() {
            return sharePlatform;
        }

        public void setSharePlatform(String sharePlatform) {
            this.sharePlatform = sharePlatform;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Object getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(Object callbackUrl) {
            this.callbackUrl = callbackUrl;
        }
    }

    public static class WECHATMOMENTSBean {
        /**
         * sourceType : VIDEO
         * itemType : WEB_PAGE
         * sharePlatform : WECHAT_MOMENTS
         * title : 塞维利亚街头乱舞 | 开眼 Eyepetizer
         * description : 塞维利亚街头乱舞乱记
         * imageUrl :
         * link : http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=wechat-moments&uid=0
         * callbackUrl : null
         */

        private String sourceType;
        private String itemType;
        private String sharePlatform;
        private String title;
        private String description;
        private String imageUrl;
        private String link;
        private Object callbackUrl;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getSharePlatform() {
            return sharePlatform;
        }

        public void setSharePlatform(String sharePlatform) {
            this.sharePlatform = sharePlatform;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Object getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(Object callbackUrl) {
            this.callbackUrl = callbackUrl;
        }
    }

    public static class WECHATFRIENDSBean {
        /**
         * sourceType : VIDEO
         * itemType : WEB_PAGE
         * sharePlatform : WECHAT_FRIENDS
         * title : 塞维利亚街头乱舞 | 开眼 Eyepetizer
         * description : 塞维利亚街头乱舞乱记
         * imageUrl :
         * link : http://www.eyepetizer.net/detail.html?vid=48053&utm_campaign=routine&utm_medium=share&utm_source=wechat-friends&uid=0
         * callbackUrl : null
         */

        private String sourceType;
        private String itemType;
        private String sharePlatform;
        private String title;
        private String description;
        private String imageUrl;
        private String link;
        private Object callbackUrl;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getSharePlatform() {
            return sharePlatform;
        }

        public void setSharePlatform(String sharePlatform) {
            this.sharePlatform = sharePlatform;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Object getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(Object callbackUrl) {
            this.callbackUrl = callbackUrl;
        }
    }
}
