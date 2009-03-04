package com.google.code.jtracert.instrument;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.impl.asm.JTracertASMByteCodeTransformer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertByteCodeTransformerFactory {

    /**
     *
     * @return
     */
    public static JTracertByteCodeTransformer getJTracertByteCodeTransformer() {
        return new JTracertASMByteCodeTransformer();
    }

    /**
     *
     * @param instrumentationProperties
     * @return
     */
    public static JTracertByteCodeTransformer getJTracertByteCodeTransformer(InstrumentationProperties instrumentationProperties) {
        final JTracertByteCodeTransformer JTracertByteCodeTransformer =
                getJTracertByteCodeTransformer();
        JTracertByteCodeTransformer.setInstrumentationProperties(instrumentationProperties);
        return JTracertByteCodeTransformer;
    }

}
