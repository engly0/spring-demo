package me.yangtao.spring.demo.common.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExceptionMsg implements Serializable {
    private static final long serialVersionUID = -2910817361046004120L;


    private String excName;

    private String excMessage;

    private String excFirstLine;

}
