package com.mouse.lion.pocketdj.utils;

import android.util.Log;

import com.mouse.lion.pocketdj.BuildConfig;

/**
 * Created by lionm on 1/17/2018.
 */

public class Logger {

    private static final String  TAG     = Logger.class.getSimpleName();
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    public static void v(){
        if(IS_DEBUG){
            Log.v(TAG, getMetaInfo());
        }
    }

    public static void v(String message){
        if(IS_DEBUG){
            Log.v(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void d(){
        if(IS_DEBUG){
            Log.d(TAG, getMetaInfo());
        }
    }

    public static void d(String message){
        if(IS_DEBUG){
            Log.d(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void i(){
        if(IS_DEBUG){
            Log.i(TAG, getMetaInfo());
        }
    }

    public static void i(String message){
        if(IS_DEBUG){
            Log.i(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void w(String message){
        if(IS_DEBUG){
            Log.w(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void w(String message, Throwable e){
        if(IS_DEBUG){
            Log.w(TAG, getMetaInfo() + null2str(message), e);
            printThrowable(e);
            if(e.getCause() != null){
                printThrowable(e.getCause());
            }
        }
    }

    public static void e(String message){
        if(IS_DEBUG){
            Log.e(TAG, getMetaInfo() + null2str(message));
        }
    }

    public static void e(String message, Throwable e){
        if(IS_DEBUG){
            Log.e(TAG, getMetaInfo() + null2str(message), e);
            printThrowable(e);
            if(e.getCause() != null){
                printThrowable(e.getCause());
            }
        }
    }

    public static void e(Throwable e){
        if(IS_DEBUG){
            printThrowable(e);
            if(e.getCause() != null){
                printThrowable(e.getCause());
            }
        }
    }

    private static String null2str(String string){
        if(string == null){
            return "(null)";
        }
        return string;
    }

    private static void printThrowable(Throwable e){
        Log.e(TAG, e.getClass().getName() + ": " + e.getMessage());
        for(StackTraceElement element : e.getStackTrace()){
            Log.e(TAG, "  at " + Logger.getMetaInfo(element));
        }
    }

    private static String getMetaInfo(){
        final StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        return Logger.getMetaInfo(element);
    }

    private static String getMetaInfo(StackTraceElement element){
        final String fullClassName = element.getClassName();
        final String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        final String methodName = element.getMethodName();
        final int lineNumber = element.getLineNumber();
        return "[" + simpleClassName + "#" + methodName + ":" + lineNumber + "]";
    }
}
