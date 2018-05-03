package com.sinjinsong.webserver.core.context.holder;

import com.sinjinsong.webserver.core.filter.Filter;
import lombok.Data;

/**
 * @author sinjinsong
 * @date 2018/5/3
 */
@Data
public class FilterHolder {
    private Filter filter;
    private String filterClass;

    public FilterHolder(String filterClass) {
        this.filterClass = filterClass;
    }
}
