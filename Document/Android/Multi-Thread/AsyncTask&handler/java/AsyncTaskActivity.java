package com.sokasyn.develop.learning.TestMultiTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Samson on 16/5/24.
 */
public class AsyncTaskActivity extends Activity{

    private DownloadTask downloadTask;
    private TextView txtTaskState;
    private final String DOWNLOAD_URL = "local test";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asyntask);

        txtTaskState = (TextView)findViewById(R.id.idTxtTaskState);
        configBtnClickListener();
    }

    // 配置按钮点击事件
    private void configBtnClickListener(){
        Button btnDown = (Button)findViewById(R.id.idBtnDownload);
        btnDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTask = new DownloadTask();
                downloadTask.execute(DOWNLOAD_URL);
            }
        });
    }

    public void btnPauseClicked(View view){
        if(downloadTask != null){
            downloadTask.pause();
        }
    }

    public void btnCancelClicked(View view){
        if(downloadTask != null){
            Log.i("info","btnCancelClicked");
            downloadTask.cancel(true);
        }
    }


    /* 异步下载图片任务类
     * 这个测试类,模拟了下载的过程
     */
    public class DownloadTask extends AsyncTask<String ,String, String>{

        public Integer progress = 0;   // 对外公布的该任务的进度
        public boolean stop = false;  // 可以控制任务的停止
        private final Integer DOWNLOAD_SIZE = 5; // 每秒钟下载增量,达到100任务结束

        /* 暂停任务,AsyncTask只能执行一次,我们可以通过api,onCancelled()来取消该任务
         * 但是onCancelled()之后,onPostExecute是不执行的
         */
        public void pause(){
            stop = true;
        }

       /*
        * 后台任务执行之前,该方法是在UI线程中执行的
        * 所以可以在后台任务执行之前做一些相关的处理,尤其是UI的,比如弹出框框等
        */
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            stop = false;
            progress = 0;
            txtTaskState.setText("0");
        }

        /*
         * 后台任务处理,该过程是在后台(子线程)中执行的
         * 所以方法内部是不允许存在对UI的操作的,如果确实需要在任务处理过程中更新UI
         * 那么可以通过publishProgress(result)将结果发布出去,在重载的onProgressUpdate()进行UI操作
         */
        @Override
        protected String doInBackground(String... params){
            String result = "0%";
            while (!stop && (progress != 100)){
                progress = downLoading(progress);
                result = progress + "%";
                publishProgress(result);
            }
            return result;
        }

        /*
         * 子线程在执行doInBackground的过程中,通过publishProgress(result)触发该方法
         * 该方法在UI线程执行,所以能更新UI
         */
        @Override
        protected void onProgressUpdate(String... result){
            super.onProgressUpdate(result);
            Log.i("info","updating.......");
            txtTaskState.setText(result[0]);
        }

        /*
         * 取消任务时候执行,不会执行onPostExecute()
         * 备注:AsyncTask的caceled()方法并不会终止任务,测试中任务还是在执行
         * 只是没有像正常情况下执行onProgressUpdate()更新UI
         * 任务结束后会执行onCancelled()
         */
        @Override
        protected void onCancelled(String result){
            Log.i("info","onCaceled");
            txtTaskState.setText("Canceled");
        }

        /*
         * 后台任务正常结束之后就会执行
         * 即doInBackground（）执行完毕就会回到UI线程执行该方法
         */
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.i("info","Done");
            txtTaskState.setText(result);
        }

        // 模拟下载过程,没隔1秒钟,返回1个DOWNLOAD_SIZE的进度
        private Integer downLoading(Integer preProgress){
            Log.i("info","dowloading....");
            sleepForSeconds(1);
            Integer progress = preProgress + DOWNLOAD_SIZE;
            progress = (progress >= 100) ? 100 : progress;
            return progress;
        }

        // 通过让线程睡眠来模拟任务要消耗的时间
        private void sleepForSeconds(long seconds){
            try{
                Thread.sleep(seconds * 1000);
            }catch (InterruptedException e){
                Log.v("sleep", "interrupted");
            }
        }
    }
}
