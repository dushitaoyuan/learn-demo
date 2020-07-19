package com.taoyuanx.demo.es.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dushitaoyuan
 * @date 2020/7/16
 */
@Data
@Document(indexName = "contract")
public class ContractDTO implements Serializable {
    @Id
    @Field
    private Long contract_id;
    @Field

    private String contract_name;
    @Field

    private String contract_code;
    @Field

    private String contract_hash;
    @Field

    private Long plat_id;
    @Field

    private String contract_figure;
    @Field

    private String serial_number;
    @Field(type = FieldType.Date,format = DateFormat.basic_ordinal_date)
    private Date create_time;

}
