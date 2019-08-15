package com.gogotalk.system.presenter.command;

import com.gogotalk.system.presenter.command.action.OpenMicAction;
/**
 * 打开麦克风信令处理实现类
 */
public class OpenMicCommandImpl implements Command {
    OpenMicAction openMicAction;

    //构造注入打开麦克风信令处理具体实现动作
    public OpenMicCommandImpl(OpenMicAction openMicAction) {
        this.openMicAction = openMicAction;
    }

    //执行打开麦克风信令处理具体实现动作
    @Override
    public void execute() {
        openMicAction.action();
    }
}
