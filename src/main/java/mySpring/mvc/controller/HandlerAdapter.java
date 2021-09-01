package mySpring.mvc.controller;

import mySpring.mvc.model.FileModel;
import mySpring.mvc.model.Model;
import mySpring.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 将控制类及其方法映射并填入参数，并返回ModelAndView给视图渲染器
 */
public class HandlerAdapter {
    private Map<String, Integer> paramType;
    final static String REDIRECT = "redirect:";
    final static String FORWARD = "forward:";
    public HandlerAdapter(Map<String, Integer> paramType) {
        this.paramType = paramType;
    }


    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {

        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();

        Object[] paramValues = new Object[parameterTypes.length];

        Model model = null;

        ModelAndView modelAndView = new ModelAndView(); //实例化方法执行完返回的ModelAndView

        if (paramType.containsKey(HttpServletRequest.class.getName())) {

            paramValues[paramType.get(HttpServletRequest.class.getName())] = req;

        }
        if (paramType.containsKey(HttpServletResponse.class.getName())) {

            paramValues[paramType.get(HttpServletResponse.class.getName())] = resp;

        }

        if (paramType.containsKey(Model.class.getName())) {
            model = new Model();
            paramValues[paramType.get(Model.class.getName())] = model;
        }

        for (Map.Entry<String, Integer> entry : paramType.entrySet()) {
            String paramName = entry.getKey();
            Integer index = entry.getValue();

            /*如果是file注解，则从request中提取出文件数据，并封装成fileModel供控制器使用*/
            if (paramName.equals("file")){
                FileModel fileModel = new FileModel(req);
                if (fileModel.getFileStatus()){
                    paramValues[index] = fileModel ;
                }
            }else {
                /*对参数进行装填*/
                String[] values = req.getParameterValues(paramName);
                if (values != null && values.length != 0) {
                    String value = Arrays.toString(values).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    System.out.println("---------------->>>"+paramName+":"+value);
                    paramValues[index] = castValueType(value, parameterTypes[index]);
                }
            }

        }

        if (handler.getMethod().getReturnType() == String.class) {
            /*根据返回值来设置返回的modelAndView*/
            String data = (String) handler.getMethod().invoke(handler.getController(), paramValues);
            if (data.startsWith(REDIRECT)) {
                modelAndView.setType("redirect");
                modelAndView.setData(data.substring(REDIRECT.length()));

            } else if (data.startsWith(FORWARD)){
                modelAndView.setType("forward");
                modelAndView.setData(data.substring(FORWARD.length()));
            }else {
                modelAndView.setType("string");
                modelAndView.setData(data);
            }
        }
        else {
            handler.getMethod().invoke(handler.getController(), paramValues);
            modelAndView.setType("none"); //不需要进行处理了
        }

        //System.out.println("---------------->>>方法执行完毕");

        /*提取model的数据。并将其注入到request对象中，供jsp获取*/
        if (model!=null){
            for (Map.Entry<String, Object> entry : model.getMap().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null ) {
                    req.setAttribute(key,value);
                }
            }
        }

        //System.out.println("---------------->>>数据加载完毕");
        return modelAndView;
    }



    private Object castValueType(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value).intValue();
        } else {
            return null;
        }
    }

}