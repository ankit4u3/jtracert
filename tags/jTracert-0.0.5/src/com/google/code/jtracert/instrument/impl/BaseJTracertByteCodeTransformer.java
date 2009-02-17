package com.google.code.jtracert.instrument.impl;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;

/**
 * @author Dmitry Bedrin
 */
public abstract class BaseJTracertByteCodeTransformer implements JTracertByteCodeTransformer {

    private InstrumentationProperties instrumentationProperties;

    protected BaseJTracertByteCodeTransformer() {
        super();
    }

    public BaseJTracertByteCodeTransformer(InstrumentationProperties instrumentationProperties) {
        this();
        setInstrumentationProperties(instrumentationProperties);
    }

    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

    public abstract byte[] transform(byte[] originalBytes, int offset, int length) throws ByteCodeTransformException;

}
