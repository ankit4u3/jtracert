package com.google.code.jtracert.instrument.impl.adapter;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertByteCodeTransformerAdapter extends ByteCodeTransformerAdapter
        implements JTracertByteCodeTransformer {

    private JTracertByteCodeTransformer JTracertByteCodeTransformer;

    /**
     * @return
     */
    public JTracertByteCodeTransformer getJTracertByteCodeTransformer() {
        return JTracertByteCodeTransformer;
    }

    /**
     * @param JTracertByteCodeTransformer
     */
    public void setJTracertByteCodeTransformer(JTracertByteCodeTransformer JTracertByteCodeTransformer) {
        this.JTracertByteCodeTransformer = JTracertByteCodeTransformer;
    }

    /**
     * @param JTracertByteCodeTransformer
     */
    public JTracertByteCodeTransformerAdapter(JTracertByteCodeTransformer JTracertByteCodeTransformer) {
        super(JTracertByteCodeTransformer);
        assert null != JTracertByteCodeTransformer;
        setJTracertByteCodeTransformer(JTracertByteCodeTransformer);
    }

    /**
     * @return
     */
    public InstrumentationProperties getInstrumentationProperties() {
        assert null != getJTracertByteCodeTransformer();
        return getJTracertByteCodeTransformer().getInstrumentationProperties();
    }

    /**
     * @param istrumentationProperties
     */
    public void setInstrumentationProperties(InstrumentationProperties istrumentationProperties) {
        assert null != getJTracertByteCodeTransformer();
        getJTracertByteCodeTransformer().setInstrumentationProperties(istrumentationProperties);
    }

}
