package com.google.code.jtracert.filter;

/**
 * @author Dmitry Bedrin
 */
public interface Filterable {

    ClassFilterProcessor getClassFilterProcessor();

    void setClassFilterProcessor(ClassFilterProcessor classFilterProcessor);

}
