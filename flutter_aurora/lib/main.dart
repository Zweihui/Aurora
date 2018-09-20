import 'dart:async';
import 'dart:convert';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_aurora/res/colors.dart';
import 'package:flutter_aurora/res/strings.dart';
import 'package:flutter_aurora/widgets/bottom_navigation.dart';
import 'package:flutter_aurora/widgets/page/category_detail_page.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter_aurora/widgets/page/category_page.dart';
import 'package:flutter_aurora/widgets/page/home_page.dart';
import 'package:flutter_aurora/widgets/page/hot_page.dart';

void main() => runApp(new MyApp());

const List<Choice> choices = const <Choice>[
  const Choice(
    title: AuroraStrings.setting,
  ),
];

const List<String> pageName = const <String>[
  AuroraStrings.home,
  AuroraStrings.hot,
  AuroraStrings.category,
  AuroraStrings.follow,
  AuroraStrings.mine,
];

class Choice {
  final String title;

  const Choice({this.title});
}

Widget _widgetForRoute(String route) {
  switch (route) {
    case 'mainRoute':
      return MyApp();
    default:
      return Center(
        child: Text('Unknown route: $route', textDirection: TextDirection.ltr),
      );
  }
}

class MainContainerPage extends StatefulWidget {
  MainContainerPage({Key key}) : super(key: key);

  @override
  _MainContainerPageState createState() => new _MainContainerPageState();
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) =>
      MaterialApp(
        title: 'FlutterAurora',
        theme: new ThemeData(
          primaryColor: AuroraColors.primaryColor,
          primaryColorDark: AuroraColors.primaryColor,
          accentColor: AuroraColors.primaryColor,
        ),
        home: new MainContainerPage(),
      );
}

enum ShowTabState { hot, follow, hide }

class _MainContainerPageState extends State<MainContainerPage>
    with TickerProviderStateMixin {
  PageController _pageController;
  TabController _hotTabController;
  TabController _followTabController;
  ScrollController _scrollViewController;
  ShowTabState showTabState = ShowTabState.hide;
  TabBar hotTabBar;
  TabBar followTabBar;
  PageView pageView;
  String title = AuroraStrings.home;

  @override
  void initState() {
    super.initState();
    _scrollViewController = new ScrollController();
    _pageController = new PageController();
    _hotTabController = new TabController(
      length: 3,
      initialIndex: 0,
      vsync: this,
    );
    _followTabController = new TabController(
      length: 2,
      initialIndex: 0,
      vsync: this,
    );
    hotTabBar = new TabBar(
      labelColor: Colors.white,
      controller: _hotTabController,
      tabs: [
        new Tab(
          text: AuroraStrings.week_rank,
        ),
        new Tab(
          text: AuroraStrings.month_rank,
        ),
        new Tab(
          text: AuroraStrings.all_rank,
        ),
      ],
    );
    followTabBar = new TabBar(
      labelColor: Colors.white,
      controller: _followTabController,
      tabs: [
        new Tab(
          text: AuroraStrings.hot_follow,
        ),
        new Tab(
          text: AuroraStrings.my_follow,
        ),
      ],
    );
    pageView = new PageView(
      onPageChanged: onPageChanged,
      physics: new NeverScrollableScrollPhysics(),
      controller: _pageController,
      children: <Widget>[
        new HomePage(),
        new HotContainerPage(controller: _hotTabController),
        new CategoryPage(),
        new HomePage(),
        new HomePage(),
      ],
    );
  }

  @override
  void dispose() {
    _scrollViewController.dispose();
    _pageController.dispose();
    _hotTabController.dispose();
    super.dispose();
  }

  TabBar _getTabBar() {
    if (showTabState == ShowTabState.hot) {
      return hotTabBar;
    } else if (showTabState == ShowTabState.follow) {
      return followTabBar;
    } else {
      return null;
    }
  }

  @override
  Widget build(BuildContext context) =>
      Scaffold(
        body: new NestedScrollView(
          controller: _scrollViewController,
          headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
            return <Widget>[
              new SliverAppBar(
                pinned: (showTabState == ShowTabState.hot ||
                    showTabState == ShowTabState.follow)
                    ? true
                    : false,
                floating: true,
                snap: true,
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
                title: new Text(title),
                bottom: _getTabBar(),
                actions: <Widget>[
                  new IconButton(
                    icon: new Icon(Icons.search),
                    onPressed: () {},
                  ),
                  new PopupMenuButton<Choice>(
                    itemBuilder: (BuildContext context) {
                      return choices.map((Choice choice) {
                        return new PopupMenuItem<Choice>(
                          value: choice,
                          child: new Text(choice.title),
                        );
                      }).toList();
                    },
                  ),
                ],
              ),
            ];
          },
          body: pageView,
        ),
        drawer: new Drawer(
          child: new ListView(
            padding: EdgeInsets.zero,
            children: <Widget>[
              new UserAccountsDrawerHeader(
                accountName: new Text('User Name'),
                accountEmail: new Text('email@example.com'),
                currentAccountPicture:
                new CircleAvatar(
                    backgroundImage: new CachedNetworkImageProvider(
                        "https://avatars2.githubusercontent.com/u/18547710?s=460&v=4")),
                onDetailsPressed: () {},
              ),
              new ListTile(
                leading: new Icon(Icons.home),
                title: new Text(AuroraStrings.home),
                onTap: () {
                  Navigator.pop(context);
                },
              ),
              new ListTile(
                leading: new Icon(Icons.remove_red_eye),
                title: new Text(AuroraStrings.follow),
                onTap: () {
                  Navigator.pop(context);
                },
              ),
              new ListTile(
                leading: new Icon(Icons.video_library),
                title: new Text(AuroraStrings.cache),
                onTap: () {
                  Navigator.pop(context);
                },
              ),
              new ListTile(
                leading: new Icon(Icons.history),
                title: new Text(AuroraStrings.history),
                onTap: () {
                  Navigator.pop(context);
                },
              ),
              new ListTile(
                leading: new Icon(Icons.settings),
                title: new Text(AuroraStrings.setting),
                onTap: () {
                  Navigator.pop(context);
                },
              ),
            ],
          ),
        ),
        bottomNavigationBar: new MyBottomNavigation(_pageController),
      );

  List buildTextViews(int count) {
    List<Widget> strings = List();
    for (int i = 0; i < count; i++) {
      strings.add(new Padding(
          padding: new EdgeInsets.all(16.0),
          child: new Text("Item number " + i.toString(),
              style: new TextStyle(fontSize: 20.0))));
    }
    return strings;
  }

  void onPageChanged(int index) {
    setState(() {
      showTabState = ShowTabState.hide;
      switch (index) {
        case 0:
          title = AuroraStrings.home;
          break;
        case 1:
          title = AuroraStrings.hot;
          showTabState = ShowTabState.hot;
          break;
        case 2:
          title = AuroraStrings.category;
          break;
        case 3:
          title = AuroraStrings.follow;
          showTabState = ShowTabState.follow;
          break;
        case 4:
          title = AuroraStrings.mine;
          break;
      }
    });
  }
}
