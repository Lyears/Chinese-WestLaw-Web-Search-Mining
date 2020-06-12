package wsm.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import wsm.engine.InstrumentConstruction;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.utils.TfIdfUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private final int DEFAULT_SORT = 0;
    private final int AGE_SORT = 1;
    private final int DATE_SORT = 2;
    private final int NAME_SORT = 3;

    private final String wsmRootDir = System.getenv("WSM_ROOT_DIR");

    private TfIdfUtil tfIdfUtil = TfIdfUtil.recoverFromDisk(wsmRootDir);

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam("searchType") String searchType,
                             @RequestParam("sortType") Integer sortType,
                             @RequestParam("searchValue") String searchValue) {
        ModelAndView mav = new ModelAndView("list");
        if (searchType.equals("query")) {
            List<Integer> docNumList = new ArrayList<>();
            List<Integer> docIdOffset = IndexConsts.docIdOffsetList;
            IndexIdToDoc indexIdToDoc = InstrumentConstruction.constructInstrumentMapFromDisk(
                    wsmRootDir, docNumList);
            List<Integer> queryRes = tfIdfUtil.getRelevantTopKIndices(searchValue, 20);

        }
        return mav;
    }
}
