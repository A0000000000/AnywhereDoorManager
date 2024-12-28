package cn.maoyanluo.anywhere.door.entity;

import cn.maoyanluo.anywhere.door.config.params.ParameterName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "t_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config implements Serializable {

    public static final int TYPE_PLUGIN = 0;
    public static final int TYPE_IMSDK = 1;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ParameterName(name = "user_id")
    @JsonProperty("user_id")
    @Column(name = "user_id", columnDefinition = "int")
    private Integer userId;

    @Column(name = "type", columnDefinition = "int")
    private Integer type;

    @ParameterName(name = "target_id")
    @JsonProperty("target_id")
    @Column(name = "target_id", columnDefinition = "int")
    private Integer targetId;

    @ParameterName(name = "config_key")
    @JsonProperty("config_key")
    @Column(name = "config_key", columnDefinition = "varchar(255)")
    private String configKey;

    @ParameterName(name = "config_value")
    @JsonProperty("config_value")
    @Column(name = "config_value", columnDefinition = "text")
    private String configValue;

}
