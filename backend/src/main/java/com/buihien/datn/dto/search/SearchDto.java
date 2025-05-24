package com.buihien.datn.dto.search;

import com.buihien.datn.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class SearchDto {
    public UUID id;
    public UUID ownerId;
    public Integer pageIndex;
    public Integer pageSize;
    public String keyword;
    public Date fromDate;
    public Date toDate;
    public Boolean voided;
    public Boolean orderBy; //mặc định là DESC của trường createdAt
    public UUID roleId;
    public UUID parentId;
    public Boolean exportExcel;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        if (this.keyword == null) {
            return "";
        }
        return keyword.strip();
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getFromDate() {
        if (fromDate == null) {
            return null;
        }
        return DateTimeUtil.getStartOfDay(fromDate);
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        if (toDate == null) {
            return null;
        }
        return DateTimeUtil.getEndOfDay(toDate);
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public Boolean getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Boolean orderBy) {
        this.orderBy = orderBy;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Boolean getExportExcel() {
        return exportExcel;
    }

    public void setExportExcel(Boolean exportExcel) {
        this.exportExcel = exportExcel;
    }
}
