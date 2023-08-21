package com.zjc.shiro.exception;

import com.zjc.shiro.enums.StateCodeMsg;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationException;

/**
 * token异常
 */
@Getter
@Setter
public class TokenException extends AuthenticationException {
    private StateCodeMsg state;

    public TokenException(StateCodeMsg state) {
        super(state.getMsg());
        this.state = state;
    }
}
