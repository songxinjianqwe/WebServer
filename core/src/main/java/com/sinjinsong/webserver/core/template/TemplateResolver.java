package com.sinjinsong.webserver.core.template;

import com.sinjinsong.webserver.core.enumeration.ModelScope;
import com.sinjinsong.webserver.core.exception.TemplateResolveException;
import com.sinjinsong.webserver.core.request.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sinjinsong
 * 简化版的模板引擎，基于正则表达式的替换
 * 比如${a.b.c} 就可以解析为a.getB().getC()，并将值填充至占位符
 */
@Slf4j
public class TemplateResolver {
    public static final Pattern regex = Pattern.compile("\\$\\{(.*?)}");

    public static String resolve(String content, Request request) throws TemplateResolveException {
        Matcher matcher = regex.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            log.info("{}", matcher.group(1));
            // placeHolder 格式为scope.x.y.z
            // scope值为requestScope,sessionScope,applicationScope
            String placeHolder = matcher.group(1);
            if (placeHolder.indexOf('.') == -1) {
                throw new TemplateResolveException();
            }
            ModelScope scope = ModelScope
                    .valueOf(
                            placeHolder.substring(0, placeHolder.indexOf('.'))
                                    .replace("Scope", "")
                                    .toUpperCase());
            // key 格式为x.y.z
            String key = placeHolder.substring(placeHolder.indexOf('.') + 1);
            if (scope == null) {
                throw new TemplateResolveException();
            }
            Object value = null;
            // 按照.分隔为数组,格式为[x,y,z]
            String[] segments = key.split("\\.");
            log.info("key: {} , segments:{}", key,Arrays.toString(segments));
            switch (scope) {
                case REQUEST:
                    value = request.getAttribute(segments[0]);
                    break;
                case SESSION:
                    value = request.getSession().getAttribute(segments[0]);
                    break;
                case APPLICATION:
                    value = request.getServletContext().getAttribute(segments[0]);
                    break;
                default:
                    break;
            }
            // 此时value为x，如果没有y、z，那么会直接返回；如果有，就会递归地进行属性读取（基于反射）
            if (segments.length > 1) {
                try {
                    value = parse(value, segments, 1);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new TemplateResolveException();
                }
            }
            log.info("value:{}", value);
            // 如果解析得到的值为null，则将占位符去掉；否则将占位符替换为值
            if (value == null) {
                matcher.appendReplacement(sb, "");
            } else {
                //把group(1)得到的数据，替换为value
                matcher.appendReplacement(sb, value.toString());
            }
        }
        // 将源文件后续部分添加至尾部
        matcher.appendTail(sb);
        String result = sb.toString();
        return result.length() == 0 ? content : result;
    }

    /**
     * 基于反射实现多级查询，比如user.dept.name
     *
     * @param segments
     * @return
     */
    private static Object parse(Object value, String[] segments, int index) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (index == segments.length) {
            return value;
        }
        Method method = value.getClass().getMethod("get" + StringUtils.capitalize(segments[index]), new Class[0]);
        return parse(method.invoke(value, new Object[0]), segments, index + 1);
    }
}
