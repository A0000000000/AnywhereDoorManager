package cn.maoyanluo.anywhere.door.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String token;
    @JsonProperty("flush_token")
    private String flushToken;

}
