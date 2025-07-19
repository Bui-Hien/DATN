package com.buihien.datn.util;

import com.buihien.datn.domain.Staff;
import com.buihien.datn.generic.GenericCache;

import java.util.UUID;

public class CacheUtils {
    public static final GenericCache<UUID, Staff> HashMapStaff = new GenericCache<>();
}
