package me.yangtao.spring.demo.common.error;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCodes {
    /**
     * 0 成功，无异常；
     */
    OK(0,"成功，无异常"),
    /**
     * 3000 网络异常；
     */
    INTERNAL_ERROR(3000,"网络异常"),
    /**
     * 5030 服务异常
     */
    SERVER_UNAVAILABLE(5030,"服务异常"),
    /**
     * 5031 服务异常（因第三方依赖产生）
     */
    SERVER_DEPENDENT_ERROR(5031,"服务异常（因第三方依赖产生）"),
    /**
     * 4000 参数校验不通过
     */
    PARAM_ERROR(4000,"参数校验不通过"),
    /**
     * 4010 用户验证未通过
     */
    NOT_AUTHORIZED(4010,"用户验证未通过"),
    /**
     * 4030 拒绝服务
     */
    FORBIDDEN(4030,"拒绝服务"),
    /**
     * 4230 资源被锁定
     */
    Locked(4230,"资源被锁定");

    private int code;
    private String desc;
    private static Map<Integer, ErrorCodes> codeMap = new HashMap<>();
    static {
        for(ErrorCodes status: ErrorCodes.values()){
            codeMap.put(status.code, status);
        }
    }
    ErrorCodes(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static ErrorCodes getByCode(int code){
        return codeMap.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
