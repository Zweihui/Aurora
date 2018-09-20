import 'package:json_annotation/json_annotation.dart';

part 'video.g.dart';

//flutter packages pub run build_runner build

const String VIDEO_ITEM_TYPE_TEXT = "textCard";
const String VIDEO_ITEM_TYPE_FOLLOW = "followCard";

@JsonSerializable()
class VideoListInfo extends Object with _$VideoListInfoSerializerMixin {
  VideoListInfo(this.count, this.total, this.refreshCount, this.nextPageUrl,
      this.itemList);
  int count;
  int total;
  int refreshCount;
  String nextPageUrl;
  List<VideoData> itemList;
  factory VideoListInfo.fromJson(Map<String, dynamic> json) =>
      _$VideoListInfoFromJson(json);
}

@JsonSerializable()
class VideoData extends Object with _$VideoDataSerializerMixin {
  VideoData(this.type, this.data);
  String type;
  Video data;
  factory VideoData.fromJson(Map<String, dynamic> json) =>
      _$VideoDataFromJson(json);
}

@JsonSerializable()
class Video extends Object with _$VideoSerializerMixin{
  Video(
      this.dataType,
      this.id,
      this.actionUrl,
      this.description,
      this.provider,
      this.category,
      this.author,
      this.cover,
      this.playUrl,
      this.duration,
      this.library,
      this.content,
      this.consumption,
      this.type,
      this.titlePgc,
      this.descriptionPgc,
      this.remark,
      this.idx,
      this.date,
      this.descriptionEditor,
      this.collected,
      this.played,
      this.playInfo,
      this.title,
      this.text);
  String dataType;
  int id;
  String actionUrl;
  String description;
  Provider provider;
  String category;
  Author author;
  Cover cover;
  String playUrl;
  int duration;
  String library;
  VideoData content;
  Consumption consumption;
  String type;
  String titlePgc;
  String descriptionPgc;
  String remark;
  int idx;
  int date;
  String descriptionEditor;
  bool collected;
  bool played;
  List<PlayInfo> playInfo;
  String title;
  String text;
  factory Video.fromJson(Map<String, dynamic> json) => _$VideoFromJson(json);
}

@JsonSerializable()
class PlayInfo extends Object with _$PlayInfoSerializerMixin {
  PlayInfo(
      this.height, this.width, this.name, this.type, this.url, this.urlList);
  int height;
  int width;
  String name;
  String type;
  String url;
  List<UrlList> urlList;
  factory PlayInfo.fromJson(Map<String, dynamic> json) =>
      _$PlayInfoFromJson(json);
}

@JsonSerializable()
class Consumption extends Object with _$ConsumptionSerializerMixin {
  Consumption(this.collectionCount, this.shareCount, this.replyCount);
  int collectionCount;
  int shareCount;
  int replyCount;
  factory Consumption.fromJson(Map<String, dynamic> json) =>
      _$ConsumptionFromJson(json);
}

@JsonSerializable()
class Cover extends Object with _$CoverSerializerMixin {
  Cover(this.feed, this.detail, this.blurred);
  String feed;
  String detail;
  String blurred;
  factory Cover.fromJson(Map<String, dynamic> json) => _$CoverFromJson(json);
}

@JsonSerializable()
class Author extends Object with _$AuthorSerializerMixin {
  Author(this.id, this.icon, this.name, this.description, this.link,
      this.latestReleaseTime, this.videoNum);
  int id;
  String icon;
  String name;
  String description;
  String link;
  int latestReleaseTime;
  int videoNum;
  int approvedNotReadyVideoCount;
  bool ifPgc;
  factory Author.fromJson(Map<String, dynamic> json) => _$AuthorFromJson(json);
}

@JsonSerializable()
class Provider extends Object with _$ProviderSerializerMixin {
  Provider(this.name, this.alias, this.icon);
  String name;
  String alias;
  String icon;
  factory Provider.fromJson(Map<String, dynamic> json) =>
      _$ProviderFromJson(json);
}

@JsonSerializable()
class UrlList extends Object with _$UrlListSerializerMixin {
  UrlList(this.name, this.url, this.size);
  String name;
  String url;
  int size;
  factory UrlList.fromJson(Map<String, dynamic> json) =>
      _$UrlListFromJson(json);
}
