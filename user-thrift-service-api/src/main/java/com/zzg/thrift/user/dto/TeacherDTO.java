package com.zzg.thrift.user.dto;

import lombok.Data;

/**
 * @Author ZZG
 * @Date 2019/10/8 15:54
 * @Version v1.0.0
 */
@Data
public class TeacherDTO extends UserDTO {
    private String intro;
    private int stars;
}
