package com.ncs.sprinbase.core.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author dushitaoyuan
 * @date 2020/1/2
 */
@Data
@AllArgsConstructor
public class RegistEventDTO {
    private Long userId;
    private Long accountId;
}
