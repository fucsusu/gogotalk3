package com.gogotalk.system.presenter.command;

import com.gogotalk.system.presenter.command.action.AuxAction;

/**
 * 混音信令处理实现类
 */
public class AuxCommandImpl implements Command {
    AuxAction auxAction;

    //构造注入混音具体实现动作
    public AuxCommandImpl(AuxAction auxAction) {
        this.auxAction = auxAction;
    }

    //执行混音具体实现动作
    @Override
    public void execute() {
        auxAction.action();
    }
}
