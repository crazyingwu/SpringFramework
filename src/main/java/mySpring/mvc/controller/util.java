package mySpring.mvc.controller;

import mySpring.annotation.Controller;
import mySpring.annotation.RequestMapping;
import mySpring.annotation.RequestParam;
import mySpring.mvc.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class util {
    /**
     * 初始化 HandlerMapping
     */
    public static void initHandlerMapping(Map<String,Object> iocMap, List<Handler> handlerMapping) {

        if (iocMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String baseUrl = "";

            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping xRequestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = xRequestMapping.value();
            }

            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                String subUrl = method.getAnnotation(RequestMapping.class).value();
                String regex = (baseUrl + subUrl).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);

                handlerMapping.add(new Handler(entry.getValue(), method, pattern));

                System.out.println("[INFO-5] handlerMapping put {" + baseUrl+subUrl + "} - {" + method + "}.");

            }
        }

    }

    /*初始化HandlerAdapters，主要完成参数类型和index的匹配，后面装填参数*/
    public static void initHandlerAdapters(List<Handler> handlerMapping ,Map<Handler, HandlerAdapter> adapterMapping ) {
        if (handlerMapping.isEmpty()) return;

        for (Handler handler : handlerMapping) {
            Method method = handler.getMethod();
            Map<String, Integer> paramType = new HashMap<String, Integer>();
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class || type == Model.class ) {
                    paramType.put(type.getName(),i);
                }
            }

            Annotation[][] pas = method.getParameterAnnotations();
            for (int i = 0; i < pas.length; i++) {
                Annotation[] pa = pas[i];
                for (Annotation a:pa){
                    if(a instanceof RequestParam){
                        String paramName = ((RequestParam) a).value();
                        if(!"".equals(paramName)){
                            paramType.put(paramName,i);
                        }
                    }
                }
            }
            adapterMapping.put(handler,new HandlerAdapter(paramType));
        }
    }


    /*根据request获取Handler*/
    public static Handler getHandler(List<Handler> handlerMapping, HttpServletRequest req){
        if(handlerMapping.isEmpty())return null;
        String contextPath = req.getContextPath();
        String url = req.getRequestURI();
        url = url.replace(contextPath,"").replaceAll("/+","/");
        for (Handler handler:handlerMapping){
            if(handler.getPattern().matcher(url).matches()){
                return handler;
            }
        }
        return null;
    }

    /*获取HandlerAdapter*/
    public static HandlerAdapter getHandlerAdapter(Map<Handler, HandlerAdapter> adapterMapping, Handler handler) {
        if(adapterMapping.isEmpty())return null;
        return adapterMapping.get(handler);
    }


}
