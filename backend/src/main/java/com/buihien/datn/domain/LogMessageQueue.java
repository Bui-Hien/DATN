package com.buihien.datn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "tbl_log_message_queue")
@Entity
public class LogMessageQueue extends AuditableEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "message")
    private String message; //Nội dung của message

    @Column(name = "action")
    private String action; //Hành động tương ứng với message

    @Column(name = "status")
    private Integer status; //Trạng thái action đó là thành công hay thất bại định ở DatnConstants.LogMessageQueueStatus

    @Column(name = "type")
    private Integer type; //Định nghĩa loại log để sau paging(ví dụ type = 1 là log của giửi mail quên mật khẩu) định ở DatnConstants.LogMessageQueueTypes

    public LogMessageQueue() {
    }

    public LogMessageQueue(String message, String action, Integer status, Integer type) {
        this.message = message;
        this.action = action;
        this.status = status;
        this.type = type;
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
