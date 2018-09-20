import 'package:flutter_aurora/widgets/multi_state_layout.dart';
import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_aurora/bean/video.dart';
import 'package:flutter_aurora/http/api/api.dart';
import 'package:flutter_aurora/widgets/common_video_list.dart';

const String STRATEGYWEEK = "weekly";
const String STRATEGYMONTH = "monthly";
const String STRATEGYALL = "historical";

class HotContainerPage extends StatefulWidget {
  final TabController controller;
  bool init = false;

  HotContainerPage({Key key, this.controller}) : super(key: key);

  @override
  HotContainerPageState createState() => new HotContainerPageState();

}

class HotContainerPageState extends State<HotContainerPage>
    with AutomaticKeepAliveClientMixin<HotContainerPage> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) =>
      TabBarView(
        controller: widget.controller,
        children: <Widget>[
          new HotPage(
            title: STRATEGYWEEK,
          ),
          new HotPage(
            title: STRATEGYMONTH,
          ),
          new HotPage(
            title: STRATEGYALL,
          ),
        ],
      );

  @override
  bool get wantKeepAlive => true;
}

class HotPage extends StatefulWidget {
  final String title;

  final List<Video> videos = new List();
  ResultState refreshState = ResultState.loading;

  HotPage({Key key, this.title}) : super(key: key);

  @override
  HotPageState createState() => new HotPageState();
}

class HotPageState extends State<HotPage>
    with AutomaticKeepAliveClientMixin<HotPage> {
  @override
  Widget build(BuildContext context) =>
      CommonVideoList(
        videos: widget.videos,
        resultState: widget.refreshState,
        onRefresh: _getVideoListInfo,
        retry: _retry,
      );

  @override
  void initState() {
    super.initState();
    _getVideoListInfo();
  }

  Future<Null> _retry() {
    setState(() {
      widget.refreshState = ResultState.loading;
    });
    return _getVideoListInfo();
  }

  Future<Null> _getVideoListInfo() {
    return Api.getRankVideoListInfo(widget.title).then((response) {
      VideoListInfo info = new VideoListInfo.fromJson(response.data);
      List<VideoData> videoDatas = info.itemList;
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

  @override
  bool get wantKeepAlive => true;
}
