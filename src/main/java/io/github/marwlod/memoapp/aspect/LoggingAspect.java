package io.github.marwlod.memoapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = Logger.getLogger(getClass().getName());

    // any class, any method with any parameters in particular package
    @Pointcut("execution(* io.github.marwlod.memoapp.controller.*.*(..))")
    private void forControllerPackage() {}

    @Pointcut("execution(* io.github.marwlod.memoapp.service.*.*(..))")
    private void forServicePackage() {}

    @Pointcut("execution(* io.github.marwlod.memoapp.repository.*.*(..))")
    private void forRepositoryPackage() {}

    @Pointcut("forControllerPackage() || forServicePackage() || forRepositoryPackage()")
    private void forAppFlow() {}

    @Before("forAppFlow()")
    public void before(JoinPoint joinPoint) {
        logger.info("@Before: " + joinPoint.getSignature().toShortString());
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            logger.info(arg.toString());
        }
    }

    @AfterReturning(pointcut = "forAppFlow()",
                    returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        logger.info("@AfterReturning: " + joinPoint.getSignature().toShortString());
        logger.info("Result: " + result);
    }
}
