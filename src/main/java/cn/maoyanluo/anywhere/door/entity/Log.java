package cn.maoyanluo.anywhere.door.entity;

import cn.maoyanluo.anywhere.door.config.params.ParameterName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "t_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log implements Serializable {

    public static final Integer TYPE_MANAGER = 1;
    public static final Integer TYPE_CONTROL_PLANE = 2;
    public static final Integer TYPE_PLUGIN = 3;
    public static final Integer TYPE_IMSDK = 4;

    public static final Integer LEVEL_DEBUG = 1;
    public static final Integer LEVEL_INFO = 2;
    public static final Integer LEVEL_WARN = 3;
    public static final Integer LEVEL_ERROR = 4;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ParameterName(name = "user_id")
    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "type")
    private Integer type;

    @ParameterName(name = "target_id")
    @JsonProperty("target_id")
    @Column(name = "target_id")
    private Integer targetId;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "level")
    private Integer level;

    @ParameterName(name = "tag")
    @JsonProperty("tag")
    @Column(name = "tag", columnDefinition = "varchar(255)")
    private String tag;

    @Column(name = "log", columnDefinition = "text")
    private String log;

}
