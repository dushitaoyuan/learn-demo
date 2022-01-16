package com.ncs.single.mvc.controller;

import com.ncs.commons.api.Result;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.commons.bean.CBeanMapper;
import com.ncs.commons.utils.ResponseUtil;
import com.ncs.single.mvc.dto.AccountDTO;
import com.ncs.single.mvc.dto.PageDTO;
import com.ncs.single.mvc.entity.AccountEntity;
import com.ncs.single.mvc.enums.RegistTypeEnum;
import com.ncs.single.mvc.enums.RoleTypeEnum;
import com.ncs.single.mvc.exception.ServiceException;
import com.ncs.single.mvc.security.RequireRole;
import com.ncs.single.mvc.service.AccountService;
import com.ncs.single.mvc.vo.AccountQueryVo;
import com.ncs.single.mvc.vo.PageVo;
import com.ncs.single.mvc.vo.RegistForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author dushitaoyuan
 * @desc
 * @date 2019/12/17
 */
@Controller
@RequireRole(role = RoleTypeEnum.ADMIN)
@RequestMapping("account")
public class AccountController {
    @Autowired
    AccountService accountService;
 
    @GetMapping("page")
    @ResponseBody
    public PageDTO<AccountDTO> page(
            AccountQueryVo queryVo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum
    ) {
        PageDTO<AccountDTO> pageDTO = accountService.pageList(new PageVo<AccountQueryVo>(pageNum, pageSize, queryVo));
        return pageDTO;
    }
    @GetMapping("list")
    @ResponseBody
    public List<AccountDTO> list(AccountQueryVo queryVo) {
        List<AccountDTO> sysList = accountService.list(queryVo);
        return sysList;
    }
    @GetMapping("get")
    @ResponseBody
    public AccountDTO get(@RequestParam("id") Long appSysId) {
        Optional<AccountDTO> accountDTO = accountService.getById(appSysId);
        accountDTO.orElseThrow(() -> {
            return new ServiceException("数据不存在");
        });
        return accountDTO.get();
    }

    @PostMapping("new")
    @ResponseBody
    public void save(AccountDTO accountDTO) {
        accountService.save(accountDTO);
    }


    @PostMapping("modify")
    @ResponseBody
    public void modify(AccountDTO accountDTO) {
        accountService.update(accountDTO);
    }




    @DeleteMapping("delete")
    @ResponseBody
    public void delete(Long id) {
        accountService.delete(id);
    }



    @GetMapping("exist")
    @ResponseBody
    public Boolean accountExist(RegistForm registForm) {
        return  accountService.exist(registForm);
    }
}
