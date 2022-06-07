package me.yangtao.spring.demo.common.error;

public class DubboServerException extends Exception{
    private ErrorCodes code;

    public DubboServerException(ErrorCodes code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public DubboServerException(ErrorCodes code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCodes getCode() {
        return code;
    }

    public DubboServerException(ErrorCodes code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public DubboServerException(ErrorCodes code, String message, boolean writableStackTrace) {
        super(message, null, true, writableStackTrace);
        this.code = code;
    }
}
