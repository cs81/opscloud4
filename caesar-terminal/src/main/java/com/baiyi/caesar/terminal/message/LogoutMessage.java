package com.baiyi.caesar.terminal.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2020/5/11 5:18 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogoutMessage extends BaseMessage {

    private String instanceId;
}
