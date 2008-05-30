package com.google.code.jtracert.instrument.impl.adapter;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;

/**
 * @author Dmitry Bedrin
 */
public class JTracertByteCodeTransformerAdapter extends ByteCodeTransformerAdapter
        implements JTracertByteCodeTransformer {

    private JTracertByteCodeTransformer JTracertByteCodeTransformer;

    public JTracertByteCodeTransformer getJTracertByteCodeTransformer() {
        return JTracertByteCodeTransformer;
    }

    public void setJTracertByteCodeTransformer(JTracertByteCodeTransformer JTracertByteCodeTransformer) {
        this.JTracertByteCodeTransformer = JTracertByteCodeTransformer;
    }

    public JTracertByteCodeTransformerAdapter(JTracertByteCodeTransformer JTracertByteCodeTransformer) {
        super(JTracertByteCodeTransformer);
        assert null != JTracertByteCodeTransformer;
        setJTracertByteCodeTransformer(JTracertByteCodeTransformer);
    }

    public InstrumentationProperties getInstrumentationProperties() {
        assert null != getJTracertByteCodeTransformer();
        return getJTracertByteCodeTransformer().getInstrumentationProperties();
    }

    public void setInstrumentationProperties(InstrumentationProperties istrumentationProperties) {
        assert null != getJTracertByteCodeTransformer();
        getJTracertByteCodeTransformer().setInstrumentationProperties(istrumentationProperties);
    }

}
