package com.ncs.sprinbase.core.dto;

import java.io.Serializable;

import java.util.List;

import com.github.pagehelper.Page;

import cn.hutool.core.collection.CollectionUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dushitaoyuan
 * @date 2019/9/24 14:59
 * @desc 分页查询返回结果
 */
@Getter
@Setter
public class PageDTO<T> implements Serializable {

    /**
     * list 结果
     * total 总条数
     * pageSize 页大小
     * pageNum 当前页
     */
    private List<T> list;
    private Long    total;
    private Integer pageSize;
    private Integer pageNum;

    private PageDTO() {}

    public static <T> PageDTO<T> changeListType(List<T> list, Page page) {
        PageDTO<T> pageDTO = newPage(list, page.getTotal(), page.getPageSize(), page.getPageNum());

        return pageDTO;
    }

    /**
     * 分页插件支持
     *
     * @param page
     */
    public static <T> PageDTO<T> gitHubPage(Page<T> page) {
        return newPage(page.getResult(), page.getTotal(), page.getPageSize(), page.getPageNum());
    }

    public static <T> PageDTO<T> newPage(List<T> list, Long total, Integer pageSize, Integer pageNum) {
        PageDTO<T> pageDTO = new PageDTO<T>();

        pageDTO.setList(list);
        pageDTO.setPageNum(pageNum);
        pageDTO.setTotal(total);
        pageDTO.setPageSize(pageSize);

        return pageDTO;
    }

    public boolean noData() {
        return CollectionUtil.isEmpty(this.list);
    }

    public boolean hasData() {
        return CollectionUtil.isNotEmpty(this.list);
    }
}


