package com.google.code.jtracert.classFilter;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public interface ClassFilterable {

    /**
     * @return
     */
    ClassFilterProcessor getClassFilterProcessor();

    /**
     * @param classFilterProcessor
     */
    void setClassFilterProcessor(ClassFilterProcessor classFilterProcessor);

}
