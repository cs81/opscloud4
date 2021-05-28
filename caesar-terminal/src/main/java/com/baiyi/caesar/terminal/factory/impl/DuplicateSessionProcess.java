package com.baiyi.caesar.factory.terminal.impl;

import com.baiyi.caesar.builder.TerminalSessionInstanceBuilder;
import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.factory.terminal.BaseProcess;
import com.baiyi.caesar.factory.terminal.ITerminalProcess;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.handler.RemoteInvokeHandler;
import com.baiyi.caesar.terminal.message.BaseMessage;
import com.baiyi.caesar.terminal.message.DuplicateSessionMessage;
import com.baiyi.caesar.terminal.model.HostSystem;
import com.baiyi.caesar.terminal.model.JSchSession;
import com.baiyi.caesar.terminal.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/13 10:24 上午
 * @Version 1.0
 */
@Component
public class DuplicateSessionProcess extends BaseProcess implements ITerminalProcess {

    /**
     * 复制会话
     *
     * @return
     */

    @Override
    public String getState() {
        return MessageState.DUPLICATE_SESSION.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        DuplicateSessionMessage baseMessage = (DuplicateSessionMessage) getMessage(message);
        JSchSession jSchSession = JSchSessionMap.getBySessionId(terminalSession.getSessionId(), baseMessage.getDuplicateInstanceId());
        assert jSchSession != null;
        String host = jSchSession.getHostSystem().getHost();
        HostSystem hostSystem = buildHostSystem( host, baseMessage);
        RemoteInvokeHandler.openSSHTermOnSystem(terminalSession.getSessionId(), baseMessage.getInstanceId(), hostSystem);
        terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem,baseMessage.getDuplicateInstanceId()));
    }


    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, DuplicateSessionMessage.class);
    }

}
