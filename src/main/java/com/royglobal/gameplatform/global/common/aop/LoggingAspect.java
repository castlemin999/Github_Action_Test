package com.royglobal.gameplatform.global.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


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
        Class clazz = pjp.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(clazz);
        Object result = null;
        try {
            result = pjp.proceed(pjp.getArgs());
            return result;
        } finally {
            logger.info(getRequestUrl(pjp, clazz));
            logger.info("parameters : " + JSONObject.toJSONString(params(pjp)));
            logger.info("response : " + result);
        }
    }

    private String getRequestUrl(JoinPoint joinPoint, Class clazz) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
        String baseUrl = requestMapping.value()[0];

        String url = Stream.of( GetMapping.class, PutMapping.class, PostMapping.class,
                        PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                .filter(mappingClass -> method.isAnnotationPresent(mappingClass))
                .map(mappingClass -> getUrl(method, mappingClass, baseUrl))
                .findFirst().orElse(null);
        return url;
    }

    private String getUrl(Method method, Class<? extends Annotation> annotationClass, String baseUrl){
        Annotation annotation = method.getAnnotation(annotationClass);
        String[] value;
        String httpMethod = null;
        try {
            value = (String[])annotationClass.getMethod("value").invoke(annotation);
            httpMethod = (annotationClass.getSimpleName().replace("Mapping", "")).toUpperCase();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
        return String.format("%s %s%s", httpMethod, baseUrl, value.length > 0 ? value[0] : "") ;
    }

    private Map params(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }

}
