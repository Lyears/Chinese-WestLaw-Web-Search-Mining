package wsm.preprocess;

import wsm.utils.DiskIOHandler;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class IndexIdToDoc implements Serializable {

    public static final long serialVersionUID = 362498820111111111L;

    // the index Object for docId to DocFileName
    private HashMap<Integer, String> hashMapIdToDoc;

    public IndexIdToDoc(){
        this.hashMapIdToDoc = new HashMap<>();
    }

    /**
     * Traverse the datadir and refresh the hashMap (from docId to docFileName)
     * @param docDirPath the root path for document folder
     * @param docIdOffset the offset for docId generation
     * @return number of docs processed
     */
    public int updateMapFromDataset(String docDirPath,
                                      int docIdOffset) {

        File docDirObj = new File(docDirPath);
        if (!docDirObj.exists()) {
            return -1;
        }

        File[] files = docDirObj.listFiles();
        if (files == null) {
            return -1;
        }
        // the sequence of doc, accumulate from 1
        int sequence = 0;

        // traverse all files
        for (File docFile : files) {
            // only traverse the files, no sub-dirs
            if (docFile.isFile()) {
                this.hashMapIdToDoc.put(docIdOffset + sequence, docFile.getAbsolutePath());
                sequence += 1;
            }
        }
        System.out.printf("Totally %d reflection from Id to Document are Indexed.\n", sequence);
        return sequence;
    }

    /**
     * get the FileName from docId
     * @param id the docId
     * @return the file name string
     */
    public String getDocFileNameFromID(Integer id){

        return this.hashMapIdToDoc.getOrDefault(id, "");
    }

    /**
     * store the index into an index file
     * @param fileRootPath the index root file path
     */
    public void storeIndexToDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/IndexIdToDoc";
        DiskIOHandler.writeObjectToFile(this, fileName);
    }

    /**
     * recover an index from an index file
     * @param fileRootPath the index file path
     * @return the recovered IndexIdToDoc object
     */
    public static IndexIdToDoc recoverIndexFromDisk(String fileRootPath){
        String fileName = fileRootPath + "/boolean_index/IndexIdToDoc";
        return (IndexIdToDoc) DiskIOHandler.readObjectFromFile(fileName);
    }

}
