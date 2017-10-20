package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class VideoListInfo implements Serializable {

    private static final long serialVersionUID = 123456788L;

    private int count;
    private int total;
    private int refreshCount;
    private String nextPageUrl;
    private List<Video> itemList;

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

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public List<Video> getItemList() {
        return itemList;
    }

    public void setItemList(List<Video> itemList) {
        this.itemList = itemList;
    }
    public static class Video {
        /**
         * type : video
         * data : {"dataType":"VideoBeanForClient","id":45784,"title":"圣典传媒企业见证片","slogan":null,"description":"圣典传媒企业见证片，圣典传媒，专业的企业宣传片拍摄制作公司。服务项目包括企业宣传片,企业品牌形象片, 企业招商片,企业文化片,企业产品片,企业见证片企业，微电影，大型演唱会承办、企业明星代言等。","provider":{"name":"PGC","alias":"PGC","icon":""},"category":"广告","author":{"id":1234,"icon":"http://img.kaiyanapp.com/6523a507dcccb28dc9fdea801c9e7f4b.png?imageMogr2/quality/60/format/jpg","name":"圣典传媒","description":"圣典传媒，专业的企业宣传片拍摄制作公司。服务项目包括企业宣传片,企业品牌形象片, 企业招商片,企业文化片,企业产品片,企业见证片企业，微电影，大型演唱会承办、企业明星代言等。","link":"","latestReleaseTime":1503476192000,"videoNum":21,"adTrack":null,"follow":{"itemType":"author","itemId":1234,"followed":false},"shield":{"itemType":"author","itemId":1234,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true},"cover":{"feed":"http://img.kaiyanapp.com/1f7ee51c324009cfced4172070564784.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/1f7ee51c324009cfced4172070564784.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/6f6b2ef467af60c6504b07c0abd37126.jpeg?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":null},"playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=default&source=qcloud","thumbPlayUrl":null,"duration":237,"webUrl":{"raw":"http://www.eyepetizer.net/detail.html?vid=45784","forWeibo":"http://wandou.im/3o6qrn"},"releaseTime":1503476192000,"library":"BLOCK","playInfo":[{"height":480,"width":854,"urlList":[{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=qcloud","size":21906753},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=ucloud","size":21906753}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=qcloud"},{"height":720,"width":1280,"urlList":[{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=high&source=qcloud","size":39277495},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=high&source=ucloud","size":39277495}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=high&source=qcloud"}],"consumption":{"collectionCount":0,"shareCount":0,"replyCount":1},"campaign":null,"waterMarks":null,"adTrack":null,"tags":[],"type":"NORMAL","titlePgc":"圣典传媒企业见证片","descriptionPgc":"圣典传媒企业见证片，圣典传媒，专业的企业宣传片拍摄制作公司。服务项目包括企业宣传片,企业品牌形象片, 企业招商片,企业文化片,企业产品片,企业见证片企业，微电影，大型演唱会承办、企业明星代言等。","remark":"","idx":0,"shareAdTrack":null,"favoriteAdTrack":null,"webAdTrack":null,"date":1503476192000,"promotion":null,"label":null,"labelList":[],"descriptionEditor":"","collected":false,"played":false,"subtitles":[],"lastViewTime":null,"playlists":null}
         * tag : null
         */
        private String type;
        private VideoData data;
        private Object tag;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public VideoData getData() {
            return data;
        }

        public void setData(VideoData data) {
            this.data = data;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
        public static class VideoData {
            /**
             * dataType : VideoBeanForClient
             * id : 45784
             * title : 圣典传媒企业见证片
             * slogan : null
             * description : 圣典传媒企业见证片，圣典传媒，专业的企业宣传片拍摄制作公司。服务项目包括企业宣传片,企业品牌形象片, 企业招商片,企业文化片,企业产品片,企业见证片企业，微电影，大型演唱会承办、企业明星代言等。
             * provider : {"name":"PGC","alias":"PGC","icon":""}
             * category : 广告
             * author : {"id":1234,"icon":"http://img.kaiyanapp.com/6523a507dcccb28dc9fdea801c9e7f4b.png?imageMogr2/quality/60/format/jpg","name":"圣典传媒","description":"圣典传媒，专业的企业宣传片拍摄制作公司。服务项目包括企业宣传片,企业品牌形象片, 企业招商片,企业文化片,企业产品片,企业见证片企业，微电影，大型演唱会承办、企业明星代言等。","link":"","latestReleaseTime":1503476192000,"videoNum":21,"adTrack":null,"follow":{"itemType":"author","itemId":1234,"followed":false},"shield":{"itemType":"author","itemId":1234,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true}
             * cover : {"feed":"http://img.kaiyanapp.com/1f7ee51c324009cfced4172070564784.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/1f7ee51c324009cfced4172070564784.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/6f6b2ef467af60c6504b07c0abd37126.jpeg?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":null}
             * playUrl : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=default&source=qcloud
             * thumbPlayUrl : null
             * duration : 237
             * webUrl : {"raw":"http://www.eyepetizer.net/detail.html?vid=45784","forWeibo":"http://wandou.im/3o6qrn"}
             * releaseTime : 1503476192000
             * library : BLOCK
             * playInfo : [{"height":480,"width":854,"urlList":[{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=qcloud","size":21906753},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=ucloud","size":21906753}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=qcloud"},{"height":720,"width":1280,"urlList":[{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=high&source=qcloud","size":39277495},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=high&source=ucloud","size":39277495}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=high&source=qcloud"}]
             * consumption : {"collectionCount":0,"shareCount":0,"replyCount":1}
             * campaign : null
             * waterMarks : null
             * adTrack : null
             * tags : []
             * type : NORMAL
             * titlePgc : 圣典传媒企业见证片
             * descriptionPgc : 圣典传媒企业见证片，圣典传媒，专业的企业宣传片拍摄制作公司。服务项目包括企业宣传片,企业品牌形象片, 企业招商片,企业文化片,企业产品片,企业见证片企业，微电影，大型演唱会承办、企业明星代言等。
             * remark :
             * idx : 0
             * shareAdTrack : null
             * favoriteAdTrack : null
             * webAdTrack : null
             * date : 1503476192000
             * promotion : null
             * label : null
             * labelList : []
             * descriptionEditor :
             * collected : false
             * played : false
             * subtitles : []
             * lastViewTime : null
             * playlists : null
             */

            private String dataType;
            private int id;
            private Object slogan;
            private String actionUrl;
            private String description;
            private ProviderBean provider;
            private String category;
            private AuthorBean author;
            private CoverBean cover;
            private String playUrl;
            private Object thumbPlayUrl;
            private int duration;
            private WebUrlBean webUrl;
            private long releaseTime;
            private String library;
            private ConsumptionBean consumption;
            private Object campaign;
            private Object waterMarks;
            private Object adTrack;
            private String type;
            private String titlePgc;
            private String descriptionPgc;
            private String remark;
            private int idx;
            private Object shareAdTrack;
            private Object favoriteAdTrack;
            private Object webAdTrack;
            private long date;
            private Object promotion;
            private Object label;
            private String descriptionEditor;
            private boolean collected;
            private boolean played;
            private Object lastViewTime;
            private Object playlists;
            private List<PlayInfoBean> playInfo;
            private List<?> tags;
            private List<?> labelList;
            private List<?> subtitles;
            private String title;
            private String text;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getActionUrl() {
                return actionUrl;
            }

            public void setActionUrl(String actionUrl) {
                this.actionUrl = actionUrl;
            }

            public String getDataType() {
                return dataType;
            }

            public void setDataType(String dataType) {
                this.dataType = dataType;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getSlogan() {
                return slogan;
            }

            public void setSlogan(Object slogan) {
                this.slogan = slogan;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public ProviderBean getProvider() {
                return provider;
            }

            public void setProvider(ProviderBean provider) {
                this.provider = provider;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public AuthorBean getAuthor() {
                return author;
            }

            public void setAuthor(AuthorBean author) {
                this.author = author;
            }

            public CoverBean getCover() {
                return cover;
            }

            public void setCover(CoverBean cover) {
                this.cover = cover;
            }

            public String getPlayUrl() {
                return playUrl;
            }

            public void setPlayUrl(String playUrl) {
                this.playUrl = playUrl;
            }

            public Object getThumbPlayUrl() {
                return thumbPlayUrl;
            }

            public void setThumbPlayUrl(Object thumbPlayUrl) {
                this.thumbPlayUrl = thumbPlayUrl;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public WebUrlBean getWebUrl() {
                return webUrl;
            }

            public void setWebUrl(WebUrlBean webUrl) {
                this.webUrl = webUrl;
            }

            public long getReleaseTime() {
                return releaseTime;
            }

            public void setReleaseTime(long releaseTime) {
                this.releaseTime = releaseTime;
            }

            public String getLibrary() {
                return library;
            }

            public void setLibrary(String library) {
                this.library = library;
            }

            public ConsumptionBean getConsumption() {
                return consumption;
            }

            public void setConsumption(ConsumptionBean consumption) {
                this.consumption = consumption;
            }

            public Object getCampaign() {
                return campaign;
            }

            public void setCampaign(Object campaign) {
                this.campaign = campaign;
            }

            public Object getWaterMarks() {
                return waterMarks;
            }

            public void setWaterMarks(Object waterMarks) {
                this.waterMarks = waterMarks;
            }

            public Object getAdTrack() {
                return adTrack;
            }

            public void setAdTrack(Object adTrack) {
                this.adTrack = adTrack;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitlePgc() {
                return titlePgc;
            }

            public void setTitlePgc(String titlePgc) {
                this.titlePgc = titlePgc;
            }

            public String getDescriptionPgc() {
                return descriptionPgc;
            }

            public void setDescriptionPgc(String descriptionPgc) {
                this.descriptionPgc = descriptionPgc;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getIdx() {
                return idx;
            }

            public void setIdx(int idx) {
                this.idx = idx;
            }

            public Object getShareAdTrack() {
                return shareAdTrack;
            }

            public void setShareAdTrack(Object shareAdTrack) {
                this.shareAdTrack = shareAdTrack;
            }

            public Object getFavoriteAdTrack() {
                return favoriteAdTrack;
            }

            public void setFavoriteAdTrack(Object favoriteAdTrack) {
                this.favoriteAdTrack = favoriteAdTrack;
            }

            public Object getWebAdTrack() {
                return webAdTrack;
            }

            public void setWebAdTrack(Object webAdTrack) {
                this.webAdTrack = webAdTrack;
            }

            public long getDate() {
                return date;
            }

            public void setDate(long date) {
                this.date = date;
            }

            public Object getPromotion() {
                return promotion;
            }

            public void setPromotion(Object promotion) {
                this.promotion = promotion;
            }

            public Object getLabel() {
                return label;
            }

            public void setLabel(Object label) {
                this.label = label;
            }

            public String getDescriptionEditor() {
                return descriptionEditor;
            }

            public void setDescriptionEditor(String descriptionEditor) {
                this.descriptionEditor = descriptionEditor;
            }

            public boolean isCollected() {
                return collected;
            }

            public void setCollected(boolean collected) {
                this.collected = collected;
            }

            public boolean isPlayed() {
                return played;
            }

            public void setPlayed(boolean played) {
                this.played = played;
            }

            public Object getLastViewTime() {
                return lastViewTime;
            }

            public void setLastViewTime(Object lastViewTime) {
                this.lastViewTime = lastViewTime;
            }

            public Object getPlaylists() {
                return playlists;
            }

            public void setPlaylists(Object playlists) {
                this.playlists = playlists;
            }

            public List<PlayInfoBean> getPlayInfo() {
                return playInfo;
            }

            public void setPlayInfo(List<PlayInfoBean> playInfo) {
                this.playInfo = playInfo;
            }

            public List<?> getTags() {
                return tags;
            }

            public void setTags(List<?> tags) {
                this.tags = tags;
            }

            public List<?> getLabelList() {
                return labelList;
            }

            public void setLabelList(List<?> labelList) {
                this.labelList = labelList;
            }

            public List<?> getSubtitles() {
                return subtitles;
            }

            public void setSubtitles(List<?> subtitles) {
                this.subtitles = subtitles;
            }

            public static class ProviderBean {
                /**
                 * name : PGC
                 * alias : PGC
                 * icon :
                 */

                private String name;
                private String alias;
                private String icon;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }
            }

            public static class AuthorBean {
                /**
                 * id : 1234
                 * icon : http://img.kaiyanapp.com/6523a507dcccb28dc9fdea801c9e7f4b.png?imageMogr2/quality/60/format/jpg
                 * name : 圣典传媒
                 * description : 圣典传媒，专业的企业宣传片拍摄制作公司。服务项目包括企业宣传片,企业品牌形象片, 企业招商片,企业文化片,企业产品片,企业见证片企业，微电影，大型演唱会承办、企业明星代言等。
                 * link :
                 * latestReleaseTime : 1503476192000
                 * videoNum : 21
                 * adTrack : null
                 * follow : {"itemType":"author","itemId":1234,"followed":false}
                 * shield : {"itemType":"author","itemId":1234,"shielded":false}
                 * approvedNotReadyVideoCount : 0
                 * ifPgc : true
                 */

                private int id;
                private String icon;
                private String name;
                private String description;
                private String link;
                private long latestReleaseTime;
                private int videoNum;
                private Object adTrack;
                private FollowBean follow;
                private ShieldBean shield;
                private int approvedNotReadyVideoCount;
                private boolean ifPgc;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public long getLatestReleaseTime() {
                    return latestReleaseTime;
                }

                public void setLatestReleaseTime(long latestReleaseTime) {
                    this.latestReleaseTime = latestReleaseTime;
                }

                public int getVideoNum() {
                    return videoNum;
                }

                public void setVideoNum(int videoNum) {
                    this.videoNum = videoNum;
                }

                public Object getAdTrack() {
                    return adTrack;
                }

                public void setAdTrack(Object adTrack) {
                    this.adTrack = adTrack;
                }

                public FollowBean getFollow() {
                    return follow;
                }

                public void setFollow(FollowBean follow) {
                    this.follow = follow;
                }

                public ShieldBean getShield() {
                    return shield;
                }

                public void setShield(ShieldBean shield) {
                    this.shield = shield;
                }

                public int getApprovedNotReadyVideoCount() {
                    return approvedNotReadyVideoCount;
                }

                public void setApprovedNotReadyVideoCount(int approvedNotReadyVideoCount) {
                    this.approvedNotReadyVideoCount = approvedNotReadyVideoCount;
                }

                public boolean isIfPgc() {
                    return ifPgc;
                }

                public void setIfPgc(boolean ifPgc) {
                    this.ifPgc = ifPgc;
                }

                public static class FollowBean {
                    /**
                     * itemType : author
                     * itemId : 1234
                     * followed : false
                     */

                    private String itemType;
                    private int itemId;
                    private boolean followed;

                    public String getItemType() {
                        return itemType;
                    }

                    public void setItemType(String itemType) {
                        this.itemType = itemType;
                    }

                    public int getItemId() {
                        return itemId;
                    }

                    public void setItemId(int itemId) {
                        this.itemId = itemId;
                    }

                    public boolean isFollowed() {
                        return followed;
                    }

                    public void setFollowed(boolean followed) {
                        this.followed = followed;
                    }
                }

                public static class ShieldBean {
                    /**
                     * itemType : author
                     * itemId : 1234
                     * shielded : false
                     */

                    private String itemType;
                    private int itemId;
                    private boolean shielded;

                    public String getItemType() {
                        return itemType;
                    }

                    public void setItemType(String itemType) {
                        this.itemType = itemType;
                    }

                    public int getItemId() {
                        return itemId;
                    }

                    public void setItemId(int itemId) {
                        this.itemId = itemId;
                    }

                    public boolean isShielded() {
                        return shielded;
                    }

                    public void setShielded(boolean shielded) {
                        this.shielded = shielded;
                    }
                }
            }

            public static class CoverBean {
                /**
                 * feed : http://img.kaiyanapp.com/1f7ee51c324009cfced4172070564784.png?imageMogr2/quality/60/format/jpg
                 * detail : http://img.kaiyanapp.com/1f7ee51c324009cfced4172070564784.png?imageMogr2/quality/60/format/jpg
                 * blurred : http://img.kaiyanapp.com/6f6b2ef467af60c6504b07c0abd37126.jpeg?imageMogr2/quality/60/format/jpg
                 * sharing : null
                 * homepage : null
                 */

                private String feed;
                private String detail;
                private String blurred;
                private Object sharing;
                private Object homepage;

                public String getFeed() {
                    return feed;
                }

                public void setFeed(String feed) {
                    this.feed = feed;
                }

                public String getDetail() {
                    return detail;
                }

                public void setDetail(String detail) {
                    this.detail = detail;
                }

                public String getBlurred() {
                    return blurred;
                }

                public void setBlurred(String blurred) {
                    this.blurred = blurred;
                }

                public Object getSharing() {
                    return sharing;
                }

                public void setSharing(Object sharing) {
                    this.sharing = sharing;
                }

                public Object getHomepage() {
                    return homepage;
                }

                public void setHomepage(Object homepage) {
                    this.homepage = homepage;
                }
            }

            public static class WebUrlBean {
                /**
                 * raw : http://www.eyepetizer.net/detail.html?vid=45784
                 * forWeibo : http://wandou.im/3o6qrn
                 */

                private String raw;
                private String forWeibo;

                public String getRaw() {
                    return raw;
                }

                public void setRaw(String raw) {
                    this.raw = raw;
                }

                public String getForWeibo() {
                    return forWeibo;
                }

                public void setForWeibo(String forWeibo) {
                    this.forWeibo = forWeibo;
                }
            }

            public static class ConsumptionBean {
                /**
                 * collectionCount : 0
                 * shareCount : 0
                 * replyCount : 1
                 */

                private int collectionCount;
                private int shareCount;
                private int replyCount;

                public int getCollectionCount() {
                    return collectionCount;
                }

                public void setCollectionCount(int collectionCount) {
                    this.collectionCount = collectionCount;
                }

                public int getShareCount() {
                    return shareCount;
                }

                public void setShareCount(int shareCount) {
                    this.shareCount = shareCount;
                }

                public int getReplyCount() {
                    return replyCount;
                }

                public void setReplyCount(int replyCount) {
                    this.replyCount = replyCount;
                }
            }

            public static class PlayInfoBean {
                /**
                 * height : 480
                 * width : 854
                 * urlList : [{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=qcloud","size":21906753},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=ucloud","size":21906753}]
                 * name : 标清
                 * type : normal
                 * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=qcloud
                 */

                private int height;
                private int width;
                private String name;
                private String type;
                private String url;
                private List<UrlListBean> urlList;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public List<UrlListBean> getUrlList() {
                    return urlList;
                }

                public void setUrlList(List<UrlListBean> urlList) {
                    this.urlList = urlList;
                }

                public static class UrlListBean {
                    /**
                     * name : qcloud
                     * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=45784&editionType=normal&source=qcloud
                     * size : 21906753
                     */

                    private String name;
                    private String url;
                    private int size;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }
                }
            }
        }
    }
}
