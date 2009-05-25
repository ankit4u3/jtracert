package com.google.code.jtracert.samples;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;

class ExpectedException extends RuntimeException {

}

class FailMap implements Map {

    private void fail() {
        throw new ExpectedException();
    }

    public int size() {
        fail();
        return 0;
    }

    public boolean isEmpty() {
        fail();
        return false;
    }

    public boolean containsKey(Object key) {
        fail();
        return false;
    }

    public boolean containsValue(Object value) {
        fail();
        return false;
    }

    public Object get(Object key) {
        fail();
        return null;
    }

    public Object put(Object key, Object value) {
        fail();
        return null;
    }

    public Object remove(Object key) {
        fail();
        return null;
    }

    public void putAll(Map t) {
        fail();

    }

    public void clear() {
        fail();

    }

    public Set keySet() {
        fail();
        return null;
    }

    public Collection values() {
        fail();
        return null;
    }

    public Set entrySet() {
        fail();
        return null;
    }
}

class CustomMap extends HashMap {

    @SuppressWarnings("unchecked")
    public CustomMap(Map m) {
        super(m);
    }

}

public class BootStrapClassConstructor {

    @SuppressWarnings("unchecked")
    public static void main(String[] arguments) throws Exception {
        try {
            Map map = new FailMap();
            Map adapterMap = new CustomMap(map);
        } catch (ExpectedException e) {

        }
    }

}
