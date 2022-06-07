package me.yangtao.spring.demo.common.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import me.yangtao.spring.demo.common.utils.HostUtil;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExceptionInfo implements Serializable {

    private static final long serialVersionUID = -6359188410416084628L;
    private String serverIp;

    private List<ExceptionMsg> exceptionList;

    public ExceptionInfo(){
        serverIp = HostUtil.getIp();
    }

    public void appendExceptionMsg(ExceptionMsg exceptionMsg){
        if(exceptionList == null){
            exceptionList = new LinkedList<>();
        }
        exceptionList.add(exceptionMsg);
    }
}
