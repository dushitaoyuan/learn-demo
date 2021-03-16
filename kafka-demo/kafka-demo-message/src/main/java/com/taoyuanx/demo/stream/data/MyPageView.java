package com.taoyuanx.demo.stream.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @date 2021/3/15
 */
@Data
public class MyPageView implements Serializable {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 页面id
     */
    private String pageId;
    /**
     * 访问时间
     */
    private Long accessTime;
}
