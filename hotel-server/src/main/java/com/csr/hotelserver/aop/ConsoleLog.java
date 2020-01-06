package com.csr.hotelserver.aop;


import com.csr.hotelserver.util.exception.MyException;
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
            object = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
//            if (throwable instanceof MyException){
//                System.err.println(throwable.getMessage());
//                return ResultUtil.error(throwable.getMessage());
//            }else {
//                System.err.println(throwable.getMessage());
//                return ResultUtil.error("服务器未知异常");
//            }
        }
        return object;
    }

}
