package com.zjc.shiro.exception;

import com.zjc.shiro.enums.StateCodeMsg;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录异常
 */
@Getter
@Setter
public class LoginException extends RuntimeException {
    private StateCodeMsg state;

    public LoginException() {
        super(StateCodeMsg.USERNAME_NULL.getMsg());
        this.state = StateCodeMsg.USERNAME_NULL;
    }

    public LoginException(StateCodeMsg state) {
        super(state.getMsg());
        this.state = state;
    }
}
