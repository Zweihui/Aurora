package com.zwh.mvparms.eyepetizer.mvp.ui.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.jess.arms.base.BaseApplication;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoMaster;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.MainActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/26 0026.
 */

public class DownLoadService extends IntentService {

    private String currentUrl;
    private VideoDownLoadInfo currentVideo;
    public final static String VIDEOS_INFO = "videos_info";
    public final static String PAUSE_DOWNLOAD = "pause_download";
    private OkHttpClient mOkHttpClient;
    private Call call;
    private DownloadBinder mBinder = new DownloadBinder();


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
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(PAUSE_DOWNLOAD,false)){
            pause();
            return START_REDELIVER_INTENT;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        VideoDownLoadInfo video = (VideoDownLoadInfo) intent.getSerializableExtra(VIDEOS_INFO);
        currentVideo = video;
        ProgressManager.getInstance().addResponseListener(video.getVideo().getPlayInfo().get(0).getUrl(), getDownloadListener());
        DaoMaster master = GreenDaoHelper.getInstance().create(video.getDbName()).getMaster();
        VideoDownLoadInfo videofromdao = master.newSession().getVideoDownLoadInfoDao().loadByRowId(video.getId());
        master.newSession()
                .getVideoDownLoadInfoDao()
                .insert(currentVideo);
        beginDownload(video);
    }

    private void beginDownload(VideoDownLoadInfo video) {
        currentUrl = video.getVideo().getPlayInfo().get(0).getUrl();
        String url = new String(currentUrl);
        ProgressManager.getInstance().addResponseListener(url,getDownloadListener());
        Long start = video.getCurrentBytes() == null ? 0 : video.getCurrentBytes();
        try {
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes="+start+"-")
                    .url(url)
                    .build();
            call = mOkHttpClient.newCall(request);
            Response response = call.execute();

            InputStream is = response.body().byteStream();
            //为了方便就不动态申请权限了,直接将文件放到CacheDir()中
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos/" + video.getId() + ".mp4");
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos");
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(dir.getAbsolutePath() , video.getId() + ".mp4");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
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
        }
        String url = new String(currentUrl);
        List<ProgressListener> listeners = ProgressManager.getInstance().getListener(url);
        if (StringUtils.isEmpty(listeners)) {
            for (ProgressListener lis : listeners) {
                lis.onPause();
            }
        }
        getNotificationManager().cancel(1);
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
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
            ProgressInfo info;

            @Override
            public void onProgress(ProgressInfo progressInfo) {
                info = progressInfo;
                currentVideo.setContentLength(progressInfo.getContentLength());
                currentVideo.setCurrentBytes(progressInfo.getCurrentbytes());
                currentVideo.setPath(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos/" + info.getId() + ".mp4").getAbsolutePath());
                getNotificationManager().notify(1, getNotification("正在下载视频" + currentVideo.getId() + "...", progressInfo.getPercent()));
                if (progressInfo.getPercent() == 100){
                    currentVideo.setFinish(true);
                    getNotificationManager().cancel(1);
                }
            }

            @Override
            public void onError(long id, Exception e) {

            }


            @Override
            public void onPause() {

            }
        };
    }

    /**
     * 返回一个Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public DownLoadService getService() {
            return DownLoadService.this;
        }
    }

}
