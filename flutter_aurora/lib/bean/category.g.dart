// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'category.dart';

// **************************************************************************
// Generator: JsonSerializableGenerator
// **************************************************************************

Category _$CategoryFromJson(Map<String, dynamic> json) => new Category(
    json['id'] as int,
    json['authorId'] as int,
    json['name'] as String,
    json['alias'] as String,
    json['description'] as String,
    json['bgPicture'] as String,
    json['bgColor'] as String,
    json['headerImage'] as String);

abstract class _$CategorySerializerMixin {
  int get id;
  int get authorId;
  String get name;
  String get alias;
  String get description;
  String get bgPicture;
  String get bgColor;
  String get headerImage;
  Map<String, dynamic> toJson() => <String, dynamic>{
        'id': id,
        'authorId': authorId,
        'name': name,
        'alias': alias,
        'description': description,
        'bgPicture': bgPicture,
        'bgColor': bgColor,
        'headerImage': headerImage
      };
}
