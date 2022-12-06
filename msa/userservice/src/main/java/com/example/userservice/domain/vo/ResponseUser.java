package com.example.userservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
// 클라이언트에게 json 데이터로 보낼 때 필드 값이 null 인 값을 null 로 보내는게 아니라 아예 보내지 않음
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {

    private String email;

    private String name;

    private String username;

    private List<ResponseOrder> orders;
}
