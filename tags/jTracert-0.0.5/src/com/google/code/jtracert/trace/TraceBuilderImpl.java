package com.google.code.jtracert.trace;



public class TraceBuilderImpl implements TraceBuilder {

    private final static ThreadLocal<TraceBuilderState> traceBuilderStateThreadLocal =
            new ThreadLocal<TraceBuilderState>();

    private TraceBuilderState getTraceBuilderState() {
        return traceBuilderStateThreadLocal.get();
    }

    private void setTraceBuilderState(TraceBuilderState state) {
        traceBuilderStateThreadLocal.set(state);
    }

    public void onMethodEnter(Class clazz, Object object, String methodName, String methodDescriptor) {

        assert null != clazz;
        assert null != object;
        assert null != methodName;
        assert !"".equals(methodName);
        assert null != methodDescriptor;
        assert !"".equals(methodDescriptor);

        Method method = new Method(clazz, object, methodDescriptor, methodName);

        onMethodEnter(method);

    }

    public void onMethodEnter(Class clazz, Object object, String methodName, String methodDescriptor, Object[] methodArguments) {

        assert null != clazz;
        assert null != object;
        assert null != methodName;
        assert !"".equals(methodName);
        assert null != methodDescriptor;
        assert !"".equals(methodDescriptor);

        Method method = new Method(clazz, object, methodDescriptor, methodArguments, methodName);

        onMethodEnter(method);

    }

    private void onMethodEnter(Method method) {

        assert null != method;

        TraceBuilderState state = getTraceBuilderState();

        if (null == state) {
            state = new TraceBuilderState(method);
            setTraceBuilderState(state);
            onTraceStart(method);
        } else {

            TraceElement contextTraceElement = state.getContextTraceElement();

            assert null != contextTraceElement;

            contextTraceElement.addTraceElement(method);

            state.setContextTraceElement(method);

        }

        state.enterCount++;

    }

    public void onMethodReturn() {

        TraceBuilderState state = getTraceBuilderState();

        assert null != state;

        TraceElement contextTraceElement = state.getContextTraceElement();

        assert null != contextTraceElement;

        if (null == contextTraceElement.getParentTraceElement()) {
            setTraceBuilderState(null);
            onTraceEnd(contextTraceElement);
        } else {
            state.setContextTraceElement(contextTraceElement.getParentTraceElement());
        }

        state.leaveCount++;
        assert state.enterCount == state.leaveCount;

    }

    public void onMethodReturnValue(Object returnValue) {

        TraceBuilderState state = getTraceBuilderState();

        assert null != state;

        TraceElement contextTraceElement = state.getContextTraceElement();

        assert null != contextTraceElement;
        assert contextTraceElement instanceof Method;

        //Method method = (Method)contextTraceElement;
        //method.setReturnValue(returnValue);

        onMethodReturn();

    }

    public void onMethodException(Throwable exception) {

        TraceBuilderState state = getTraceBuilderState();

        assert null != state;

        TraceElement contextTraceElement = state.getContextTraceElement();

        assert null != contextTraceElement;
        assert contextTraceElement instanceof Method;

        //Method method = (Method)contextTraceElement;
        //method.setException(exception);

        onMethodReturn();
    }

    public void onConstructorEnter(Class clazz, Object object, String constructorDescriptor) {

        assert null != clazz;
        assert null != object;
        assert null != constructorDescriptor;
        assert !"".equals(constructorDescriptor);

        Constructor constructor = new Constructor(clazz, object, constructorDescriptor);

        onConstructorEnter(constructor);

    }

    public void onConstructorEnter(Class clazz, Object object, String constructorDescriptor, Object[] arguments) {

        assert null != clazz;
        assert null != object;
        assert null != constructorDescriptor;
        assert !"".equals(constructorDescriptor);

        Constructor constructor = new Constructor(clazz, object, constructorDescriptor, arguments);

        onConstructorEnter(constructor);

    }

    private void onConstructorEnter(Constructor constructor) {

        assert null != constructor;

        TraceBuilderState state = getTraceBuilderState();

        if (null == state) {
            state = new TraceBuilderState(constructor);
            setTraceBuilderState(state);
            onTraceStart(constructor);
        } else {
            TraceElement contextTraceElement = state.getContextTraceElement();

            assert null != contextTraceElement;

            TraceElement previousSiblingTraceElement = contextTraceElement.
                    getChildTraceElements().
                    get(contextTraceElement.getChildTraceElements().size() - 1);

            contextTraceElement.addTraceElement(constructor);
            state.setContextTraceElement(constructor);

            if ((null != previousSiblingTraceElement) &&
                    (previousSiblingTraceElement instanceof Constructor) &&
                    (previousSiblingTraceElement.getObject() == constructor.getObject())
                    ) {

                contextTraceElement.getChildTraceElements().remove(previousSiblingTraceElement);
                constructor.addTraceElement(previousSiblingTraceElement);

            }

        }

        state.enterCount++;

    }

    public void onConstructorReturn() {

        TraceBuilderState state = getTraceBuilderState();

        assert null != state;

        TraceElement contextTraceElement = state.getContextTraceElement();

        assert null != contextTraceElement;

        if (null == contextTraceElement.getParentTraceElement()) {
            setTraceBuilderState(null);
            onTraceEnd(contextTraceElement);
        } else {
            state.setContextTraceElement(contextTraceElement.getParentTraceElement());
        }

        state.leaveCount++;
        assert state.enterCount == state.leaveCount;

    }

    public void onConstructorException(Class clazz, String constructorDescriptor, Throwable exception) {

        TraceBuilderState state = getTraceBuilderState();

        if (null == state) {

            Constructor constructor = new Constructor(clazz, null, constructorDescriptor);

            state = new TraceBuilderState(constructor);
            setTraceBuilderState(state);

            state.enterCount++;
            onTraceStart(constructor);

            state.leaveCount++;
            onTraceEnd(constructor);

        } else {

            if (state.enterCount == state.leaveCount) {
                // constructor body wasn't executed;  Constructor instance wasn't created

                // note that this requires constructors of ALL classes to be instrumented

                TraceElement contextTraceElement = state.getContextTraceElement();

                assert null != contextTraceElement;

                TraceElement previousSiblingTraceElement = contextTraceElement.
                        getChildTraceElements().
                        get(contextTraceElement.getChildTraceElements().size() - 1);

                assert null != previousSiblingTraceElement;
                assert previousSiblingTraceElement instanceof Constructor;

                Constructor constructor =
                        new Constructor(clazz, previousSiblingTraceElement.getObject(), constructorDescriptor);

                onConstructorEnter(constructor);
                onConstructorReturn();

            } else {
                // constructor body was executed; Constructor instance was created
                onConstructorReturn();
            }

        }

        assert state.enterCount == state.leaveCount;

    }

    public void onTraceStart(TraceElement traceElement) {

        assert null != traceElement;

    }

    public void onTraceEnd(TraceElement traceElement) {

        assert null != traceElement;

    }

}
