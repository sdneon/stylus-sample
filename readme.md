# Stylus Sample App (Java)
## _Stylus Pressure Input for Java, corresponding **sample app** for **stylus-jni**_

This is my corresponding **sample app** for [stylus-jni](https://github.com/sdneon/stylus-jni) & [library](https://github.com/sdneon/stylus-lib) mods.

It doesn't actually let use draw anything.
However, just scribble in the JPanel to observe the output stylus events =D

## Use With

Use with [Stylus JNI](https://github.com/sdneon/stylus-jni) and [Stylus library](https://github.com/sdneon/stylus-lib).

To use this mod:
* Create a project with these sources, in Eclipse or NetBeans.
* Make sure to add 'lib' as folder for native binaries (i.e. stylus.DLL)
  * And add it to Run Congfiguration. E.g. -Djava.library.path="lib;../lib" (adjust the relative path accordingly)
* Build and run with console (i.e. run using java.exe instead of javaw.exe).

## Thanks

Thanks to [org.lecturestudio.stylus](https://github.com/lectureStudio/stylus) for the awesome Stylus JNI set.