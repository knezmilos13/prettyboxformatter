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

    repositories {
        maven { url "https://dl.bintray.com/knezmilos13/PrettyBoxFormatter" }
    }
    
    dependencies {
        implementation 'com.bgpixel:prettyboxformatter:1.0.0'
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
// Code                                    //  Result:
List<String> lines = new ArrayList<>();    //  ┌─────────────┐
lines.add("First line");                   //  │ First line  │
lines.add("Second line");                  //  │ Second line │
lines.add("");                             //  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┤
lines.add("Third line");                   //  │ Third line  │
                                           //  └─────────────┘  
String result = pbFormatter.format(lines);
```

## Configuration

You can pass a `PrettyBoxConfiguration` instance while instantiating a `PrettyBoxFormatter`:
```
PrettyBoxFormatter pbFormatter = new PrettyBoxFormatter(
    new PrettyBoxConfiguration.Builder()
        .setCharsPerLine(32)
        .setWrapContent(false)
        .build()
);
```

Settings that were not specified will use default values. Alternatively, you can set/change a 
`PrettyBoxConfiguration` instance later using `setConfiguration` method.

You can also pass a `PrettyBoxConfiguration` instance with every call to `format` method. The 
settings provided in this way will be used only for that one call. Global configuration will be used
for settings that were not provided in the instance passed with a call to `format`.

| Setting | Meaning |
| --- |--- |
| prefixEveryPrintWithNewline | Add a newline before every box. This helps with loggers that add tags and other stuff before every printout which splits the first line of the box (example: Logcat in Android). |
| charsPerLine | Number of characters to show per line. If `wrapContent` is set to `true`, this represents only the maximum possible width of the box. If `wrapContent` is set to `false`, this represents the fixed width of the box. <br/>Note: both the horizontal padding and margin are included in the `charsPerLine` value.   |
| wrapContent | If set to `true`, the box will take the minimum width needed to show all content without splitting lines, if possible. The box will not be wider than charsPerLine.<br/>If set to `false`, the box will have a fixed width of `charsPerLine` |
| closeOnTheRight | If set to `true`, the box will be closed on the right side.<br/>If set to `false`, the right side of the box will be left open.<br/>Note: many "monospaced" fonts are not fully monospaced so closed boxes might not work properly (i.e. the lengths of the lines won't be the same). In that case you should set this value to `false`. |
| horizontalPadding | Sets the number of spaces between the text and the left/right sides of the box. Is part of the box width, e.g. a closed box with edges 1 space wide, horizontal padding of 10, and width of 40 would have 18 spaces left for content. |
| verticalPadding | Sets the number of newlines between the text and the top/bottom sides of the box. |
| horizontalMargin | Sets the number of spaces between the box left/right sides and the surrounding elements (e.g. line start, other boxes). Is part of the box width, e.g. a closed box with edges 1 space wide, horizontal margin of 10, and width of 40 would have 18 spaces left for content. |
| verticalMargin| Sets the number of newlines before and after the box is drawn. |

When setting the instance-level configuration, if the resulting configuration is not valid, previous configuration (or default configuration, if non was set previously) will not be changed and a warning message will be output with all future printing calls. Similarly, when setting the per-call configuration, if the resulting configuration is not valid, the instance-level configuration will be used, and a warning message will be output with that single call.


## Font problems

A big problem when using this library is the font used to display the formatted (boxed) strings. Apparently, some monospaced fonts are not entirely monospaced, at least for some of the less used characters like the lines that are used to draw boxes in PrettyBoxFormatter.

Example, from left to right - editor window in IntelliJ, Courier New; markdown preview in IntelliJ, unknown font; console view in IntelliJ, Source code Pro font.  

<img src="https://github.com/knezmilos13/prettyboxformatter/blob/master/doc/fonts_example_bad_intellij_editor.png"> <img src="https://github.com/knezmilos13/prettyboxformatter/blob/master/doc/fonts_example_bad_intellij_markdown_preview.png"> <img src="https://github.com/knezmilos13/prettyboxformatter/blob/master/doc/fonts_example_good_source_code_pro.png">

If you wish to make sure your box looks reasonably independently of the font used, try using the boxes not closed on the right side.


## Examples

The following examples are taken from the `FormatTest` class:

Format lines manually:
```
// Code                                    //  Result:
List<String> lines = new ArrayList<>();    //  ┌─────────────┐
lines.add("First line");                   //  │ First line  │
lines.add("Second line");                  //  │ Second line │
lines.add("");                             //  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┤
lines.add("Third line");                   //  │ Third line  │
                                           //  └─────────────┘  
String result = pbFormatter.format(lines);
```

Note: examples below print an object implementing the PrettyBoxable interface as follows:
```
public class SimpleBoxableObject implements PrettyBoxable {
   ...
   @Override
   public List<String> toStringLines() {
       List<String> lines = new ArrayList<>();
       lines.add("Number of apples: " + numberOfApples);
       lines.add("Apple seller name: " + appleSellerName);
       lines.add("Has green apples: " + hasGreenApples);
       return lines;
   }
   ...
}
```

Printing a `PrettyBoxable` shown above with default settings:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT);
  
// Result:
// ┌─────────────────────────────────┐
// │ Number of apples: 5             │
// │ Apple seller name: John Johnson │
// │ Has green apples: true          │
// └─────────────────────────────────┘
```

With newline prefix:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
    new PrettyBoxConfiguration.Builder()
            .setPrefixEveryPrintWithNewline(true)
            .build());
  
