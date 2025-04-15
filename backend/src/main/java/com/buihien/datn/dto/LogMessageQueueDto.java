package com.buihien.datn.dto;

import com.buihien.datn.domain.LogMessageQueue;

public class LogMessageQueueDto extends AuditableEntityDto {
    private String message; //Nội dung của message
    private String action; //Hành động tương ứng với message
    private Integer status; //Trạng thái action đó là thành công hay thất bại định ở DatnConstants.LogMessageQueueStatus
    private Integer type; //Định nghĩa loại log để sau paging(ví dụ type = 1 là log của giửi mail quên mật khẩu) định ở DatnConstants.LogMessageQueueTypes

    public LogMessageQueueDto() {
    }

    public LogMessageQueueDto(LogMessageQueue entity, Boolean isGetFull) {
        super(entity);
        if (null == entity) {
            return;
        }
        this.message = entity.getMessage();
        this.action = entity.getAction();
        this.status = entity.getStatus();
        this.type = entity.getType();
        if (isGetFull) {
            // Chuyển đổi các thuộc tính khác nếu cần thiết
        }
    }
    public LogMessageQueueDto(LogMessageQueue entity){
        this(entity, true);
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
