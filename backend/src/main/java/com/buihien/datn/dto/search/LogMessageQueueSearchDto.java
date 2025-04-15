package com.buihien.datn.dto.search;

public class LogMessageQueueSearchDto extends SearchDto {
    private Integer status; //Trạng thái action đó là thành công hay thất bại định ở DatnConstants.LogMessageQueueStatus
    private Integer type; //Định nghĩa loại log để sau paging(ví dụ type = 1 là log của giửi mail quên mật khẩu) định ở DatnConstants.LogMessageQueueTypes

    public LogMessageQueueSearchDto() {
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
