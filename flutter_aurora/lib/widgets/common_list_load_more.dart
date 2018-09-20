import 'package:flutter/material.dart';
import 'package:flutter_aurora/res/colors.dart';
import 'package:flutter_aurora/res/strings.dart';

class CommonListLoadMore extends StatefulWidget {
  final LoadMoreState loadMoreState;

  CommonListLoadMore({Key key, this.loadMoreState: LoadMoreState.hide})
      : super(key: key);

  @override
  _CommonListLoadMoreState createState() => new _CommonListLoadMoreState();
}

enum LoadMoreState { error, showLoading, noMore, hide }

class _CommonListLoadMoreState extends State<CommonListLoadMore> {
  @override
  Widget build(BuildContext context) =>
      Container(
        height: 80.0,
        child: new Stack(
          children: <Widget>[
            new Offstage(
                offstage: widget.loadMoreState != LoadMoreState.showLoading,
                child: new Center(
                  child: new SizedBox(
                    width: 30.0,
                    height: 30.0,
                    child: new CircularProgressIndicator(
                      strokeWidth: 2.5,
                    ),
                  ),
                )),
            new Offstage(
              offstage: widget.loadMoreState != LoadMoreState.noMore,
              child: new Center(
                child: new Text(
                  AuroraStrings.nomore,
                  style: new TextStyle(
                    fontSize: 16.0,
                    color: AuroraColors.textColor,
                    fontStyle: FontStyle.italic,
                  ),
                ),
              ),
            ),
          ],
        ),
      );
}
