package cn.maoyanluo.anywhere.door.entity;

import cn.maoyanluo.anywhere.door.config.params.ParameterName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "t_plugin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plugin implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ParameterName(name = "user_id")
    @JsonProperty("user_id")
    @Column(name = "user_id", columnDefinition = "int")
    private Integer userId;

    @ParameterName(name = "plugin_name")
    @JsonProperty("plugin_name")
    @Column(name = "plugin_name", columnDefinition = "varchar(255)")
    private String pluginName;

    @ParameterName(name = "plugin_describe")
    @JsonProperty("plugin_describe")
    @Column(name = "plugin_describe", columnDefinition = "varchar(255)")
    private String pluginDescribe;

    @ParameterName(name = "plugin_host")
    @JsonProperty("plugin_host")
    @Column(name = "plugin_host", columnDefinition = "varchar(255)")
    private String pluginHost;

    @ParameterName(name = "plugin_port")
    @JsonProperty("plugin_port")
    @Column(name = "plugin_port", columnDefinition = "int")
    private Integer pluginPort;

    @ParameterName(name = "plugin_prefix")
    @JsonProperty("plugin_prefix")
    @Column(name = "plugin_prefix", columnDefinition = "varchar(255)")
    private String pluginPrefix;

    @ParameterName(name = "plugin_token")
    @JsonProperty("plugin_token")
    @Column(name = "plugin_token", columnDefinition = "varchar(255)")
    private String pluginToken;

    @ParameterName(name = "is_active")
    @JsonProperty("is_active")
    @Column(name = "is_active", columnDefinition = "int")
    private Integer isActive;

}
