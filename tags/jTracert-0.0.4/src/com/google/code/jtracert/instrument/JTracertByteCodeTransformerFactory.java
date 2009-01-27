package com.google.code.jtracert.instrument;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.impl.asm.JTracertASMByteCodeTransformer;

/**
 * @author Dmitry Bedrin
 */
public class JTracertByteCodeTransformerFactory {

    public static JTracertByteCodeTransformer getJTracertByteCodeTransformer() {
        return new JTracertASMByteCodeTransformer();
    }

    public static JTracertByteCodeTransformer getJTracertByteCodeTransformer(InstrumentationProperties instrumentationProperties) {
        final JTracertByteCodeTransformer JTracertByteCodeTransformer =
                getJTracertByteCodeTransformer();
        JTracertByteCodeTransformer.setInstrumentationProperties(instrumentationProperties);
        return JTracertByteCodeTransformer;
    }

}
