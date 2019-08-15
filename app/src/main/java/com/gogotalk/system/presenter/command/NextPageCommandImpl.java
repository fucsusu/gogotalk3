package com.gogotalk.system.presenter.command;

import com.gogotalk.system.presenter.command.action.NextPageAction;
/**
 * 课件跳页信令处理实现类
 */
public class NextPageCommandImpl implements Command {
    NextPageAction nextPageAction;

    //构造注入课件跳页信令处理具体实现动作
    public NextPageCommandImpl(NextPageAction nextPageAction) {
        this.nextPageAction = nextPageAction;
    }
    //执行课件跳页信令处理具体实现动作
    @Override
    public void execute() {
        nextPageAction.action();
    }
}
