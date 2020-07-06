package com.taoyuanx.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @date 2020/7/1
 */
@Data
public class DemoMsg implements Serializable {
    private String msgId;
    private String msgText;

}
