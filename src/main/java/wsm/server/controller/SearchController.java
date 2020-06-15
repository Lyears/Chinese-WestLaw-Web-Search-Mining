package wsm.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import wsm.models.CourtInfo;
import wsm.server.repository.IndexRepository;
import wsm.server.repository.InstrumentRepository;
import wsm.utils.Page;

import java.util.List;

@Controller
public class SearchController {

    private final int DEFAULT_SORT = 0;
    private final int AGE_SORT = 1;
    private final int DATE_SORT = 2;
    private final int NAME_SORT = 3;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private IndexRepository indexRepository;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/instruments")
    public ModelAndView instruments(@RequestParam("searchType") String searchType,
                                    @RequestParam(name = "sortType", required = false) Integer sortType,
                                    @RequestParam("searchValue") String searchValue) {
        ModelAndView mav = new ModelAndView("instruments");
        if (searchType.equals("query")) {
//            System.out.println(instrumentRepository.queryInstrument(searchValue).toString());
            mav.addObject("instruments", instrumentRepository.queryInstrument(searchValue));
        }
        return mav;
    }

    @RequestMapping("/list")
    public ModelAndView booleanQuery(@RequestParam("searchType") String searchType,
                                     @RequestParam(name = "sortType", required = false) Integer sortType,
                                     @RequestParam("searchValue") String searchValue,
                                     @RequestParam("page") int pageNum) {
        ModelAndView mav = new ModelAndView("list");
        System.out.println(searchValue);
        if (searchType.equals("boolean")) {
            List<CourtInfo> result = indexRepository.indexQuery(searchValue);
            Page<CourtInfo> page = new Page<>(pageNum, 20, result.size(), result);
            mav.addObject("page", page);
        }
        return mav;
    }
}
