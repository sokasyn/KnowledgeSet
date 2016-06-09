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
     * js将成功和失败回调函数封装在一起,只生成一个callbackId,
     * 需要通过JSUtil.OK和JSUtil.ERROR来执行成功和失败的回调
     * 比如,如果两个execCallBack()都用JSUtil.ERROR,
     * 则执行的回调都是bridge.callbackId(success,failure)都中的failure,即第二个回调函数
     */
    public void userLogin(IWebview pWebview, JSONArray array){
        String userName = array.optString(0);
        String passwrod = array.optString(1);
        String callbackId = array.optString(2);
        boolean success = login(userName,passwrod);
        String result = "";
        if(success){
            result = "登陆成功";
            JSUtil.execCallback(pWebview,callbackId,result,JSUtil.OK,false);
        }else{
            result = "登陆失败";
            JSUtil.execCallback(pWebview,callbackId,result,JSUtil.ERROR,false);
        }
    }

    /*
     * 用户注册
     * js将成功和失败的回调函数都单独生成了callbackId,
     * 则execCallback()执行回调只需要通过对应的callbackId即可,JSUtil.OK则没有影响
     */
    public void userRegister(IWebview pWebview , JSONArray array){
        String userName = array.optString(0);
        String successCallbackId = array.optString(1);
        String failureCallbackId = array.optString(2);
        boolean success = false;
        if(userName.equals(USER_NAME)){
            success = true;
        }
        String result = "";
        if(success){
            result = "注册成功";
            JSUtil.execCallback(pWebview,successCallbackId,result,JSUtil.OK,false);
        }else{
            result = "用户名非法,注册失败";
            JSUtil.execCallback(pWebview,failureCallbackId,result,JSUtil.OK,false);
        }
    }

    /*
     * 忘记密码
     * 参数只有一个,无回调,直接返回结果,js得用同步调用方式exeSync()才能确保得到正确结果
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
     * js调用时,忽略参数列表,即参数都不写的情况,返回值是boolean类型的字符串
     */
    public String logout(IWebview pWebview , JSONArray array){
        return JSUtil.wrapJsVar(true);
    }

    /*
     * 用户签到
     * 只有一个回调
     */
    public void checkin(IWebview pWebview , JSONArray array){
        String callbackId = array.optString(0);
        S_CHECKIN_TIMES ++;
        String result =  "您成功签到了" + S_CHECKIN_TIMES + "次.";
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
