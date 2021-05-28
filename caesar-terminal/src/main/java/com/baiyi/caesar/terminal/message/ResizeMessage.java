package com.baiyi.caesar.terminal.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2020/5/12 10:44 上午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResizeMessage extends BaseMessage {

    private String instanceId;
}
