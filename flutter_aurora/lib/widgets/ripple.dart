import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_aurora/res/colors.dart';

class RippleCard extends StatelessWidget {
  final Widget child;
  final VoidCallback onTap;

  RippleCard({@required this.child, @required this.onTap});

  @override
  Widget build(BuildContext context) =>
      Card(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(4.0),
        ),
          child: new Stack(
          children: <Widget>[
            new Positioned.fill(
              child: child,
            ),
            new Material(
              color: Colors.transparent,
              child: new InkWell(
                borderRadius: BorderRadius.circular(4.0),
                highlightColor: Colors.transparent,
                enableFeedback: false,
                splashColor: AuroraColors.ripplrColor,
                onTap: () {
                  onTap();
                },
              ),
            ),
          ],
        ),
      );
}

class RippleRaisedCard extends StatelessWidget {
  final Widget child;
  final VoidCallback onTap;

  RippleRaisedCard({@required this.child, @required this.onTap});

  @override
  Widget build(BuildContext context) =>
      RaisedButton(
        onPressed: () {},
        elevation: 1.0,
        padding: new EdgeInsets.all(0.0),
        child: new Stack(
          children: <Widget>[
            child,
            new Material(
              color: Colors.transparent,
              child: new InkWell(
                splashColor: AuroraColors.ripplrColor,
                highlightColor: Colors.transparent,
                onTap: onTap,
              ),
            ),
          ],
        ),
      );
}