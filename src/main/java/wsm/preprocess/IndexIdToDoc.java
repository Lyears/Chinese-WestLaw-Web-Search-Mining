package wsm.preprocess;


import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class IndexIdToDoc {

    // the index Object for docId to DocFileName
    private HashMap<Integer, String> hashMapIdToDoc;

    public IndexIdToDoc(){
        this.hashMapIdToDoc = new HashMap<>();
    }

    /**
     * Traverse the datadir and refresh the hashMap (from docId to docFileName)
     * @param docDirPath the root path for document folder
     * @param docIdOffset the offset for docId generation
     */
    public void updateMapFromDataset(String docDirPath,
                                      int docIdOffset) {

        File docDirObj = new File(docDirPath);
        if (!docDirObj.exists()) {
            return;
        }

        File[] files = docDirObj.listFiles();
        if (files == null) {
            return;
        }
        // the sequence of doc, accumulate from 1
        int sequence = 1;

        // traverse all files
        for (File docFile : files) {
            // only traverse the files, no sub-dirs
            if (docFile.isFile()) {
                this.hashMapIdToDoc.put(docIdOffset + sequence, docFile.getAbsolutePath());
                sequence += 1;
            }
        }
        System.out.printf("Totally %d docs are Indexed.\n", sequence - 1);
    }

    /**
     * get the FileName from docId
     * @param id the docId
     * @return the file name string
     */
    public String getDocFileNameFromID(Integer id){

        return this.hashMapIdToDoc.getOrDefault(id, "");
    }

}
