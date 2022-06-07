package me.yangtao.spring.demo.common.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public abstract class BaseRequest implements Serializable {
    private static final long serialVersionUID = -4791205387709453145L;
    /**
     * 请求系统来源
     */
    protected String from;

    public void checkFrom() throws Exception {
        if(StringUtils.isBlank(from)){
            throw new RuntimeException("from参数为空");
        }
    }
}
