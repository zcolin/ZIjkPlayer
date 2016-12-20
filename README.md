# ZIjkPlayer
##基于IjkPlayer的视频播放库.

####基于https://github.com/supercwn/SuperPlayer  代码进行修改，修正了一些bug，增加了一些接口，支持点播、直播，全屏、列表播放。
####IjkPlayer的so库编译版本为0.6.0

## Gradle
app的build.gradle中添加
```
dependencies {
    compile 'com.github.zcolin:ZIjkPlayer:1.0.9'
}
```
工程的build.gradle中添加
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
