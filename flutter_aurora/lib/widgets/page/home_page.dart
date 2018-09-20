import 'package:flutter_aurora/widgets/multi_state_layout.dart';
import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_aurora/bean/video.dart';
import 'package:flutter_aurora/http/api/api.dart';
import 'package:flutter_aurora/res/colors.dart';
import 'package:flutter_aurora/res/strings.dart';
import 'package:flutter_aurora/widgets/common_list_load_more.dart';
import 'package:flutter_aurora/widgets/common_video_list.dart';
import 'package:flutter_aurora/widgets/multi_state_layout.dart';


class HomePage extends StatefulWidget {
  final String title;

  List<Video> videos = new List();
  ResultState refreshState = ResultState.loading;
  LoadMoreState loadMoreState = LoadMoreState.hide;

  HomePage({Key key, this.title}) : super(key: key);

  @override
  HomePageState createState() => new HomePageState();
}

class HomePageState extends State<HomePage>
    with AutomaticKeepAliveClientMixin<HomePage> {

  int date = 0;
  int num = 0;

  @override
  Widget build(BuildContext context) =>
      CommonVideoList(
        videos: widget.videos,
        resultState: widget.refreshState,
        onRefresh: _getVideoListInfo,
        retry: _retry,
        loadMoreState: widget.loadMoreState,
        onLoadMore: _loadMoreVideoListInfo,
        
      );

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    if (widget.refreshState == ResultState.success) {
      return;
    }
    _getVideoListInfo();
  }

  Future<Null> _retry() {
    setState(() {
      widget.refreshState = ResultState.loading;
    });
    return _getVideoListInfo();
  }

  Future<Null> _loadMoreVideoListInfo() {
    setState(() {
      widget.loadMoreState = LoadMoreState.showLoading;
    });
    return Api.getHomeVideoListInfo(date, num).then((response) {
      VideoListInfo info = new VideoListInfo.fromJson(response.data);
      date = _getDateFromNextPageUrl(info.nextPageUrl);
      num = _getNumFromNextPageUrl(info.nextPageUrl);
      List<VideoData> videoDatas = info.itemList
          .where(
              (VideoData videoDate) => VIDEO_ITEM_TYPE_FOLLOW == videoDate.type)
          .toList();
      List<Video> videos =
      videoDatas.map((VideoData videoDate) => videoDate.data).toList();
      setState(() {
        widget.videos.addAll(videos);
        widget.loadMoreState = LoadMoreState.hide;
      });
    }, onError: (e) {
      setState(() {
        widget.loadMoreState = LoadMoreState.error;
      });
    });
  }

  Future<Null> _getVideoListInfo() {
    date = 0;
    num = 0;
    return Api.getHomeVideoListInfo(date, num).then((response) {
      VideoListInfo info = new VideoListInfo.fromJson(response.data);
      date = _getDateFromNextPageUrl(info.nextPageUrl);
      num = _getNumFromNextPageUrl(info.nextPageUrl);
      List<VideoData> videoDatas = info.itemList
          .where(
              (VideoData videoDate) => VIDEO_ITEM_TYPE_FOLLOW == videoDate.type)
          .toList();
      List<Video> videos =
      videoDatas.map((VideoData videoDate) => videoDate.data).toList();
      setState(() {
        widget.videos.clear();
        widget.videos.addAll(videos);
        widget.refreshState = ResultState.success;
      });
    }, onError: (e) {
      setState(() {
        widget.videos.clear();
        widget.refreshState = ResultState.error;
      });
    });
  }

  int _getDateFromNextPageUrl(String nextPageUrl) {
    String date = nextPageUrl?.substring(
        nextPageUrl.indexOf("date=") + 5, nextPageUrl.indexOf("&"));
    if (date.isNotEmpty) {
      return int.parse(date);
    } else {
      return 0;
    }
  }

  int _getNumFromNextPageUrl(String nextPageUrl) {
    String num = nextPageUrl?.substring(
        nextPageUrl.indexOf("num=") + 4, nextPageUrl.length);
    if (num.isNotEmpty) {
      return int.parse(num);
    } else {
      return 0;
    }
  }
}
