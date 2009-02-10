package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.model.JTracertObjectCompanion;

import java.util.Map;
import java.util.HashMap;

/**
 * @author dmitry.bedrin
 */
public class HashCodeBuilderMethodCallGraphVisitor implements MethodCallVisitor<Integer> {

    private Map<Integer,Integer> hashCodeInstanceCountMap = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> classesInstancesCountMap = new HashMap<Integer,Integer>();

    public Integer visit(MethodCall methodCall) {

        int hashCode = 17;

        int classNameHashCode = methodCall.getRealClassName().hashCode();
        hashCode += 37 * classNameHashCode;

        if (null != methodCall.getMethodName()) {
            hashCode += 37 * methodCall.getMethodName().hashCode();
        }
        if (null != methodCall.getMethodSignature()) {
            hashCode += 37 * methodCall.getMethodSignature().hashCode();
        }
        //hashCode += 37 * methodCall.getObjectHashCode();

        /*JTracertObjectCompanion jTracertObjectCompanion = methodCall.getjTracertObjectCompanion();
        if (null != jTracertObjectCompanion) {

            int relativeInstanceNumber;

            int jTracertObjectCompanionHash = jTracertObjectCompanion.hashCode();
            int jTracertObjectCompanionFullHash = 37 * 37 * 17 + 37 * classNameHashCode + jTracertObjectCompanionHash;

            if (!hashCodeInstanceCountMap.containsKey(jTracertObjectCompanionFullHash)) {
                int classInstancesCount;
                if (classesInstancesCountMap.containsKey(classNameHashCode)) {
                    classInstancesCount = classesInstancesCountMap.get(classNameHashCode) + 1;
                } else {
                    classInstancesCount = 1;
                }
                hashCodeInstanceCountMap.put(jTracertObjectCompanionFullHash,1);
            }

            relativeInstanceNumber = hashCodeInstanceCountMap.get(jTracertObjectCompanionFullHash);

            hashCode += 37 * relativeInstanceNumber;

        }*/

        for (MethodCall callee : methodCall.getCallees()) {
            hashCode += 37 * callee.accept(this);
        }

        return hashCode;
        
    }
}
