package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;

/**
 * @author dmitry.bedrin
 */
public interface SDEditClient {

    void processMethodCall(MethodCall methodCall);

}
