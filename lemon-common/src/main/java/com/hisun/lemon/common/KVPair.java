package com.hisun.lemon.common;

public class KVPair<K, V> {
    private K k;
    private V v;
    
    public KVPair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
    
    public static <K, V> KVPair<K, V> instance(K k, V v) {
        return new KVPair<K, V>(k, v);
    }
    
}
