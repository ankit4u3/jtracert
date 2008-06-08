package com.google.code.jtracert.classFilter;

/**
 * @author Dmitry Bedrin
 */
public interface ClassFilterable {

    ClassFilterProcessor getClassFilterProcessor();

    void setClassFilterProcessor(ClassFilterProcessor classFilterProcessor);

}
