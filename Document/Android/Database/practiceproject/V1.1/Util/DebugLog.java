package com.emin.digit.mobile.android.database.Util;

import java.lang.reflect.Method;

/**
 * Created by Samson on 16/7/4.
 */
public class DebugLog {

    private static boolean isDebug = true;

    public static void i(String tag, String msg) {
        System.out.println(tag + " " + msg);
        if (isDebug) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void methodStart(){
        if(isDebug){
            String calledByMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            System.out.println( calledByMethod + " Start.");
        }
    }

    public static void methodStart(String className){
        print(className);
        String calledByMethodInClass = Thread.currentThread().getStackTrace()[2].getClassName();
        print(calledByMethodInClass);

        try{
            Class calledClass = Class.forName(calledByMethodInClass);

            Method[] methods = calledClass.getDeclaredMethods();
            for(int i = 0 ; i < methods.length ; i ++){
                String name =  methods[i].getName();
            }

        }catch (ClassNotFoundException e){
            e.printStackTrace();
            return;
        }

    }

    public static void methodEnd(){
        if(isDebug){
            String calledByWhom = Thread.currentThread().getStackTrace()[2].getMethodName();
            System.out.println( calledByWhom + " End.");
        }
    }

    public static void print(String message){
        System.out.println(message);
    }

    public static void print(int num){
        System.out.println("" + num);
    }

    public static void print(){
        System.out.println("test...");
    }
}
