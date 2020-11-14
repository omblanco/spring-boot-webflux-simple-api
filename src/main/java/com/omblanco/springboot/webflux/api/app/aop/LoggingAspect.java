package com.omblanco.springboot.webflux.api.app.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

/**
 * Registra los errores de la aplicación
 *
 */
@Aspect
public class LoggingAspect {
    
    private static final String LOG_ERROR_PATTERN = "Error en {}.{}()";
    private static final String LOG_ERROR_JDBC_PATTERN = "Error en {}.{}.{}.{}()";
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Traza correspondiente a un error en la capa de acceso a
     * @param joinPoint
     * @param e Error
     */
    @AfterThrowing(pointcut = "SystemArchitecture.inDataAccessLayer()", throwing = "e")
    public void logAfterDataAccessThrowing(JoinPoint joinPoint, Throwable e) {
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String msg = e.getMessage();
        
        if (e instanceof DataAccessException) {
            int errorCode = 0;
            
            if (e.getCause() instanceof JDBCException) {
                errorCode = ((JDBCException) e.getCause()).getErrorCode();
            }
            LOGGER.error(LOG_ERROR_JDBC_PATTERN, typeName, methodName, errorCode, msg, e);
        } else {
            LOGGER.error(LOG_ERROR_PATTERN, typeName, methodName, e);
        }
    }

    /**
     * Trazas de log de entrada/salida/excepción para la capa de servicio.
     * @param joinPoint
     * @return el objeto que sería devuelto originalmente en el método.
     * @throws Throwable Excepción lanzada por el método
     */
    @Around("SystemArchitecture.inServiceLayer() || SystemArchitecture.inWebLayer() || SystemArchitecture.inDataAccessLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint
                        .getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature()
                    .getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e);
            throw e;
        }
    }

}
