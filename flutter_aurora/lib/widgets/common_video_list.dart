import 'dart:async';
import 'dart:convert';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_aurora/bean/video.dart';
import 'package:flutter_aurora/res/colors.dart';
import 'package:flutter_aurora/res/strings.dart';
import 'package:flutter_aurora/widgets/common_list_load_more.dart';
import 'package:flutter_aurora/widgets/multi_state_layout.dart';
import 'package:flutter_aurora/widgets/ripple.dart';

class CommonVideoList extends StatefulWidget {
  final List<Video> videos;
  final ResultState resultState;
  final LoadMoreState loadMoreState;
  final RefreshCallback onRefresh;
  final VoidCallback retry;
  final VoidCallback onLoadMore;

  CommonVideoList({
    Key key,
    @required this.videos,
    @required this.resultState,
    this.loadMoreState,
    @required this.onRefresh,
    this.retry,
    this.onLoadMore,
  }) : super(key: key);

  // The framework calls createState the first time a widget appears at a given
  // location in the tree. If the parent rebuilds and uses the same type of
  // widget (with the same key), the framework will re-use the State object
  // instead of creating a new State object.

  @override
  _VideoListState createState() => new _VideoListState();
}

class CommonVideoListItem extends StatelessWidget {
  static const double height = 280.0;
  final Video video;
  static const _platform = const MethodChannel('com.zwh.mvparms.eyepetizer/main');
  CommonVideoListItem({Key key, @required this.video}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final ThemeData theme = Theme.of(context);
    final TextStyle descriptionStyle = theme.textTheme.subhead;
    // TODO: implement build
    return new Container(
      padding: new EdgeInsets.all(8.0),
      height: height,
      child: new RippleCard(
        onTap: () {
          _platform.invokeMethod('gotoVideo', { "video": json.encode(this.video.content == null ? this.video :this.video.content)});
        },
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            new SizedBox(
              height: 185.0,
              child: new Stack(
                children: <Widget>[
                  new Positioned.fill(
                    child: new ClipRRect(
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(4.0),
                        topRight: Radius.circular(4.0),),
                      child: new Image(
                        image: new CachedNetworkImageProvider(
                            video.cover == null
                                ? video.content.data.cover.feed
                                : video.cover.feed),
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            new Expanded(
              child: new Padding(
                padding: const EdgeInsets.fromLTRB(12.0, 12.0, 12.0, 12.0),
                child: new Row(
                  children: <Widget>[
                    new CircleAvatar(
                      radius: 22.0,
                      backgroundImage: new CachedNetworkImageProvider(
                          video.author == null
                              ? (video.content.data.author == null
                              ? ""
                              : video.content.data.author.icon)
                              : video.author.icon),
                    ),
                    new Expanded(
                      child: new Padding(
                        padding: const EdgeInsets.fromLTRB(10.0, 0.0, 0.0, 0.0),
                        child: new Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            new Flexible(
                              child: new Text(
                                video.title == null
                                    ? video.content.data.title
                                    : video.title,
                                overflow: TextOverflow.ellipsis,
                                style: descriptionStyle.copyWith(
                                  color: Colors.black54,
                                  fontSize: 16.0,
                                ),
                              ),
                            ),
                            new Flexible(
                              child: new Text(
                                _getVideoDetailInfo(video),
                                overflow: TextOverflow.ellipsis,
                                style: descriptionStyle.copyWith(
                                  color: Colors.black54,
                                  fontSize: 16.0,
                                ),
                              ),
                            )
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  String _getVideoDetailInfo(Video video) {
    int duration =
    video.duration == null ? video.content.data.duration : video.duration;
    int hours = (duration ~/ 3600);
    int minutes = (duration % 3600 ~/ 60);
    int seconds = duration - hours * 3600 - minutes * 60;
    String hour = hours == 0
        ? "00"
        : hours > 9 ? hours.toString() : "0" + hours.toString();
    String minute = minutes == 0
        ? "00"
        : minutes > 9 ? minutes.toString() : "0" + minutes.toString();
    String second = seconds == 0
        ? "00"
        : seconds > 9 ? seconds.toString() : "0" + seconds.toString();
    String timeStr = (hour + ":" + minute + ":" + second);
    String str = '#${video.category == null
        ? video.content.data.category
        : video.category } / ' +
        timeStr;
    return str;
  }
}

class _VideoListState extends State<CommonVideoList> {
  double _dragOffset;
  final GlobalKey<RefreshIndicatorState> _refreshIndicatorKey =
  new GlobalKey<RefreshIndicatorState>();

  @override
  Widget build(BuildContext context) =>
      MultiStateView(
        content: new Center(
          child: new NotificationListener(
            onNotification: _onNotification,
            child: new RefreshIndicator(
              key: _refreshIndicatorKey,
              child: new ListView(
                padding: new EdgeInsets.symmetric(vertical: 8.0),
                children: _getListItems(),
              ),
              onRefresh: widget.onRefresh,
            ),
          ),
        ),
        resultState: widget.resultState,
        retry: widget.retry,
      );

  List<Widget> _getListItems() {
    List<Widget> list = new List();
    widget.videos.forEach((Video video) {
      Widget widget = new CommonVideoListItem(
        key: new PageStorageKey("videoItem"),
        video: video,
      );
      list.add(widget);
    });
    list.add(new CommonListLoadMore(
      loadMoreState: widget.loadMoreState,
    ));
    return list;
  }

  bool _onNotification(ScrollNotification notification) {
    if (notification is ScrollStartNotification) {
      _dragOffset = 0.0;
    }

    if (notification is ScrollUpdateNotification) {
      _dragOffset -= notification.scrollDelta;
      if (notification.metrics.extentAfter <= 80.0 && _dragOffset < 0.0) {
        if (widget.loadMoreState != LoadMoreState.showLoading) {
          widget.onLoadMore();
        }
      }
    }
    return true;
  }
}
