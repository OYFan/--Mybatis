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
    public Integer insert(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MappedStatement> mappedStatementMap = configuration.getMappedStatementMap();
        MappedStatement mappedStatement = mappedStatementMap.get(statementId);
        Integer rows = simpleExecutor.update(configuration, mappedStatement, params);
        return rows;
    }

    @Override
    public Integer update(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MappedStatement> mappedStatementMap = configuration.getMappedStatementMap();
        MappedStatement mappedStatement = mappedStatementMap.get(statementId);
        Integer rows = simpleExecutor.update(configuration, mappedStatement, params);
        return rows;
    }

    @Override
    public Integer delete(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MappedStatement> mappedStatementMap = configuration.getMappedStatementMap();
        MappedStatement mappedStatement = mappedStatementMap.get(statementId);
        Integer rows  = simpleExecutor.delete(configuration, mappedStatement, params);
        return rows;
    }

    public Integer dispatch(String statementId, Object... params) throws Exception{
        Map<String, MappedStatement> mappedStatementMap = configuration.getMappedStatementMap();
        MappedStatement mappedStatement = mappedStatementMap.get(statementId);
        String executeType = mappedStatement.getExecuteType();
        if ("insert".equals(executeType)) {
            return insert(statementId,params);
        }else if ("update".equals(executeType)){
            return update(statementId,params);
        }else if ("delete".equals(executeType)){
            return delete(statementId,params);
        }
        return null;
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
                }else if(method.getReturnType() == Integer.class){//返回值为Integer则调用增删改操作
                    return dispatch(statementId,args);
                }
                return selectOne(statementId,args);
            }
        });
        return (T) proxyInstance;
    }


}
