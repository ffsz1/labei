package com.xchat.oauth2.service.core.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * concurrent hashset
 *
 * @author hecong
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E> {

    private ConcurrentHashMap<E, Boolean> map;

    public ConcurrentHashSet() {
        this.map = new ConcurrentHashMap<E, Boolean>();
    }

    public ConcurrentHashSet(Collection<E> c) {
        this.map = new ConcurrentHashMap<E, Boolean>();
        addAll(c);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean add(E o) {
        return map.put(o, Boolean.TRUE) == null;
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return map.keySet().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return map.keySet().retainAll(c);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean equals(Object o) {
        return ((o == this) || (map.keySet().equals(o)));
    }

    @Override
    public int hashCode() {
        return map.keySet().hashCode();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

}
