package com.chaoshan.entity.dto;

import lombok.Data;

/**
 * @DATE: 2022/05/26 17:31
 * @Author: 小爽帅到拖网速
 */
@Data
public class UserPasswordDTO {
    private String rawPassword;
    private String newPassword;
}
