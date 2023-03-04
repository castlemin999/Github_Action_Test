package com.royglobal.gameplatform.global.common.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.simple.JSONObject;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * AOP를 활용하여 Request, Response 정보를 Logging
 * @since 2023.03.07
 * @author Jayden (최성민)
 * @version 1.0
 */
@Component
@Aspect
public class LoggingAspect {
    public static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    // 적용 할 대상 지정
    @Pointcut("within(com.royglobal.gameplatform.domain.*.controller..*)")
    public void pointCut(){}

    /*
        실행 시점 지정
        @Around : 타겟 메소드 실행 전과 후
        @Before : 타겟 메소드 실행 전
        @After : 타겟 메소드 실행 후
        @AfterReturning : 타겟 메소드 호출이 정상적으로 종료된 후
        @AfterThrowing : 타겟 메소드의 예외가 발생한 경우
    */
    @Around("com.royglobal.gameplatform.global.common.aop.LoggingAspect.pointCut()")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable{
        Class clazz = pjp.getTarget().getClass();
        Logger logger = LogManager.getLogger(clazz);
        Object result = null;
        try {
            result = pjp.proceed(pjp.getArgs());
            return result;
        } finally {
            logger.info("======================= Request & Response Data =======================");
            logger.info("URL : " + getRequestUrl(pjp, clazz));
            logger.info("Request Parameters : " + JSONObject.toJSONString(getParams(pjp)));
            logger.info("Response : " + result);
            logger.info("=======================================================================");
        }
    }

    private String getRequestUrl(JoinPoint joinPoint, Class clazz) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class); // class annotation
        String baseUrl = requestMapping.value()[0];
        String url = Stream.of( GetMapping.class, PutMapping.class, PostMapping.class, PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                .filter(mappingClass -> method.isAnnotationPresent(mappingClass)) // annotation이 부여되고 있는지 확인
                .map(mappingClass -> getUrl(method, mappingClass, baseUrl))
                .findFirst()
                .orElse(null);
        return url;
    }

    private String getUrl(Method method, Class<? extends Annotation> annotationClass, String baseUrl){
        Annotation annotation = method.getAnnotation(annotationClass);
        String[] value;
        String httpMethod = null;
        try {
            value = (String[])annotationClass.getMethod("value").invoke(annotation); // method Annotation
            httpMethod = (annotationClass.getSimpleName().replace("Mapping", "")).toUpperCase(); // HTTP Method
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
        return String.format("[%s] %s%s", httpMethod, baseUrl, value.length > 0 ? value[0] : "") ;
    }

    private Map getParams(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames(); // method parameter names
        Object[] args = joinPoint.getArgs(); // method parameter values
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }

}
