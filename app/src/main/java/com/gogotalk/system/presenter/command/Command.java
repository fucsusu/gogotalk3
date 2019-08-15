package com.gogotalk.system.presenter.command;

import java.util.Map;

/**
 * 命令接口
 */
public interface Command {
    /**
     * 执行命令
     */
    void execute();
    /**
     * 返回数据回调接口
     */
    interface DataCallBack{
        Map<String,Object> onCallBack(Map<String,Object> map);
    }
}
