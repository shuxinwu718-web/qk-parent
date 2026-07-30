package com.qk.exception;

public enum ErrorCode {
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "无权访问"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "资源冲突"),
    INTERNAL_ERROR(500, "服务器内部错误");

    private final int httpStatus;
    private final String defaultMessage;

    ErrorCode(int httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    // 👇 新增：根据 httpStatus 查找对应的 ErrorCode
    public static ErrorCode fromHttpStatus(int httpStatus) {
        for (ErrorCode code : values()) {
            if (code.httpStatus == httpStatus) {
                return code;
            }
        }
        // 如果找不到匹配的，默认返回 INTERNAL_ERROR
        return INTERNAL_ERROR;
    }
}