package com.ncs.sprinbase.core.event.listener;

import com.ncs.sprinbase.core.event.RegistEvent;
import com.ncs.sprinbase.core.event.dto.RegistEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc 注册事件监听器
 * @date 2020/1/2
 */
@Component
@Slf4j
public class RegistLogEventListener implements ApplicationListener<RegistEvent> {
    @Override
    public void onApplicationEvent(RegistEvent event) {
        Object source = event.getSource();
        if(Objects.nonNull(source)){
            RegistEventDTO registEventDTO= (RegistEventDTO) source;
            log.info("注册用户总数加1");
        }

    }
}
