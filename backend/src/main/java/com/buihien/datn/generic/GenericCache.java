package com.buihien.datn.generic;

import org.openjdk.jol.info.GraphLayout;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericCache<K, V> {
    private final Map<K, V> cacheMap = new ConcurrentHashMap<>();

    // Lấy dữ liệu theo key (get by key)
    public V get(K key) {
        return cacheMap.get(key);
    }

    // Thêm mới hoặc cập nhật (put: insert or overwrite)
    public void put(K key, V value) {
        cacheMap.put(key, value);
    }

    // Xoá tất cả (clear all cache)
    public void clear() {
        cacheMap.clear();
    }

    // Xoá theo key
    public void remove(K key) {
        cacheMap.remove(key);
    }

    public void removes(List<K> keys) {
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(cacheMap::remove);
        }
    }

    // Kiểm tra tồn tại
    public boolean contains(K key) {
        return cacheMap.containsKey(key);
    }

    // Trả về toàn bộ cache
    public Map<K, V> getAll() {
        return cacheMap;
    }

    // Log dung lượng bộ nhớ (memory usage)
    public Double getMemoryUsage() {
        try {
            GraphLayout layout = GraphLayout.parseInstance(cacheMap);
            long totalBytes = layout.totalSize();
            double mb = totalBytes / (1024.0 * 1024.0);
            return Double.valueOf(String.format("%.4f", mb));
        } catch (Exception e) {
            return 0.0;
        }
    }

}
