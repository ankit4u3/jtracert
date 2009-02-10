package com.google.code.jtracert.instrument;

import com.google.code.jtracert.config.InstrumentationProperties;

/**
 * @author Dmitry Bedrin
 */
public interface ConfigurableTransformer {

    InstrumentationProperties getInstrumentationProperties();

    void setInstrumentationProperties(InstrumentationProperties istrumentationProperties);

}
