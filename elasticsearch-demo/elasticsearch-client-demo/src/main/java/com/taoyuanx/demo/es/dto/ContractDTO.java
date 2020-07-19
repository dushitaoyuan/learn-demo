package com.taoyuanx.demo.es.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @author dushitaoyuan
 * @date 2020/7/16
 */
@Data
public class ContractDTO implements Serializable {
    private Long contract_id;
    private String contract_name;

    private String contract_code;

    private String contract_hash;

    private Long plat_id;

    private String contract_figure;

    private String serial_number;
    private Date create_time;

}
