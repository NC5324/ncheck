package com.nc.ncheck.payload.requests;

import java.util.HashSet;
import java.util.Set;

public class ItemDeleteRequest {
    Long userId;
    Set<Long> deletedItems = new HashSet<>();

    public ItemDeleteRequest() {}

    public ItemDeleteRequest(Long userId, Set<Long> deletedItems) {
        this.userId = userId;
        this.deletedItems = deletedItems;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getDeletedItems() {
        return deletedItems;
    }

    public void setDeletedItems(Set<Long> deletedItems) {
        this.deletedItems = deletedItems;
    }
}
