# ZIjkPlayer
##基于IjkPlayer的视频播放库（[IjkPlayer](https://github.com/Bilibili/ijkplayer)是bilibili的一个开源项目，他基于ffmpeg开发，支持多种视频格式格式）. 在[SuperPlayer](https://github.com/supercwn/SuperPlayer)的基础上修改，修正了一些问题，增加了一些易用接口。支持点播、直播，全屏、列表播放。IjkPlayer的so库编译版本为0.6.0。


## Gradle
app的build.gradle中添加
```
dependencies {
    compile 'com.github.zcolin:ZIjkPlayer:1.1.0'
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

Usage
=
```
player.setLive(isLive)//设置该地址是直播的地址
	.setNetChangeListener(true)//设置监听手机网络的变化,这个参数是内部是否处理网络监听，和setOnNetChangeListener没有关系
	.setOnNetChangeListener(this)//实现网络变化的回调
	.setScaleType(SuperPlayer.SCALETYPE_FITXY)
	.setPlayerWH(0, player.getMeasuredHeight())//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置
	.setAlwaysShowControl()  //设置则一直显示
	.onPrepared(new SuperPlayer.OnPreparedListener() {
		@Override
		public void onPrepared() {
		//TODO 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
		}
	})
	.onComplete(new Runnable() {
		@Override
		public void run() {
		//TODO 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
		}
	})
	.onInfo(new SuperPlayer.OnInfoListener() {
		@Override
		public void onInfo(int what, int extra) {
		//TODO 监听视频的相关信息。
		}
	})
	.onError(new SuperPlayer.OnErrorListener() {
		@Override
		public void onError(int what, int extra) {
		//TODO 监听视频播放失败的回调
		}
	});
```
