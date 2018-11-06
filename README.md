# PrettyBoxFormatter

A library for formatting/serializing text into boxes drawn using UTF-8 characters.

An example of a box produced by PrettyBoxFormatter:

```
┌─────────────────────────────────────────┐
│                                         │
│     Number of apples: 5                 │
│     Apple seller name: John Johnson     │
│     Has green apples: true              │
│                                         │
└─────────────────────────────────────────┘
```

Another example, screenshot from Android Studio's Logcat:  
<img src="https://github.com/knezmilos13/prettyboxformatter/blob/master/doc/readme_demo_logcat.png">

PrettyBoxFormatter is not a logging library - the input strings/objects are simply converted to a
single String containing the "box" with the desired content. You can then output that String to your
console, write it to a file, send it over the Internet (don't know why you would do that), etc.

PrettyBoxFormatter can be used both with regular Java and with Android.

To add to your project using gradle, add the following:

    dependencies {
        ...
        implementation 'com.bgpixel:prettyboxformatter:1.4.0'
        ...
    }

To use the library, first create an instance of `PrettyBoxFormatter` then call one of the 
`format` methods which accept a `List<String>` or an object implementing `PrettyBoxable` 
interface:

```
PrettyBoxFormatter pbFormatter = new PrettyBoxFormatter();
String result = pbFormatter.format(content);
```

You can add inner horizontal lines by adding a `LineWithLevel` or `LineWithType` instance to a 
`List<CharSequence>` passed to `format` method. For details, see 
[format method](https://github.com/knezmilos13/prettyboxformatter/wiki/Format-method).  

```
// Code                                                 //  Result:
List<CharSequence> contentLines = new ArrayList<>();    //  ┌─────────────┐
contentLines.add("First line");                         //  │ First line  │
contentLines.add("Second line");                        //  │ Second line │
contentLines.add(LineWithLevel.LEVEL_1);                //  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┤
contentLines.add("Third line");                         //  │ Third line  │
                                                        //  └─────────────┘  
String result = pbFormatter.format(contentLines);
```


You can use different [line types](https://github.com/knezmilos13/prettyboxformatter/wiki/Line-types):  
<img src="https://github.com/knezmilos13/prettyboxformatter/blob/master/doc/readme_demo_box_1.png">
<img src="https://github.com/knezmilos13/prettyboxformatter/blob/master/doc/readme_demo_box_2.png">
<img src="https://github.com/knezmilos13/prettyboxformatter/blob/master/doc/readme_demo_box_3.png">


For more details, see the following wiki pages:  
* [Configuration](https://github.com/knezmilos13/prettyboxformatter/wiki/Configuration)
* [Format method](https://github.com/knezmilos13/prettyboxformatter/wiki/Format-method)  
* [Font problems](https://github.com/knezmilos13/prettyboxformatter/wiki/Font-problems)  
* [Examples](https://github.com/knezmilos13/prettyboxformatter/wiki/Examples)  
* [Line types](https://github.com/knezmilos13/prettyboxformatter/wiki/Line-types)
