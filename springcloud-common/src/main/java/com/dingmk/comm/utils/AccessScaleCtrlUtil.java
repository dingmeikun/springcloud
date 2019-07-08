package com.dingmk.comm.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 访问比例控制器
 * Created by cat on 2017/4/13.
 */
public class AccessScaleCtrlUtil {
    private int scale_pass = 0;
    private int scale_do = 0;
    private int scale_total = 0;

    /**
     * 构造函数
     *
     * @param scale_pass 服务放通的比例因子
     * @param scale_do   服务处理的比例因子
     */
    public AccessScaleCtrlUtil(int scale_pass, int scale_do) {
        this.scale_pass = scale_pass;
        this.scale_do = scale_do;
        this.scale_total = this.scale_pass + this.scale_do;
    }

    private Lock counter_lock = new ReentrantLock();
    private int counter = 0;

    /**
     * 根据控制比例设置，判断是否需要处理请求
     *
     * @return
     */
    public boolean needDo() {
        if (this.scale_pass < 1) {
            return true;
        }

        if (this.scale_do < 1) {
            return false;
        }

        try {
            counter_lock.lock();

            counter += 1;
            if (counter <= scale_pass) {
                return false;
            } else if (counter <= scale_total) {
                return true;
            } else {
                counter = 1;
                return false;
            }
        } finally {
            counter_lock.unlock();
        }

    }
}
