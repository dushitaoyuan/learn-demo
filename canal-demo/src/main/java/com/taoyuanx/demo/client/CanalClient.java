package com.taoyuanx.demo.client;

public abstract class CanalClient {
    protected volatile boolean runing = false;


    public void start() {
        doStart();
        runing = true;
    }


    public void stop() {
        doStop();
        runing = false;
    }
    protected abstract void doStart();

    protected abstract void doStop();


}
