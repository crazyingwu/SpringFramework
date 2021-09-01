package mySpring.mvc.controller;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 保存控制类的信息，支持正则匹配url
 */
public class Handler {

    private Object controller;
    private Method method;
    private Pattern pattern;



    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}