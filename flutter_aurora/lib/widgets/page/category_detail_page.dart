import 'dart:async';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_aurora/bean/category.dart';
import 'package:flutter_aurora/bean/video.dart';
import 'package:flutter_aurora/http/api/api.dart';
import 'package:flutter_aurora/res/colors.dart';
import 'package:flutter_aurora/res/strings.dart';
import 'package:flutter_aurora/widgets/common_list_load_more.dart';
import 'package:flutter_aurora/widgets/common_video_list.dart';
import 'package:flutter_aurora/widgets/multi_state_layout.dart';
import 'package:flutter_aurora/widgets/multi_state_layout.dart';
import 'package:flutter_aurora/widgets/ripple.dart';

class CategoryDetailPage extends StatefulWidget {
  final List<Category> categories;
  final int position;

  CategoryDetailPage(
      {Key key, @required this.categories, @required this.position})
      : super(key: key);

  @override
  CategoryDetailPageState createState() => new CategoryDetailPageState();
}

class CategoryDetailPageState extends State<CategoryDetailPage>
    with
        AutomaticKeepAliveClientMixin<CategoryDetailPage>,
        TickerProviderStateMixin<CategoryDetailPage> {
  ScrollController _scrollViewController;
  TabController _categoryTabController;
  final double _appBarHeight = 256.0;
  TabBarView _tabbarView;

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    _scrollViewController = new ScrollController();
    _categoryTabController = new TabController(
      length: widget.categories.length,
      initialIndex: widget.position,
      vsync: this,
    );
    _categoryTabController.addListener(_handleTabSelection);
    _tabbarView = new TabBarView(
      controller: _categoryTabController,
      children: widget.categories.map((Category category) {
        return new SafeArea(
          top: false,
          bottom: false,
          child: new CategoryTabPage(
            categoryId: category.id,
          ),
        );
      }).toList(),
    );
  }

  void _handleTabSelection() {
    setState(() {});
  }

  @override
  void dispose() {
    _scrollViewController.dispose();
    _categoryTabController.dispose();
    super.dispose();
  }

  TabBar _getTabBar() {
    return new TabBar(
      isScrollable: true,
      labelColor: Colors.white,
      controller: _categoryTabController,
      tabs: widget.categories
          .map((category) =>
      new Tab(
        text: category.name,
      ))
          .toList(),
    );
  }

  @override
  Widget build(BuildContext context) =>
      Scaffold(
        body: new NestedScrollView(
          controller: _scrollViewController,
          headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
            return <Widget>[
              new SliverAppBar(
                expandedHeight: _appBarHeight,
                pinned: true,
                floating: false,
                snap: false,
                forceElevated: true,
                brightness: Brightness.dark,
                iconTheme: new IconThemeData(color: Colors.white),
                textTheme: Theme
                    .of(context)
                    .textTheme
                    .apply(
                  bodyColor: Colors.white,
                  displayColor: Colors.white,
                ),
                flexibleSpace: new FlexibleSpaceBar(
                  title: new Container(
                    margin: new EdgeInsets.fromLTRB(0.0, 0.0, 0.0, 47.0),
                    child: new Text(
                      widget.categories[_categoryTabController.index].name,
                      style: new TextStyle(color: Colors.white, fontSize: 20.0),
                    ),
                  ),
                  background: new Stack(
                    fit: StackFit.expand,
                    children: <Widget>[
                      new Image(
                        image: new CachedNetworkImageProvider(widget
                            .categories[_categoryTabController.index]
                            .headerImage),
                        fit: BoxFit.cover,
                      ),
                      // This gradient ensures that the toolbar icons are distinct
                      // against the background image.
                      const DecoratedBox(
                        decoration: const BoxDecoration(
                          gradient: const LinearGradient(
                            begin: const Alignment(0.0, -1.0),
                            end: const Alignment(0.0, -0.4),
                            colors: const <Color>[
                              const Color(0x60000000),
                              const Color(0x00000000)
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                bottom: _getTabBar(),
              ),
            ];
          },
          body: _tabbarView,
        ),
      );
}

class CategoryTabPage extends StatefulWidget {
  final int categoryId;

  List<Video> videos = new List();
  ResultState refreshState = ResultState.loading;
  LoadMoreState loadMoreState = LoadMoreState.hide;

  CategoryTabPage({Key key, this.categoryId}) : super(key: key);

  @override
  CategoryTabPageState createState() => new CategoryTabPageState();
}

class CategoryTabPageState extends State<CategoryTabPage>
    with AutomaticKeepAliveClientMixin<CategoryTabPage> {
  int start = 0;

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
    return Api.getCategoryVideos(widget.videos.length, widget.categoryId).then(
            (response) {
          VideoListInfo info = new VideoListInfo.fromJson(response.data);
          List<VideoData> videoDatas = info.itemList;
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
    return Api.getCategoryVideos(0, widget.categoryId).then((response) {
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
}
