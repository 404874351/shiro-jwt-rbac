package com.zjc.shiro.handler;

import com.zjc.shiro.enums.StateCodeMsg;
import com.zjc.shiro.exception.LoginException;
import com.zjc.shiro.exception.ServiceException;
import com.zjc.shiro.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * controller全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({RuntimeException.class})
    public ResponseResult onRuntimeException(RuntimeException exception) {
        log.error(exception.getMessage());
        return ResponseResult.fail(StateCodeMsg.ACCESS_FAILED, null);
    }

    /**
     * 接口数据校验异常
     */
    @ExceptionHandler({BindException.class})
    public ResponseResult onBindException(BindException exception) {
        log.error(exception.getMessage());
        List<String> messageList = exception.getAllErrors().stream().map(item -> item.getDefaultMessage()).collect(Collectors.toList());
        return ResponseResult.fail(StateCodeMsg.PARAMETER_ILLEGAL, messageList);
    }

    /**
     * 登录异常
     */
    @ExceptionHandler({LoginException.class})
    public ResponseResult onLoginException(LoginException exception) {
        log.error(exception.getState().getMsg());
        return ResponseResult.fail(exception.getState(), null);
    }

    /**
     * 具体业务异常
     */
    @ExceptionHandler({ServiceException.class})
    public ResponseResult onExceptionException(ServiceException exception) {
        log.error(exception.getState().getMsg());
        return ResponseResult.fail(exception.getState(), null);
    }

}
