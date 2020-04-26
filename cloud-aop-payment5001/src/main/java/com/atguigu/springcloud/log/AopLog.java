package com.atguigu.springcloud.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class AopLog {

	@Pointcut("execution(* com.atguigu.springcloud.service.UserService.insertUser(..))")
	public void pointCut(){
		log.info("切入点");
	}

	@Before("pointCut()")
	public void beforeAdvice(){
		log.info("beforeAdvice");
	}

	@After("pointCut()")
	public void afteAdvice(){
		log.info("afterAdvice");
	}

	@Around("pointCut()")
	public void around(ProceedingJoinPoint pjp) throws Throwable{
		log.info("环绕前");
		pjp.proceed();
		log.info("环绕后");
	}
}
