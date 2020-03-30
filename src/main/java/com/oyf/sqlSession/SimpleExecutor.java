package com.oyf.sqlSession;

import com.oyf.config.BoundSql;
import com.oyf.pojo.Configuration;
import com.oyf.pojo.MappedStatement;
import com.oyf.utils.GenericTokenParser;
import com.oyf.utils.ParameterMapping;
import com.oyf.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {


    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... param) throws  Exception{
        Connection connection = configuration.getDataSource().getConnection();

        String sqlText= mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sqlText);
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        sql = sql.trim();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        String paramterType = mappedStatement.getParamterType();
        Class<?> parameterClass = getClasss(paramterType);
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = parameterClass.getDeclaredField(content);
            field.setAccessible(true);
            Object o = field.get(param[0]);
            preparedStatement.setObject(i+1,o);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClasss(resultType);
        List<Object> list = new ArrayList<>();
        while (resultSet.next()){
            Object t = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount() ; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(columnName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(t,value);
            }
            list.add(t);
        }
        return (List<T>) list;
    }

    @Override
    public Integer insert(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception {
        Connection connection = configuration.getDataSource().getConnection();

        String sqlText= mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sqlText);
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        sql = sql.trim();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        String paramterType = mappedStatement.getParamterType();
        Class<?> parameterClass = getClasss(paramterType);
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = parameterClass.getDeclaredField(content);
            field.setAccessible(true);
            Object o = field.get(param[0]);
            preparedStatement.setObject(i+1,o);
        }
        int executeUpdate = preparedStatement.executeUpdate();
        return executeUpdate;
    }

    @Override
    public Integer update(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception {
        Connection connection = configuration.getDataSource().getConnection();

        String sqlText= mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sqlText);
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        sql = sql.trim();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        String paramterType = mappedStatement.getParamterType();
        Class<?> parameterClass = getClasss(paramterType);
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = parameterClass.getDeclaredField(content);
            field.setAccessible(true);
            Object o = field.get(param[0]);
            preparedStatement.setObject(i+1,o);
        }
        int rows = preparedStatement.executeUpdate();
        return rows;
    }

    @Override
    public Integer delete(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception {
        Connection connection = configuration.getDataSource().getConnection();

        String sqlText= mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sqlText);
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        sql = sql.trim();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        String paramterType = mappedStatement.getParamterType();
        Class<?> parameterClass = getClasss(paramterType);
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = parameterClass.getDeclaredField(content);
            field.setAccessible(true);
            Object o = field.get(param[0]);
            preparedStatement.setObject(i+1,o);
        }
        int executeUpdate = preparedStatement.executeUpdate();
        return executeUpdate;
    }

    private Class<?> getClasss(String path) throws ClassNotFoundException {
        if (path == null || "".equals(path)){
            return null;
        }
        return Class.forName(path);
    }

    private BoundSql getBoundSql(String sqlText){
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parse = genericTokenParser.parse(sqlText);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(parse,parameterMappings);
    }
}
