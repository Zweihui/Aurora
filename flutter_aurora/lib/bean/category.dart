import 'package:json_annotation/json_annotation.dart';
part 'category.g.dart';

@JsonSerializable()
class Category extends Object with _$CategorySerializerMixin {
  Category(this.id, this.authorId, this.name, this.alias, this.description,
      this.bgPicture, this.bgColor, this.headerImage);
  int id;
  int authorId;
  String name;
  String alias;
  String description;
  String bgPicture;
  String bgColor;
  String headerImage;
  factory Category.fromJson(Map<String, dynamic> json) =>
      _$CategoryFromJson(json);
}
