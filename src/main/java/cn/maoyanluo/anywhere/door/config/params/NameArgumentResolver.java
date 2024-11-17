package cn.maoyanluo.anywhere.door.config.params;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class NameArgumentResolver extends ServletModelAttributeMethodProcessor {

    public NameArgumentResolver() {
        super(true);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return true;
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        Map<String, String[]> map = new HashMap<>();
        Map<String, String[]> params = request.getParameterMap();
        Object target = binder.getTarget();
        if (target != null) {
            Class<?> targetClass = target.getClass();
            Field[] fields = targetClass.getFields();
            for (Field field : fields) {
                ParameterName annotation = field.getAnnotation(ParameterName.class);
                if (annotation != null) {
                    String name = annotation.name();
                    if (!StringUtils.isEmpty(name)) {
                        map.put(field.getName(), params.get(name));
                    }
                }
            }
            fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                ParameterName annotation = field.getAnnotation(ParameterName.class);
                if (annotation != null) {
                    String name = annotation.name();
                    if (!StringUtils.isEmpty(name)) {
                        map.put(field.getName(), params.get(name));
                    }
                }
            }
        }
        PropertyValues propertyValues = new MutablePropertyValues(map);
        binder.bind(propertyValues);
    }

}