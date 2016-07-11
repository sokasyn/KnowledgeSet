package com.emin.digit.mobile.android.notification.TestNotification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;

/**
 * Created by Samson on 16/7/11.
 */
public class SecondActivity extends Activity {

    private static final String TAG = SecondActivity.class.getSimpleName();

    // 声明按钮
    private Button cancelBtn;
    // 声明Notification
    private Notification notification;
    // 声明NotificationManager
    private NotificationManager mNotification;
    // Notification标示ID
    private int noteId = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // 实例化按钮
        cancelBtn = (Button)findViewById(R.id.cancelButton2);
        // 为按钮添加监听器
        cancelBtn.setOnClickListener(cancelListener);

        localNotify();
    }

    private void localNotify(){
        // 获取通知服务(系统service)管理器对象
        mNotification = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        // 创建通知栏构造器NotificationCompat.Builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        // 设置通知栏构造器
        mBuilder.setSmallIcon(R.mipmap.ic_launcher); //设置通知小ICON(必须)
        mBuilder.setContentTitle("测试标题"); //设置通知栏标题(必须)
        mBuilder.setContentText("测试内容");  //设置通知栏显示内容(必须)
        mBuilder.setTicker("测试通知来啦");   //通知首次出现在通知栏，带上升动画效果的
        mBuilder.setWhen(System.currentTimeMillis()); //通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
        mBuilder.setAutoCancel(true); //设置这个标志当用户单击面板就可以让通知将自动取消
//      mBuilder.setOngoing(false)//true，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                //.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        mBuilder.setNumber(2);

        // 设置PendingIntent(即,点击之后跳到的视图)
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);

        // 生成notification
        Notification notification = mBuilder.build();

        // 发送该Notification
        mNotification.notify(noteId,notification);
    }

    // 取消通知监听器
    private OnClickListener cancelListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 取消通知
            mNotification.cancel(noteId);
        }
    };
}
