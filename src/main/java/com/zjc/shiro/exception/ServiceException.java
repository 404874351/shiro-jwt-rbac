package com.zjc.shiro.exception;

import com.zjc.shiro.enums.StateCodeMsg;
import lombok.Getter;
import lombok.Setter;

/**
 * 具体业务异常
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {
    private StateCodeMsg state;

    public ServiceException(StateCodeMsg state) {
        super(state.getMsg());
        this.state = state;
    }
}
