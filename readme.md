# Stylus Sample App (Java)
## _Stylus Pressure Input for Java, corresponding **sample apps** for **stylus-jni**_

This are my corresponding **sample apps** for [stylus-jni](https://github.com/sdneon/stylus-jni) & [library](https://github.com/sdneon/stylus-lib) mods.

1. **StylusSample**
It doesn't actually let use draw anything.
However, just scribble in the JPanel to observe the output stylus events X)
2. **StylusDemo**
This has a drawing canvas.
It is a modified/reduced version of [org.lecturestudio.stylus's](https://github.com/lectureStudio/stylus) sample app, without the bloat of Google Guice, injects et al. That's 5 dependencies/JARs eliminated (aopalliance-1.0.jar, failureaccess-1.0.1.jar, guava-30.1-jre.jar, guice-5.0.1.jar, javax.inject-1.jar), so it should be easier to play with in your apps =D

## Use With

Use with [Stylus JNI](https://github.com/sdneon/stylus-jni) and [Stylus library](https://github.com/sdneon/stylus-lib).

To use this mod:
* Create a project with these sources, in Eclipse or NetBeans.
* Make sure to add 'lib' as folder for native binaries (i.e. stylus.DLL)
  * And add it to Run Congfiguration. E.g. -Djava.library.path="lib;../lib" (adjust the relative path accordingly)
* Build and run with console (i.e. run using java.exe instead of javaw.exe).

## Thanks

Thanks to [org.lecturestudio.stylus](https://github.com/lectureStudio/stylus) for the awesome Stylus JNI set.