package wsm.preprocess;

import wsm.models.CourtInfo;
import java.util.LinkedList;
import java.util.TreeSet;

public abstract class IndexAbstract {

    /**
     * update the index from courtInfo
     * @param courtInfo the courtInfo object
     */
    public abstract void updateFromCourtInfo(CourtInfo courtInfo);

    /**
     * process a request query string with index
     * return a posting List from a query string
     * @param queryString the String for querying
     * @return the posting list
     */
    public abstract TreeSet<Integer> queryFromRequestString(String queryString);


    /**
     * store the index into an index file
     * @param fileName the index file path
     * @return the number of bytes written
     */
    public abstract Integer storeIndexToDisk(String fileName);

    /**
     * recover an index from an index file
     * @param fileName the index file path
     * @return the number of bytes read
     */
    public abstract Integer recoverIndexFromDisk(String fileName);
}
