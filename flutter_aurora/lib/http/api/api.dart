import 'dart:async';

import 'package:dio/dio.dart';
import 'package:http/http.dart' as http;
import 'package:flutter_aurora/http/aurora_httpclient.dart';

const String UUID = '97cb741ce0b4442aaca94749a506b380ca2fb30f';
const String VC = '220';
const String VD = '3.10';

class Api {
  static var client = new AuroraHttpClient();

  static Future getHomeVideoListInfo(int date, int num) {
    var path = "v5/index/tab/feed";
    return
//      http.get("http://baobab.kaiyanapp.com/api/v5/index/tab/feed?date=0&num=0");

      client.get(
      path,
      {
        'date': date,
        'num': num,
        'udid': UUID,
        'vc': VC,
        'vn': VD,
      },
    );
  }

  static Future getRankVideoListInfo(String strategy) {
    var path = "v3/ranklist";
    return client.get(
      path,
      {
        'strategy': strategy,
      },
    );
  }

  static Future getCategories() {
    var path = "v2/categories";
    return client.get(
      path,
      {},
    );
  }

  static Future getCategoryVideos(int length, int categoryId) {
    var path = "v4/categories/videoList";
    return client.get(
      path,
      {
        'id': categoryId,
        'start': length,
        'num': 10,
        'udid': UUID,
      },
    );
  }
}
