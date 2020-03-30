package com.oyf.utils;

public class ParameterMapping {

    private String content;

    public ParameterMapping(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ParameterMapping{" +
                "content='" + content + '\'' +
                '}';
    }
}
