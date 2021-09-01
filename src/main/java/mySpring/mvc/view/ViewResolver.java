package mySpring.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
视图解析器，根据ModelAndView解析成具体的view并返回给客户端
* */
public class ViewResolver {

    private final static String REDIRECT = "redirect";
    private final static String STRING = "string";
    private final static String BYTE = "byte";
    private final static String NONE = "none";
    private final static String FORWARD = "forward";

    public ViewResolver(){
    }

    public void render(HttpServletRequest request, HttpServletResponse res, ModelAndView modelAndView) throws Exception {
        request.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
        if (modelAndView.getType().equals(FORWARD)){
            String viewPath = "/WEB-INF/view/"+(String)modelAndView.getData() + ".jsp";
            System.out.println(viewPath);
            request.getRequestDispatcher(viewPath).forward(request, res);
        }else if (modelAndView.getType().equals(REDIRECT)) {
            res.sendRedirect((String)modelAndView.getData());
        }else if (modelAndView.getType().equals(STRING)){
            res.getWriter().write((String) modelAndView.getData());
        }else {
            // Type为none，不需要做处理
        }

    }
}
