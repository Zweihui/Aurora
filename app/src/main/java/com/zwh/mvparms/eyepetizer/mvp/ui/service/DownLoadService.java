package com.zwh.mvparms.eyepetizer.mvp.ui.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.BaseApplication;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoMaster;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.CacheActivity;

import org.simple.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by Administrator on 2017/10/26 0026.
 */

public class DownLoadService extends IntentService {

    private String currentUrl;
    private VideoDownLoadInfo currentVideo;
    private List<VideoDownLoadInfo> videos = new ArrayList<>();
    public final static String VIDEOS_INFO = "videos_info";
    public final static String VIDEOS_INSERT = "videos_insert";
    public final static String PAUSE_DOWNLOAD = "pause_download";
    public final static String MORE_DOWNLOAD = "more_download";
    private OkHttpClient mOkHttpClient;
    private Call call;
    private ProgressInfo info;
    long start = 0l;


    public DownLoadService() {
        super("DownLoadService");
    }

    public DownLoadService(String name) {
        super(name);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mOkHttpClient = ((BaseApplication) getApplication()).getAppComponent().okHttpClient();
        Timber.e("onCreate");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Timber.e("onStart");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Timber.e("onStartCommand");
        VideoDownLoadInfo video = (VideoDownLoadInfo) intent.getSerializableExtra(VIDEOS_INFO);
        if (intent.getBooleanExtra(PAUSE_DOWNLOAD, false)) {
            pause();
            return START_REDELIVER_INTENT;
        }
        if (videos.size() < 1) {
            videos.add(video);
            return super.onStartCommand(intent, flags, startId);
        } else {
            videos.add(video);
            DaoMaster master = GreenDaoHelper.getInstance().create(video.getDbName()).getMaster();
            master.newSession()
                    .getVideoDownLoadInfoDao()
                    .insertOrReplace(video);
            return START_REDELIVER_INTENT;
        }


    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.e("onHandleIntent");
        VideoDownLoadInfo video = (VideoDownLoadInfo) intent.getSerializableExtra(VIDEOS_INFO);
        Boolean needInsert = intent.getBooleanExtra(VIDEOS_INSERT, true);
        currentVideo = video;
        if (needInsert) {
            DaoMaster master = GreenDaoHelper.getInstance().create(video.getDbName()).getMaster();
            master.newSession()
                    .getVideoDownLoadInfoDao()
                    .insertOrReplace(currentVideo);
        }
        beginDownload(video);
    }

    private void beginDownload(VideoDownLoadInfo video) {
        currentUrl = video.getVideo().getPlayUrl();
        String url = new String(currentUrl);
        ProgressManager.getInstance().addResponseListener(url, getDownloadListener());
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir.getAbsolutePath(), video.getId() + ".mp4");
        RandomAccessFile savedFile = null;
        if (file.exists()) {
            //如果文件存在的话，得到文件的大小
            start = file.length();
            Timber.e("start--------"+start);
        } else {
            start = 0L;
        }
        try {
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + start + "-")
                    .url(url)
                    .build();
            call = mOkHttpClient.newCall(request);
            Response response = call.execute();
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(start);
            InputStream is = response.body().byteStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                savedFile.write(buffer, 0, len);
            }
            savedFile.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            //当外部发生错误时,使用此方法可以通知所有监听器的 onError 方法
            ProgressManager.getInstance().notifyOnErorr(url, e);
        }
    }

    public void pause() {
        if (call != null) {
            call.cancel();
        } else {
            stopSelf();
            return;
        }
        getNotificationManager().cancel(1);
        EventBus.getDefault().post(videos.get(0).getId(), EventBusTags.CACHE_DOWNLOAD_CACNCEL);
        if (videos.size() > 0) {
            videos.remove(0);
            if (videos.size()>0){
                Intent intent = new Intent(this,DownLoadService.class);
                intent.putExtra(DownLoadService.VIDEOS_INFO,videos.get(0));
                onStart(intent,START_REDELIVER_INTENT);
            }else {
//                EventBus.getDefault().post(-1L, EventBusTags.CACHE_DOWNLOAD_CACNCEL);
                stopSelf();
            }
        } else {
            stopSelf();
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, CacheActivity.class);
        intent.putExtra(CacheActivity.FROM_NOTIFICATION, true);
        //PendingIntent是等待的Intent,这是跳转到一个Activity组件。当用户点击通知时，会跳转到MainActivity
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        /**
         * 几乎Android系统的每一个版本都会对通知这部分功能进行获多或少的修改，API不稳定行问题在通知上面凸显的尤其严重。
         * 解决方案是：用support库中提供的兼容API。support-v4库中提供了一个NotificationCompat类，使用它可以保证我们的
         * 程序在所有的Android系统版本中都能正常工作。
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //设置通知的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知的大图标，当下拉系统状态栏时，就可以看到设置的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //当通知被点击的时候，跳转到MainActivity中
        builder.setContentIntent(pi);
        //设置通知的标题
        builder.setContentTitle(title);
        if (progress > 0) {
            //当progress大于或等于0时，才需要显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }


    private ProgressListener getDownloadListener() {
        return new ProgressListener() {

            @Override
            public void onProgress(ProgressInfo progressInfo) {
                info = progressInfo;
                ProgressInfo _info = new ProgressInfo(currentVideo.getId());
                _info.setContentLength(progressInfo.getContentLength() + start);
                _info.setCurrentbytes(progressInfo.getCurrentbytes() + start);

//                currentVideo.setPath(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos/" + info.getId() + ".mp4").getAbsolutePath());
                getNotificationManager().notify(1, getNotification("正在下载视频" + currentVideo.getId(), _info.getPercent()));
                if (progressInfo.isFinish()) {
                    currentVideo.setFinish(true);
                    currentVideo.setContentLength(progressInfo.getContentLength() + start);
                    DaoMaster master = GreenDaoHelper.getInstance().create(currentVideo.getDbName()).getMaster();
                    master.newSession()
                            .getVideoDownLoadInfoDao()
                            .update(currentVideo);
                    getNotificationManager().cancel(1);
                    EventBus.getDefault().post("finish", EventBusTags.CACHE_DOWNLOAD_FINISH);
                    if (videos.size() > 0) {
                        videos.remove(0);
                        if (videos.size()>0){
                            Intent intent = new Intent(DownLoadService.this,DownLoadService.class);
                            intent.putExtra(DownLoadService.VIDEOS_INFO,videos.get(0));
                            onStart(intent,START_REDELIVER_INTENT);
                        }
                    }
                    return;
                }
                EventBus.getDefault().post(_info, EventBusTags.CACHE_DOWNLOAD_PROGRESS);
            }

            @Override
            public void onError(long id, Exception e) {
                EventBus.getDefault().post("finish", EventBusTags.CACHE_DOWNLOAD_CACNCEL);
            }
        };
    }

    /**
     * 返回一个Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        Timber.e("onDestroy");
        super.onDestroy();
    }
}
