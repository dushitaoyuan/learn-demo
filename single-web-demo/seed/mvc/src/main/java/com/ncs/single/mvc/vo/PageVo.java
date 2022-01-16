package com.ncs.single.mvc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author lianglei
 * @date 2019/9/24 14:47
 * @desc 分页查询
 **/
@Getter
@Setter
public class PageVo<T> implements Serializable {
    /**
     * pageSize 分页大小
     * pageNum 当前页码
     * query 查询参数对象
     */
    private Integer pageSize = 10;
    private Integer pageNum = 1;
    private T query;

    public PageVo() {
    }

    public PageVo(Integer pageNum, Integer pageSize) {
        if (Objects.nonNull(pageNum)) {
            this.pageNum = pageNum;
        }
        if (Objects.nonNull(pageSize)) {
            this.pageSize = pageSize;
        }
    }

    public PageVo(Integer pageNum, Integer pageSize, T query) {
        this(pageNum, pageSize);
        this.query = query;
    }


}
