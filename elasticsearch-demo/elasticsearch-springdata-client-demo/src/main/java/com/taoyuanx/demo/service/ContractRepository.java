package com.taoyuanx.demo.service;

import com.taoyuanx.demo.es.dto.ContractDTO;
import org.springframework.data.repository.CrudRepository;


public interface ContractRepository extends CrudRepository<ContractDTO, Long> {


}
