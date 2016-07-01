package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import android.content.Context;

/**
 * Created by Samson on 16/6/27.
 */
/*
 * 数据信息的配置
 */
public class EMDaoConfig {

    private  static final String DB_NAME = "default_config.db";
    private  static final int DB_VERSION = 1;

    private Context mContext = null; // android上下文
    private String mDbName = DB_NAME; // 数据库名字
    private int dbVersion = DB_VERSION; // 数据库版本
    private boolean debug = true; // 是否是调试模式（调试模式 增删改查的时候显示SQL语句）
    // private boolean saveOnSDCard = false;//是否保存到SD卡
    private String targetDirectory;// 数据库文件在sd卡中的目录

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public String getDbName() {
        return mDbName;
    }

    public void setDbName(String dbName) {
        this.mDbName = dbName;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    // public boolean isSaveOnSDCard() {
    // return saveOnSDCard;
    // }
    //
    // public void setSaveOnSDCard(boolean saveOnSDCard) {
    // this.saveOnSDCard = saveOnSDCard;
    // }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }
}
