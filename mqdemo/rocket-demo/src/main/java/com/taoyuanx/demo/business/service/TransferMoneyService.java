package com.taoyuanx.demo.business.service;

import com.taoyuanx.demo.business.dto.TransferDTO;

/**
 * @author dushitaoyuan
 * @desc 转账服务
 * @date 2020/7/6
 */
public interface TransferMoneyService {

    void transferMoney(TransferDTO transferDTO) throws Exception;
}
