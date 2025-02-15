package cn.maoyanluo.anywhere.door.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Beans {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

}
