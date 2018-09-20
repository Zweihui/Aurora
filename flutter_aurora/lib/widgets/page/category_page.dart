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
import 'package:flutter_aurora/widgets/page/category_detail_page.dart';
import 'package:flutter_aurora/widgets/ripple.dart';

class CategoryPage extends StatefulWidget {
  final String title;
  ResultState resultState = ResultState.loading;
  final List<Category> categories = new List();

  CategoryPage({Key key, this.title}) : super(key: key);

  @override
  CategoryPageState createState() => new CategoryPageState();
}

class CategoryPageState extends State<CategoryPage>
    with AutomaticKeepAliveClientMixin<CategoryPage> {
  final GlobalKey<RefreshIndicatorState> _refreshIndicatorKey =
  new GlobalKey<RefreshIndicatorState>();

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) =>
      SafeArea(
        top: false,
        bottom: false,
        child: new Builder(
          builder: (BuildContext context) {
            return new MultiStateView(
              content: new Center(
                child: new RefreshIndicator(
                  key: _refreshIndicatorKey,
                  child: new GridView.builder(
                    padding: new EdgeInsets.all(10.0),
                    itemCount: widget.categories.length,
                    gridDelegate: new SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 0.75,
                      crossAxisSpacing: 10.0,
                      mainAxisSpacing: 10.0,
                    ),
                    itemBuilder: (BuildContext context, int index) {
                      return new RippleRaisedCard(
                        child: new Stack(
                          children: <Widget>[
                            new Positioned.fill(
                              child: new Image(
                                image: new CachedNetworkImageProvider(
                                  widget.categories[index].bgPicture,
                                ),
                                fit: BoxFit.cover,
                              ),
                            ),
                            new Center(
                              child: new Text(
                                widget.categories[index].name,
                                style:
                                new TextStyle(
                                    fontSize: 22.0, color: Colors.white),
                              ),
                            ),
                          ],
                        ),
                        onTap: () {
                          Navigator.push(context, new MaterialPageRoute(
                            fullscreenDialog: false,
                            builder: (BuildContext context) =>
                            new CategoryDetailPage(
                              categories: widget.categories, position: index,),
                          ));
                        },
                      );
                    },

                  ),
                  onRefresh: _getCategories,
                ),
              ),
              resultState: widget.resultState,
              retry: _retry,
            );
          },
        ),
      );

  @override
  void initState() {
    super.initState();
    if (widget.resultState == ResultState.success) {
      return;
    }
    _getCategories();
  }

  Future<Null> _getCategories() {
    return Api.getCategories().then((response) {
      List datas = response.data as List;
      List<Category> categories =
      datas.map((category) => new Category.fromJson(category)).toList();
      setState(() {
        widget.categories.clear();
        widget.categories.addAll(categories);
        widget.resultState = ResultState.success;
      });
    }, onError: (e) {
      setState(() {
        widget.categories.clear();
        widget.resultState = ResultState.error;
      });
    });
  }

  Future<Null> _retry() {
    setState(() {
      widget.resultState = ResultState.loading;
    });
    return _getCategories();
  }
}
