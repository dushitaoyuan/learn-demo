package com.taoyuanx.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @desc msg
 * @date 2020/7/1
 */
@Data
public class MsgDTO implements Serializable {
    private String msgId;
    private byte[] data;

    public MsgDTO() {
    }

    public MsgDTO(String msgId, byte[] data) {
        this.msgId = msgId;
        this.data = data;
    }
}
