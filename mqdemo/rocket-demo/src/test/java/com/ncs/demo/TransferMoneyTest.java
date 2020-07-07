package com.ncs.demo;

import com.taoyuanx.demo.DemoBootApplication;
import com.taoyuanx.demo.business.dto.TransferDTO;
import com.taoyuanx.demo.business.entity.UserAccountEntity;
import com.taoyuanx.demo.business.service.TransferHelperService;
import com.taoyuanx.demo.business.service.TransferMoneyService;
import com.taoyuanx.demo.config.DemoConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author dushitaoyuan
 * @desc 测试生产者
 * @date 2020/6/2
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
public class TransferMoneyTest {

    @Autowired
    TransferMoneyService transferMoneyService;
    @Autowired
    TransferHelperService transferHelperService;


    @Test
    public void test() throws Exception {
        /**
         * a-b
         */
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setMoney(10D);
        transferDTO.setSerialNo("123456-a-b");
        transferDTO.setFrom(transferHelperService.getByIdA(1L));
        transferDTO.setTo(transferHelperService.getByIdB(2L));
        transferDTO.setTransferType(DemoConfig.TRANSFER_TYPE_A_B);
        transferMoneyService.transferMoney(transferDTO);


        /**
         * b-a
         */

        TransferDTO transferDTOBA = new TransferDTO();
        transferDTOBA.setMoney(20D);
        transferDTOBA.setSerialNo("12345678-b-a");
        transferDTOBA.setFrom(transferHelperService.getByIdB(2L));
        transferDTOBA.setTo(transferHelperService.getByIdA(1L));
        transferDTOBA.setTransferType(DemoConfig.TRANSFER_TYPE_B_A);
        transferMoneyService.transferMoney(transferDTOBA);
        Thread.sleep(300000);
    }

}
