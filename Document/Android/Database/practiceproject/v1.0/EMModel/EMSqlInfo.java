package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

/**
 * Created by Samson on 16/6/27.
 */
public class EMSqlInfo {

    private String sqlStr;

    public EMSqlInfo(){

    }

    public EMSqlInfo(String sql){
        this.sqlStr = sql;
    }

    public String getSql() {
        return sqlStr;
    }
    public void setSql(String sqlStr) {
        this.sqlStr = sqlStr;
    }
}
