package com.pwf.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by PWF on 2018/11/19.
 */
@Data
public class PageResult<T> {
    private Long total; //总记录数
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}
