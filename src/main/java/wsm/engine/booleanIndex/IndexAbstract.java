package wsm.engine.booleanIndex;

import wsm.models.CourtInfo;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class IndexAbstract {

    /**
     * update the index from courtInfo
     * @param docId the docID list
     * @param courtInfo the courtInfo object
     */
    public abstract void updateFromCourtInfo(List<Integer> docId, List<CourtInfo> courtInfo);

    /**
     * process a request query string with index
     * return a posting List from a query string
     * @param queryString the String for querying
     * @return the posting list
     */
    public abstract TreeSet<Integer> queryFromRequestString(String queryString);

    /**
     * store the index into an index file
     * @param fileRootPath the index file path
     */
    public abstract void storeIndexToDisk(String fileRootPath);

}
