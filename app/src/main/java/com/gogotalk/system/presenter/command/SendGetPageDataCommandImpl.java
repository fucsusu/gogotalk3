package com.gogotalk.system.presenter.command;

import com.gogotalk.system.presenter.command.action.AuxAction;
import com.gogotalk.system.presenter.command.action.SendGetPageDataAction;

/**
 * 发送获取页面信令处理实现类
 */
public class SendGetPageDataCommandImpl implements Command {
    SendGetPageDataAction sendGetPageDataAction;

    //构造注入混音具体实现动作
    public SendGetPageDataCommandImpl(SendGetPageDataAction sendGetPageDataAction) {
        this.sendGetPageDataAction = sendGetPageDataAction;
    }

    //执行混音具体实现动作
    @Override
    public void execute() {
        sendGetPageDataAction.action();
    }
}
