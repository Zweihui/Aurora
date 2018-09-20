// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'video.dart';

// **************************************************************************
// Generator: JsonSerializableGenerator
// **************************************************************************

VideoListInfo _$VideoListInfoFromJson(Map<String, dynamic> json) =>
    new VideoListInfo(
        json['count'] as int,
        json['total'] as int,
        json['refreshCount'] as int,
        json['nextPageUrl'] as String,
        (json['itemList'] as List)
            ?.map((e) => e == null
                ? null
                : new VideoData.fromJson(e as Map<String, dynamic>))
            ?.toList());

abstract class _$VideoListInfoSerializerMixin {
  int get count;
  int get total;
  int get refreshCount;
  String get nextPageUrl;
  List<VideoData> get itemList;
  Map<String, dynamic> toJson() => <String, dynamic>{
        'count': count,
        'total': total,
        'refreshCount': refreshCount,
        'nextPageUrl': nextPageUrl,
        'itemList': itemList
      };
}

VideoData _$VideoDataFromJson(Map<String, dynamic> json) => new VideoData(
    json['type'] as String,
    json['data'] == null
        ? null
        : new Video.fromJson(json['data'] as Map<String, dynamic>));

abstract class _$VideoDataSerializerMixin {
  String get type;
  Video get data;
  Map<String, dynamic> toJson() =>
      <String, dynamic>{'type': type, 'data': data};
}

Video _$VideoFromJson(Map<String, dynamic> json) => new Video(
    json['dataType'] as String,
    json['id'] as int,
    json['actionUrl'] as String,
    json['description'] as String,
    json['provider'] == null
        ? null
        : new Provider.fromJson(json['provider'] as Map<String, dynamic>),
    json['category'] as String,
    json['author'] == null
        ? null
        : new Author.fromJson(json['author'] as Map<String, dynamic>),
    json['cover'] == null
        ? null
        : new Cover.fromJson(json['cover'] as Map<String, dynamic>),
    json['playUrl'] as String,
    json['duration'] as int,
    json['library'] as String,
    json['content'] == null
        ? null
        : new VideoData.fromJson(json['content'] as Map<String, dynamic>),
    json['consumption'] == null
        ? null
        : new Consumption.fromJson(json['consumption'] as Map<String, dynamic>),
    json['type'] as String,
    json['titlePgc'] as String,
    json['descriptionPgc'] as String,
    json['remark'] as String,
    json['idx'] as int,
    json['date'] as int,
    json['descriptionEditor'] as String,
    json['collected'] as bool,
    json['played'] as bool,
    (json['playInfo'] as List)
        ?.map((e) =>
            e == null ? null : new PlayInfo.fromJson(e as Map<String, dynamic>))
        ?.toList(),
    json['title'] as String,
    json['text'] as String);

abstract class _$VideoSerializerMixin {
  String get dataType;
  int get id;
  String get actionUrl;
  String get description;
  Provider get provider;
  String get category;
  Author get author;
  Cover get cover;
  String get playUrl;
  int get duration;
  String get library;
  VideoData get content;
  Consumption get consumption;
  String get type;
  String get titlePgc;
  String get descriptionPgc;
  String get remark;
  int get idx;
  int get date;
  String get descriptionEditor;
  bool get collected;
  bool get played;
  List<PlayInfo> get playInfo;
  String get title;
  String get text;
  Map<String, dynamic> toJson() => <String, dynamic>{
        'dataType': dataType,
        'id': id,
        'actionUrl': actionUrl,
        'description': description,
        'provider': provider,
        'category': category,
        'author': author,
        'cover': cover,
        'playUrl': playUrl,
        'duration': duration,
        'library': library,
        'content': content,
        'consumption': consumption,
        'type': type,
        'titlePgc': titlePgc,
        'descriptionPgc': descriptionPgc,
        'remark': remark,
        'idx': idx,
        'date': date,
        'descriptionEditor': descriptionEditor,
        'collected': collected,
        'played': played,
        'playInfo': playInfo,
        'title': title,
        'text': text
      };
}

PlayInfo _$PlayInfoFromJson(Map<String, dynamic> json) => new PlayInfo(
    json['height'] as int,
    json['width'] as int,
    json['name'] as String,
    json['type'] as String,
    json['url'] as String,
    (json['urlList'] as List)
        ?.map((e) =>
            e == null ? null : new UrlList.fromJson(e as Map<String, dynamic>))
        ?.toList());

abstract class _$PlayInfoSerializerMixin {
  int get height;
  int get width;
  String get name;
  String get type;
  String get url;
  List<UrlList> get urlList;
  Map<String, dynamic> toJson() => <String, dynamic>{
        'height': height,
        'width': width,
        'name': name,
        'type': type,
        'url': url,
        'urlList': urlList
      };
}

Consumption _$ConsumptionFromJson(Map<String, dynamic> json) => new Consumption(
    json['collectionCount'] as int,
    json['shareCount'] as int,
    json['replyCount'] as int);

abstract class _$ConsumptionSerializerMixin {
  int get collectionCount;
  int get shareCount;
  int get replyCount;
  Map<String, dynamic> toJson() => <String, dynamic>{
        'collectionCount': collectionCount,
        'shareCount': shareCount,
        'replyCount': replyCount
      };
}

Cover _$CoverFromJson(Map<String, dynamic> json) => new Cover(
    json['feed'] as String,
    json['detail'] as String,
    json['blurred'] as String);

abstract class _$CoverSerializerMixin {
  String get feed;
  String get detail;
  String get blurred;
  Map<String, dynamic> toJson() =>
      <String, dynamic>{'feed': feed, 'detail': detail, 'blurred': blurred};
}

Author _$AuthorFromJson(Map<String, dynamic> json) => new Author(
    json['id'] as int,
    json['icon'] as String,
    json['name'] as String,
    json['description'] as String,
    json['link'] as String,
    json['latestReleaseTime'] as int,
    json['videoNum'] as int)
  ..approvedNotReadyVideoCount = json['approvedNotReadyVideoCount'] as int
  ..ifPgc = json['ifPgc'] as bool;

abstract class _$AuthorSerializerMixin {
  int get id;
  String get icon;
  String get name;
  String get description;
  String get link;
  int get latestReleaseTime;
  int get videoNum;
  int get approvedNotReadyVideoCount;
  bool get ifPgc;
  Map<String, dynamic> toJson() => <String, dynamic>{
        'id': id,
        'icon': icon,
        'name': name,
        'description': description,
        'link': link,
        'latestReleaseTime': latestReleaseTime,
        'videoNum': videoNum,
        'approvedNotReadyVideoCount': approvedNotReadyVideoCount,
        'ifPgc': ifPgc
      };
}

Provider _$ProviderFromJson(Map<String, dynamic> json) => new Provider(
    json['name'] as String, json['alias'] as String, json['icon'] as String);

abstract class _$ProviderSerializerMixin {
  String get name;
  String get alias;
  String get icon;
  Map<String, dynamic> toJson() =>
      <String, dynamic>{'name': name, 'alias': alias, 'icon': icon};
}

UrlList _$UrlListFromJson(Map<String, dynamic> json) => new UrlList(
    json['name'] as String, json['url'] as String, json['size'] as int);

abstract class _$UrlListSerializerMixin {
  String get name;
  String get url;
  int get size;
  Map<String, dynamic> toJson() =>
      <String, dynamic>{'name': name, 'url': url, 'size': size};
}
