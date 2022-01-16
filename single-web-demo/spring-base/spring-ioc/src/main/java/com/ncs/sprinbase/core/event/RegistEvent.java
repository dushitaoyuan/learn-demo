package com.ncs.sprinbase.core.event;

import com.ncs.sprinbase.core.event.dto.RegistEventDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @author dushitaoyuan
 * @desc 用途描述
 * @date 2020/1/2
 */
public class RegistEvent extends ApplicationEvent {
    public RegistEvent(RegistEventDTO registEventDTO) {
        super(registEventDTO);
    }

    public RegistEvent(Long userId,Long accountId) {
        super(new RegistEventDTO(userId,accountId));
    }

}
