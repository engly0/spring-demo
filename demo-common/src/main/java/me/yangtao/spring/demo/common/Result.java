package me.yangtao.spring.demo.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import me.yangtao.spring.demo.common.error.ErrorCodes;

import java.io.Serializable;

/**
 * @author yangtao
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -8397719131140539797L;

    private int code;

    private String message;

    private T data;

    private Result(ErrorCodes code, String message, T data) {
        if(code == null){
            code = ErrorCodes.OK;
        }
        this.code = code.getCode();
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result<>(ErrorCodes.OK, "OK", data);
    }

    public static <T> Result<T> success(String message, T data){
        return new Result<>(ErrorCodes.OK, message, data);
    }

    public static <T> Result<T> fail(ErrorCodes code, String message){
        return new Result<>(code, message, null);
    }

}
