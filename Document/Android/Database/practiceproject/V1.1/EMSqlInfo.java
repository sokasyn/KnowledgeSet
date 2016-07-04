package com.emin.digit.mobile.android.database;

/**
 * Created by Samson on 16/7/4.
 *
 * 数据库可执行的sql的信息的封装
 */
public class EMSqlInfo {
    private String sqlStr;

    public EMSqlInfo(){

    }

    /**
     * 构造方法
     *
     * @param sql sql语句
     */
    public EMSqlInfo(String sql){
        this.sqlStr = sql;
    }

    /**
     * 获取sql语句
     *
     * @return
     */
    public String getSql() {
        return sqlStr;
    }

    /**
     * 设置sql语句
     *
     * @param sqlStr sql语句
     */
    public void setSql(String sqlStr) {
        this.sqlStr = sqlStr;
    }
}
