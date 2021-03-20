package com.alex.homework.rpcfx.demo.consumer.aspect;

import com.alex.homework.rpcfx.annotation.Remote;
import com.alex.homework.rpcfx.client.Rpcfx;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * TODO
 *
 * @author Alex Shen
 */
@Aspect
@Component
public class RemoteAnnotationAspect {
    @Before("execution(* com.alex.homework.rpcfx.demo.consumer..*.*(..))")
    private void createProxy(JoinPoint jointPoint) throws Exception {
        Object target = jointPoint.getTarget();
        Field[] fields = target.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Remote remote = field.getAnnotation(Remote.class);
            if(remote != null){
                if(field.get(target) == null){
                    field.set(target , Rpcfx.create(field.getType() , remote.url()));
                }
            }
        }
    }
}
