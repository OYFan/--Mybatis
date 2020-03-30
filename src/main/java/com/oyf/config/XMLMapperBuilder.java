package com.oyf.config;

import com.oyf.pojo.Configuration;
import com.oyf.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse (InputStream in) throws DocumentException {
        Document mapperDocument = new SAXReader().read(in);
        Element mapperRootElement = mapperDocument.getRootElement();
        String namespace = mapperRootElement.attributeValue("namespace");
        List<Element> selectNodes = mapperDocument.selectNodes("//select");
        List<Element> insertNodes = mapperDocument.selectNodes("//insert");
        List<Element> updateNodes = mapperDocument.selectNodes("//update");
        List<Element> deleteNodes = mapperDocument.selectNodes("//delete");
        putMappedStatement(configuration,selectNodes,namespace,"select");
        putMappedStatement(configuration,insertNodes,namespace,"insert");
        putMappedStatement(configuration,updateNodes,namespace,"update");
        putMappedStatement(configuration,deleteNodes,namespace,"delete");

    }

    private void putMappedStatement(Configuration configuration,List<Element> nodes,String namespace,String executeType){
        for (Element ele : nodes) {
            MappedStatement mappedStatement = new MappedStatement();
            String id = ele.attributeValue("id");
            String resultType = ele.attributeValue("resultType");
            String paramterType = ele.attributeValue("paramterType");
            String sqlText = ele.getText();
            mappedStatement.setId(id);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sqlText);
            mappedStatement.setExecuteType(executeType);
            configuration.getMappedStatementMap().put(namespace + "." + id,mappedStatement);
        }
    }
}
