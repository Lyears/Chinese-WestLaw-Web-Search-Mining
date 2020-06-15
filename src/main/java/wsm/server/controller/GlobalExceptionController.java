package wsm.server.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import wsm.exception.QueryFormatException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(QueryFormatException.class)
    public ModelAndView ExceptionControl(HttpServletRequest req, QueryFormatException e) {
        String query = req.getParameter("query");
        ModelAndView mav = new ModelAndView("index");
        System.out.println(e.getDescription());
        mav.addObject("exception", e);
        mav.addObject("query", query);
        return mav;
    }
}
