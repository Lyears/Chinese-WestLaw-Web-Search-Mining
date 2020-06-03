package wsm.preprocess;

import wsm.models.CourtInfo;

import java.util.HashMap;
import java.util.List;

public class IndexNoWordCut {

    private HashMap<String, List<Integer>> inverseIndex;

    public IndexNoWordCut() {
        this.inverseIndex = new HashMap<String, List<Integer>>();
    }

    public void updateIndexFromCourtInfo(CourtInfo courtInfo){

    }

}
