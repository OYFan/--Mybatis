package com.oyf.sqlSession;

import com.oyf.pojo.Configuration;
import com.oyf.pojo.MappedStatement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.*;

import java.util.List;
import java.util.Map;

public class DefaultSqlSession implements SqlSession{

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectList(String statementId,Object ...params) throws Exception{
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MappedStatement> mappedStatementMap = configuration.getMappedStatementMap();
        MappedStatement mappedStatement = mappedStatementMap.get(statementId);

        List<T> query = simpleExecutor.query(configuration, mappedStatement, params);
        return query;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception{
        List<T> list = selectList(statementId, params);
        if (list.size() == 1){
            return list.get(0);
        }else{
            throw new RuntimeException("查询行数不为1");
        }
    }

    @Override
    public <T> T getMapperClass(Class<?> mapperClass){
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." +methodName;
                Type methodGenericReturnType = method.getGenericReturnType();
                if (methodGenericReturnType instanceof ParameterizedType){
                    List<Object> objects = selectList(statementId, args);
                    return objects;
                }
                return selectOne(statementId,args);
            }
        });
        return (T) proxyInstance;
    }


}
