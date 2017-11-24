package com.zwh.mvparms.eyepetizer.mvp.ui.service;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class DownloadManager {

    private final static class HolderClass{
        private final static DownloadManager INSTANCE = new DownloadManager();
    }

    public static DownloadManager getImpl(){
        return HolderClass.INSTANCE;
    }

    private ArrayList<DownloadStatusUpdater> updaterList = new ArrayList<>();

    public void startDownload(final String url, final String path){
        FileDownloader.getImpl().create(url)
                .setPath(path)
                .setListener(lis)
                .start();
    }

    public void addUpdater(final DownloadStatusUpdater updater) {
        if (!updaterList.contains(updater)) {
            updaterList.add(updater);
        }
    }

    public boolean removeUpdater(final DownloadStatusUpdater updater) {
        return updaterList.remove(updater);
    }


    private FileDownloadListener lis = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            update(task);
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            update(task);
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            update(task);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            update(task);
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
            blockComplete(task);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            update(task);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            update(task);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            update(task);
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            update(task);
        }
    };

    private void blockComplete(final BaseDownloadTask task){
        final List<DownloadStatusUpdater> updaterListCopy = (List<DownloadStatusUpdater>) updaterList.clone();
        for (DownloadStatusUpdater downloadStatusUpdater : updaterListCopy) {
            downloadStatusUpdater.blockComplete(task);
        }
    }

    private void update(final BaseDownloadTask task){
        final List<DownloadStatusUpdater> updaterListCopy = (List<DownloadStatusUpdater>) updaterList.clone();
        for (DownloadStatusUpdater downloadStatusUpdater : updaterListCopy) {
            downloadStatusUpdater.update(task);
        }
    }

    public interface DownloadStatusUpdater{
        void blockComplete(BaseDownloadTask task);
        void update(BaseDownloadTask task);
    }

}
