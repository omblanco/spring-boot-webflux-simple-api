package com.omblanco.springboot.webflux.api.commons.aop;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
public class ProfilingAspect {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilingAspect.class);

    /**
     * Registra datos de temporización en los logs.
     */
    @Around("SystemArchitecture.inServiceLayer() || SystemArchitecture.traceableElement()")
    public Object profile(ProceedingJoinPoint call) throws Throwable {
        StopWatch clock = new StopWatch("Profiling");
        try {
            clock.start(call.toShortString());
            return call.proceed();
        } finally {
            clock.stop();
            LOGGER.info(clock.prettyPrint());
        }
    	
//        String uuid = UUID.randomUUID().toString();
//        
//        try {
//            
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug(LOG_ENTER_PATTERN, uuid, call.getSignature().getDeclaringTypeName(),
//                		call.getSignature().getName(), Arrays.toString(call.getArgs()));
//            }
//            
//            Object result = call.proceed();
//
//            if (LOGGER.isDebugEnabled()) {
//                if (result instanceof Mono) {
//                    var monoResult = (Mono<?>) result;
//
//                    return monoResult.doOnSuccess(o -> {
//                        var response = "";
//                        if (Objects.nonNull(o)) {
//                            response = o.toString();
//                        }
//                        LOGGER.debug(LOG_EXIT_PATTERN, uuid,
//                        		call.getSignature().getDeclaringTypeName(), call.getSignature().getName(),
//                                response);
//                    });
//                }if (result instanceof Flux) {
//                    var fluxResult = (Flux<?>) result;
//                    return fluxResult.map(fluxItem -> {
//                        LOGGER.debug(LOG_EXIT_PATTERN, uuid, call.getSignature().getDeclaringTypeName(),
//                        		call.getSignature().getName(), fluxItem);
//                        return fluxItem;
//                    });
//                    
//                } else {
//                    LOGGER.debug(LOG_EXIT_PATTERN, uuid, call.getSignature().getDeclaringTypeName(),
//                    		call.getSignature().getName(), result);
//                }
//            }
//            return result;
//        } catch (IllegalArgumentException e) {
//            LOGGER.error(LOG_ERROR_ARROUND_PATTERN, uuid, Arrays.toString(call.getArgs()),
//            		call.getSignature().getDeclaringTypeName(), call.getSignature().getName(), e);
//            throw e;
//        }
    }

}
