package me.yangtao.spring.demo.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

@Slf4j
public class AopTargetUtils {
    public static Object getTarget(Object obj) {
        try {
            if (!AopUtils.isAopProxy(obj)) {
                if(obj instanceof Proxy){
                    return Proxy.getInvocationHandler(obj);
                }
                return obj;
            }
            //判断是jdk还是cglib代理
            if (AopUtils.isJdkDynamicProxy(obj)) {
                obj = getJdkDynamicProxyTargetObject(obj);
            } else {
                obj = getCglibDynamicProxyTargetObject(obj);
            }
        } catch (Exception e) {
            log.error("getTarget err:", e);
        }
        return obj;
    }

    private static Object getCglibDynamicProxyTargetObject(Object obj) throws Exception {
        Field h = obj.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);

        Object dynamicAdvisedInterceptor = h.get(obj);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }

    private static Object getJdkDynamicProxyTargetObject(Object obj) throws Exception {
        Field h = obj.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);

        AopProxy aopProxy = (AopProxy) h.get(obj);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }
}
