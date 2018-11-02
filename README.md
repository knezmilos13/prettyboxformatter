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

PrettyBoxFormatter is not a logging library - the input strings/objects are simply converted to a
single String containing the "box" with the desired content. You can then output that String to your
console, write it to a file, send it over the Internet (don't know why you would do that), etc.

PrettyBoxFormatter can be used both with regular Java and with Android.

To add to your project using gradle, add the following:

    dependencies {
        ...
        implementation 'com.bgpixel:prettyboxformatter:1.1.0'
        ...
    }

To use the library, first create an instance of `PrettyBoxFormatter` then call one of the 
`format` methods which accept a `List<String>` or an object implementing `PrettyBoxable` 
interface:

```
PrettyBoxFormatter pbFormatter = new PrettyBoxFormatter();
String result = pbFormatter.format(content);
```

An empty String in the list given to format method will be formatted as a second-level horizontal line:
```
// Code                                           //  Result:
List<String> contentLines = new ArrayList<>();    //  ┌─────────────┐
contentLines.add("First line");                   //  │ First line  │
contentLines.add("Second line");                  //  │ Second line │
contentLines.add("");                             //  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┤
contentLines.add("Third line");                   //  │ Third line  │
                                                  //  └─────────────┘  
String result = pbFormatter.format(contentLines);
```


For more details, see the following wiki pages:  
[Configuration](https://github.com/knezmilos13/prettyboxformatter/wiki/Configuration)  
[Font problems](https://github.com/knezmilos13/prettyboxformatter/wiki/Font-problems)  
[Examples](https://github.com/knezmilos13/prettyboxformatter/wiki/Examples)
