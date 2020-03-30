package com.oyf.sqlSession;

import com.oyf.pojo.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public SqlSession openSession() {
        DefaultSqlSession defaultSqlSession = new DefaultSqlSession(configuration);
        return defaultSqlSession;
    }
}
