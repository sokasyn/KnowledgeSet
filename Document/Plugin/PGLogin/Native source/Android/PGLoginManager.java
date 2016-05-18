package com.summer.dev.test.plugin;

import org.json.JSONArray;

import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;

/**
 * Created by samson on 16/5/18.
 */
public class PGLoginManager extends StandardFeature{

    private final String USER_NAME = "Samson";
    private final String PASSWORD = "123";

    private static int S_CHECKIN_TIMES = 0;
    /*
     * 用户登陆
     */
    public String userLogin(IWebview pWebview, JSONArray array){
        String userName = array.optString(0);
        String passwrod = array.optString(1);
        boolean success = login(userName,passwrod);
        return JSUtil.wrapJsVar(success);
    }

    /*
     * 用户注册
     */
    public String userRegister(IWebview pWebview , JSONArray array){
        String result = "注册成功";
        return JSUtil.wrapJsVar(result);
    }

    /*
     * 忘记密码
     */
    public String forgetPassword(IWebview pWebview , JSONArray array){
        String userName = array.optString(0);
        String result = "";
        if (userName.equals(USER_NAME)){
            result = PASSWORD;
        }
        return JSUtil.wrapJsVar(result);
    }

    /*
     * 用户登出
     */
    public String logout(IWebview pWebview , JSONArray array){
        return JSUtil.wrapJsVar(true);
    }

    /*
     * 用户签到
     * 测试JS异步调用,回调反馈,多线程(android的mesage机制等)
     */
    public void checkin(IWebview pWebview , JSONArray array){
        String callbackId = array.optString(0);
        String userName = array.optString(1);
        S_CHECKIN_TIMES ++;
        String result = userName + ",您成功签到了" + S_CHECKIN_TIMES + "次.";
        JSUtil.execCallback(pWebview,callbackId,result,JSUtil.OK,false);
    }

    private boolean login(String userName , String passwrod){
        boolean success = false;
        if(userName.equals(USER_NAME) && passwrod.equals(PASSWORD)){
            success = true;
        }
        return success;
    }

    private void printParam(JSONArray paramArray){
        System.out.println(getParamString(paramArray));
    }

    private String getParamString(JSONArray paramArray){
        if (paramArray == null) return null;
        String result = "";
        int length = paramArray.length();
        for(int i = 0 ; i < length ; i++){
            if (i == 0){
                result += "[";
            }
            result += paramArray.optString(i);
            if (i == (length-1)){
                result += "]";
            }else{
                result += ",";
            }
        }
        return result;
    }
}
