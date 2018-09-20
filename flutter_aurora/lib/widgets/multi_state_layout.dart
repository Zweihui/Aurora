import 'package:flutter/material.dart';
import 'package:flutter_aurora/res/colors.dart';

class MultiStateView extends StatefulWidget {
  final Widget content;
  final VoidCallback retry;
  final ResultState resultState;
  MultiStateView({Key key, this.retry, this.content, this.resultState:ResultState.loading})
      : super(key: key);


  @override
  _MultiStateViewState createState() => new _MultiStateViewState();
}

enum ResultState { error, loading, empty, success }

class _MultiStateViewState extends State<MultiStateView> {
  @override
  Widget build(BuildContext context) {
    final ThemeData theme = Theme.of(context);
    final TextStyle descriptionStyle = theme.textTheme.body2.copyWith(color: AuroraColors.textColor);
    return new Stack(
      children: <Widget>[
        new Offstage(
          offstage: widget.resultState != ResultState.loading,
          child: new Center(
            child: new CircularProgressIndicator(),
          ),
        ),
        new Offstage(
          offstage: widget.resultState != ResultState.success,
          child: new Center(
            child: widget.content,
          ),
        ),
        new Offstage(
          offstage: widget.resultState != ResultState.empty,
          child: new Center(
            child: new Text(
              "暂时没有你想要的内容",
              style: descriptionStyle,
            ),
          ),
        ),
        new Offstage(
          offstage: widget.resultState != ResultState.error,
          child: new Center(
            child: new Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                new Text(
                  "Whoops!请求异常",
                  style: descriptionStyle,
                ),
                new RaisedButton(
                  onPressed: widget.retry,
                  color: AuroraColors.primaryColor,
                  child: new Text("重试",style: descriptionStyle.copyWith(color: Colors.white),),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}
