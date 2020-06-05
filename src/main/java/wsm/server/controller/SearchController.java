package wsm.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam("searchType")String searchType,
                             @RequestParam("sortType") String sortType,
                             @RequestParam("searchValue") String searchValue){
        ModelAndView mav = new ModelAndView("list");

        return mav;
    }
}
