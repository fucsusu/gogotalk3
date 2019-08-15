package com.gogotalk.system.presenter.command;

import com.gogotalk.system.presenter.command.action.OpenAnswerAction;
/**
 * 开始答题信令处理实现类
 */
public class OpenAnswerCommandImpl implements Command {
    OpenAnswerAction openAnswerAction;

    //构造注入开始答题信令处理具体实现动作
    public OpenAnswerCommandImpl(OpenAnswerAction openAnswerAction) {
        this.openAnswerAction = openAnswerAction;
    }

    //执行开始答题信令处理具体实现动作
    @Override
    public void execute() {
        openAnswerAction.action();
    }
}
