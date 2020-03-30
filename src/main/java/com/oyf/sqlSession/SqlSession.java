package com.oyf.sqlSession;

import java.util.List;

public interface SqlSession {

    public <T> List<T> selectList(String statementId,Object ...params) throws Exception;

    public <T> T selectOne(String statementId,Object ... params) throws Exception;

    public  <T> T getMapperClass(Class<?> mapperClass) throws Exception;
}
