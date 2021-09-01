package mySpring.mvc.view;

/*
* 控制器执行完毕后返回，供视图解析器使用
* */
public class ModelAndView {
    public ModelAndView(){

    }
    private String type;
    private Object data;

    public String getType(){
        return type;
    }
    public Object getData(){
        return data;
    }

    public void setType(String type){
        this.type = type;
    }
    public void setData(Object data){
        this.data = data;
    }


}
