package wsm.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import wsm.engine.InstrumentConstruction;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.server.repository.InstrumentRepository;
import wsm.utils.TfIdfUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private final int DEFAULT_SORT = 0;
    private final int AGE_SORT = 1;
    private final int DATE_SORT = 2;
    private final int NAME_SORT = 3;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam("searchType") String searchType,
                             @RequestParam(name = "sortType",required = false) Integer sortType,
                             @RequestParam("searchValue") String searchValue) {
        ModelAndView mav = new ModelAndView("list");
        if (searchType.equals("query")) {
//            System.out.println(instrumentRepository.queryInstrument(searchValue).toString());
            mav.addObject("instruments", instrumentRepository.queryInstrument(searchValue));
        }
        return mav;
    }
}