// Result:
//
// ┌─────────────────────────────────┐
// │ Number of apples: 5             │
// │ Apple seller name: John Johnson │
// │ Has green apples: true          │
// └─────────────────────────────────┘
```

Box of fixed width, less wide than content:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
    new PrettyBoxConfiguration.Builder()
            .setCharsPerLine(32)
            .setWrapContent(false)
            .build());
  
// Result:
// ┌──────────────────────────────┐
// │ Number of apples: 5          │
// │ Apple seller name: John John │
// │ son                          │
// │ Has green apples: true       │
// └──────────────────────────────┘
```

Box of fixed width, wider than content:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
    new PrettyBoxConfiguration.Builder()
            .setCharsPerLine(60)
            .setWrapContent(false)
            .build());
  
// Result:
// ┌──────────────────────────────────────────────────────────┐
// │ Number of apples: 5                                      │
// │ Apple seller name: John Johnson                          │
// │ Has green apples: true                                   │
// └──────────────────────────────────────────────────────────┘
```

Wrap content:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
    new PrettyBoxConfiguration.Builder()
            .setCharsPerLine(60)
            .setWrapContent(true)
            .build());
  
// Result:
// ┌─────────────────────────────────┐
// │ Number of apples: 5             │
// │ Apple seller name: John Johnson │
// │ Has green apples: true          │
// └─────────────────────────────────┘
```

With horizontal and vertical padding:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
    new PrettyBoxConfiguration.Builder()
            .setHorizontalPadding(10)
            .setVerticalPadding(1)
            .build());
  
// Result:
// ┌───────────────────────────────────────────────────┐
// │                                                   │
// │          Number of apples: 5                      │
// │          Apple seller name: John Johnson          │
// │          Has green apples: true                   │
// │                                                   │
// └───────────────────────────────────────────────────┘
```

With horizontal and vertical margin:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
    new PrettyBoxConfiguration.Builder()
            .setHorizontalMargin(10)
            .setVerticalMargin(1)
            .build());
  
// Result:
//
//          ┌─────────────────────────────────┐
//          │ Number of apples: 5             │
//          │ Apple seller name: John Johnson │
//          │ Has green apples: true          │
//          └─────────────────────────────────┘
//
```

Box not closed on right:
```
String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
    new PrettyBoxConfiguration.Builder()
            .setHorizontalMargin(10)
            .setVerticalMargin(1)
            .setHorizontalPadding(5)
            .setVerticalPadding(1)
            .setCharsPerLine(70)
            .setWrapContent(false)
            .setCloseOnTheRight(false)
            .build());
  
// Result:
//
//          ┌───────────────────────────────────────────────────────────
//          │
//          │     Number of apples: 5
//          │     Apple seller name: John Johnson
//          │     Has green apples: true
//          │
//          └───────────────────────────────────────────────────────────
//          
```
