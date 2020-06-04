package wsm.preprocess;

import wsm.models.CourtInfo;
import java.util.LinkedList;

public abstract class IndexAbstract {

    /**
     * update the index from courtInfo
     * @param courtInfo the courtInfo object
     */
    public abstract void updateFromCourtInfo(CourtInfo courtInfo);

    /**
     * process a request query string with index
     * return a posting List from a query string
     * @param queryString the String for quering
     * @return the posting list
     */
    public abstract LinkedList<Integer> queryFromRequestString(String queryString);


    /**
     * store the index into an index file
     * @param fileName the index file path
     * @return the number of bytes written
     */
    public abstract Integer StoreIndexToDisk(String fileName);

    /**
     * recover an index from an index file
     * @param fileName the index file path
     * @return the number of bytes read
     */
    public abstract Integer recoverIndexFromDisk(String fileName);
}
