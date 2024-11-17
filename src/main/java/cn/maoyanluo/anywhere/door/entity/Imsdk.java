package cn.maoyanluo.anywhere.door.entity;

import cn.maoyanluo.anywhere.door.config.params.ParameterName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "t_imsdk")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Imsdk implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ParameterName(name = "user_id")
    @JsonProperty("user_id")
    @Column(name = "user_id", columnDefinition = "int")
    private Integer userId;

    @ParameterName(name = "imsdk_name")
    @JsonProperty("imsdk_name")
    @Column(name = "imsdk_name", columnDefinition = "varchar(255)")
    private String imsdkName;

    @ParameterName(name = "imsdk_describe")
    @JsonProperty("imsdk_describe")
    @Column(name = "imsdk_describe", columnDefinition = "varchar(255)")
    private String imsdkDescribe;

    @ParameterName(name = "imsdk_host")
    @JsonProperty("imsdk_host")
    @Column(name = "imsdk_host", columnDefinition = "varchar(255)")
    private String imsdkHost;

    @ParameterName(name = "imsdk_port")
    @JsonProperty("imsdk_port")
    @Column(name = "imsdk_port", columnDefinition = "int")
    private Integer imsdkPort;

    @ParameterName(name = "imsdk_prefix")
    @JsonProperty("imsdk_prefix")
    @Column(name = "imsdk_prefix", columnDefinition = "varchar(255)")
    private String imsdkPrefix;

    @ParameterName(name = "imsdk_token")
    @JsonProperty("imsdk_token")
    @Column(name = "imsdk_token", columnDefinition = "varchar(255)")
    private String imsdkToken;

    @ParameterName(name = "is_active")
    @JsonProperty("is_active")
    @Column(name = "is_active", columnDefinition = "int")
    private Integer isActive;

}
