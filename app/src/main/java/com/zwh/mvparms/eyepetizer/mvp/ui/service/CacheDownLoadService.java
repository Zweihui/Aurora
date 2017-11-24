package com.zwh.mvparms.eyepetizer.mvp.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jess.arms.utils.UiUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadSerialQueue;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoMaster;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DownloadProgressInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.CacheActivity;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class CacheDownLoadService extends Service {

    public final static String VIDEOS_INFO = "videos_info";
    public final static String PAUSE_DOWNLOAD = "pause_download";

    private MyBinder binder = new MyBinder();
    private DownloadSerialQueue queue;
    private Map<String,VideoDownLoadInfo> videos = new HashMap<>();
    private List<BaseDownloadTask> tasks = new ArrayList<>();
    private boolean isInit = true;

    public class MyBinder extends Binder {

        public CacheDownLoadService getService(){
            return CacheDownLoadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queue = new DownloadSerialQueue();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(PAUSE_DOWNLOAD, false)) {
            queue.pause();
            return START_REDELIVER_INTENT;
        }
        VideoDownLoadInfo video = (VideoDownLoadInfo) intent.getSerializableExtra(VIDEOS_INFO);
        EventBus.getDefault().postSticky(video,EventBusTags.CACHE_DOWNLOAD_BEGIN);
        videos.put(video.getId()+"",video);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        DaoMaster master = GreenDaoHelper.getInstance().create(video.getDbName()).getMaster();
        master.newSession().startAsyncSession().insertOrReplace(video);
        File file = new File(dir.getAbsolutePath(), video.getId() + ".mp4");
        BaseDownloadTask task = (FileDownloader.getImpl().create(video.getVideo().getPlayUrl()).setPath(file.getAbsolutePath()).setTag(video.getId()+""));
        task.setListener(new CommonDownloadListener());
        queue.enqueue(task);
        if (isInit){
            queue.resume();
            isInit = false;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.e("Service Stop!");
    }

    private class CommonDownloadListener extends FileDownloadListener{

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            getNotificationManager().notify(1, getNotification("正在连接中...", 0,true));
            EventBus.getDefault().post((String)task.getTag(), EventBusTags.CACHE_DOWNLOAD_PENDING);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            getNotificationManager().notify(1, getNotification("正在下载视频"+task.getTag(), totalBytes > 0 && soFarBytes > 0?(int)(100L * soFarBytes / totalBytes):0,false));
            DownloadProgressInfo info = new DownloadProgressInfo((String)task.getTag());
            info.setCurrentBytes(soFarBytes);
            info.setContentLength(totalBytes);
            EventBus.getDefault().post(info, EventBusTags.CACHE_DOWNLOAD_PROGRESS);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            getNotificationManager().cancel(1);
            VideoDownLoadInfo video = videos.get((String) task.getTag());
            video.setFinish(true);
            video.setContentLength(task.getLargeFileTotalBytes());
            DaoMaster master = GreenDaoHelper.getInstance().create(video.getDbName()).getMaster();
            master.newSession().update(video);
            EventBus.getDefault().post("", EventBusTags.CACHE_DOWNLOAD_FINISH);
            shouldStop();
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            getNotificationManager().cancel(1);
            EventBus.getDefault().post(videos.get((String)task.getTag()).getId(), EventBusTags.CACHE_DOWNLOAD_CACNCEL);
            shouldStop();
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            UiUtils.makeText(CacheDownLoadService.this,e.getMessage());
            getNotificationManager().cancel(1);
        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    }

    private void shouldStop() {
        if (queue.getWaitingTaskCount() == 0&&queue.workingTask == null){
            stopSelf();
            queue.shutdown();
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }


    private Notification getNotification(String title, int progress,boolean isPending) {
        Intent intent = new Intent(this, CacheActivity.class);
        intent.putExtra(CacheActivity.FROM_NOTIFICATION, true);
        //PendingIntent是等待的Intent,这是跳转到一个Activity组件。当用户点击通知时，会跳转到MainActivity
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //设置通知的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知的大图标，当下拉系统状态栏时，就可以看到设置的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //当通知被点击的时候，跳转到MainActivity中
        builder.setContentIntent(pi);
        //设置通知的标题
        builder.setContentTitle(title);
        if (isPending){
            builder.setProgress(100, progress, true);
        }
        if (progress > 0) {
            //当progress大于或等于0时，才需要显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

}
