package me.yangtao.spring.demo.common.utils.exception;

import org.apache.commons.lang3.StringUtils;

public class ExceptionAnalyzeUtil {
    private static final String PACKAGE_HEAD = packageHead();

    private static String packageHead(){
        String packageName = ExceptionAnalyzeUtil.class.getPackage().getName();
        StringBuilder stb = new StringBuilder();
        int step = 2;
        for(String dir: StringUtils.split(packageName,'.')){
            if(step-- > 0){
                if(stb.length() > 0){
                    stb.append('.');
                }
                stb.append(dir);
            }
        }
        return stb.toString();
    }

    public static ExceptionInfo analyze(Throwable e){
        if(e == null){
            return null;
        }
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        analyze(e, exceptionInfo);
        return exceptionInfo;
    }

    private static void analyze(Throwable e, ExceptionInfo exceptionInfo){
        ExceptionMsg msg = new ExceptionMsg();
        msg.setExcName(e.getClass().getName());
        msg.setExcMessage(e.getMessage());
        for(StackTraceElement element : e.getStackTrace()){
            String errInfo = element.toString();
            if(errInfo.contains(PACKAGE_HEAD)){
                msg.setExcFirstLine(errInfo);
                break;
            }
        }
        exceptionInfo.appendExceptionMsg(msg);
        if(e.getCause() != null){
            analyze(e.getCause(), exceptionInfo);
        }
    }

}
