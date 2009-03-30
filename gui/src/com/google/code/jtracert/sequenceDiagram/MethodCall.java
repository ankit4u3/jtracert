package com.google.code.jtracert.sequenceDiagram;

import java.util.List;

public interface MethodCall {

    String getResolvedClassName();

    String getResolvedMethodName();

    List<MethodCall> getCallees();

}
