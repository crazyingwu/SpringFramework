package mySpring.mvc.controller;

import mySpring.ioc.MyContext;
import mySpring.mvc.view.ModelAndView;
import mySpring.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;


public class MyDispatchServlet extends HttpServlet {
    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";
    private MyContext context;
    private ViewResolver viewResolver;
    private Properties config = new Properties();
    @Override
    public void init() throws ServletException {
        //获取配置的参数，指令扫描的包
        String location = getInitParameter(CONTEXT_CONFIG_LOCATION);
//        String classpath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/")).getPath();
        String classpath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String end = "WEB-INF/lib/MySpringMvc-jar-with-dependencies.jar";
        try {
            String filePath = classpath.replace(end, location);
            //System.out.println("新方法获取的路径: "+this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            System.out.println("文件路径-filepath："+filePath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            config.load(bufferedReader);
            /*获取需要扫描的包*/
            context = new MyContext(config.getProperty("scanPackage"));
            /*初始化handlerMapping*/
            util.initHandlerMapping(context.ioc,context.handleMap);
            /*初始化handlerAdapter*/
            util.initHandlerAdapters(context.handleMap,context.adapterMapping);
            /*实例化viewResolver，后面的视图渲染都是用它*/
            viewResolver = new ViewResolver();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        /*使用req去匹配对应的handler*/
        Handler handler = util.getHandler(context.handleMap, req);
        /*System.out.println("geturl："+ req.getRequestURI());*/
        if (handler ==null){
            resp.getWriter().write("404 NOT Found!");
            return;
        }
        /*根据handler获取handlerAdapter，对请求进行处理*/
        HandlerAdapter ha = util.getHandlerAdapter(context.adapterMapping, handler);
        ModelAndView modelAndView = ha.handle(req,resp,handler);
        /*使用handlerAdapter返回的ModelAndView来渲染视图，响应客户端*/
        System.out.println("-------------->>开始渲染视图"+modelAndView.getData());
        viewResolver.render(req,resp,modelAndView);
    }
}
