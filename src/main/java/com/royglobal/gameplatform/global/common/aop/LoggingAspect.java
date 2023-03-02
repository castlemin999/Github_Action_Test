package com.royglobal.gameplatform.global.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
public class LoggingAspect {
    public static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // 적용 할 대상 지정
    @Pointcut("within(com.royglobal.gameplatform.domain.*.controller..*)")
    public void cut(){}

    /*
        실행 시점 지정
        @Around : 타겟 메소드 실행 전과 후
        @Before : 타겟 메소드 실행 전
        @After : 타겟 메소드 실행 후
        @AfterReturning : 타겟 메소드 호출이 정상적으로 종료된 후
        @AfterThrowing : 타겟 메소드의 예외가 발생한 경우
    */
    @Around("com.royglobal.gameplatform.global.common.aop.LoggingAspect.cut()")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature method = (MethodSignature) pjp.getSignature();

        Object[] args = pjp.getArgs();
        if (args.length <= 0) log.info("no parameter");

        String param = getParamToString(args);

        log.info("----------> REQUEST {} ({})", method.getDeclaringTypeName(), method.getName());
        log.info("----------> param : {}", param);

        long startAt = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endAt = System.currentTimeMillis();

        logger.info("----------> RESPONSE : {}({}) = {} ({}ms)", method.getDeclaringTypeName(), method.getName(), result, endAt-startAt);

        return result;
    }

    private String getParamToString(Object[] args){
        return Arrays.stream(args)
                .map(arg -> String.format("%s -> (%s)", arg.getClass().getSimpleName(), arg))
                .collect(Collectors.joining(", "));
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet()
                .stream()
                .map(entry -> String.format("%s -> (%s)", entry.getKey(), Arrays.toString(entry.getValue())))
                .collect(Collectors.joining(", "));

             /*for (Object arg : args) {
            log.info("parameter type = {}", arg.getClass().getSimpleName());
            log.info("parameter value = {}", arg);
        }*/

    }








    // get requset value
    private String getRequestParams() throws IOException {
        String params = "";
        String messageBody = "";
        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();

        if(requestAttribute != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();



            Map<String, String[]> paramMap = request.getParameterMap();

            if(!paramMap.isEmpty()) {
                params = " [" + paramMapToString(paramMap) + "]";
            }else{
                ServletInputStream inputStream = request.getInputStream();
                messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                params = " [" + messageBody + "]";
            }
        }

        return params;
    }



}
