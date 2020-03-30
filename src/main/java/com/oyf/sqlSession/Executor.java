package com.oyf.sqlSession;

import com.oyf.pojo.Configuration;
import com.oyf.pojo.MappedStatement;

import java.util.List;

public interface Executor {
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception;

}
