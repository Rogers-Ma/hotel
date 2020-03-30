package com.csr.hotelserver.aop;

import com.csr.hotelserver.util.result.ResultUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class ConsoleLog {

    @Pointcut(value = "execution(* com.csr.hotelserver.controller.*.*(..))")
    public void verification() {
    }

    @Around("verification()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        String methodName = proceedingJoinPoint.getSignature().getName();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        Object[] objects = proceedingJoinPoint.getArgs();
        System.out.println("访问接口: " + className + "===>" + methodName);
        System.out.println("参   数： " + Arrays.toString(objects));
        Object object = null;
        try {
            object = proceedingJoinPoint.proceed(objects);
        } catch (Throwable throwable) {
            System.err.println("错   误： "+throwable.getMessage());
            return ResultUtil.error(throwable.getMessage());
        }
        return object;
    }

}
