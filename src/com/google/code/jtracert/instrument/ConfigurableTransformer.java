package com.google.code.jtracert.instrument;

import com.google.code.jtracert.config.InstrumentationProperties;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public interface ConfigurableTransformer {

    /**
     * @return
     */
    InstrumentationProperties getInstrumentationProperties();

    /**
     * @param istrumentationProperties
     */
    void setInstrumentationProperties(InstrumentationProperties istrumentationProperties);

}
