package wsm.server.service;

import org.springframework.stereotype.Component;
import wsm.engine.InstrumentConstruction;
import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.engine.auxiliaryIndex.IndexIdToDoc;
import wsm.models.CourtInfo;
import wsm.models.CourtInfoLoader;
import wsm.server.repository.IndexRepository;
import wsm.server.repository.InstrumentRepository;
import wsm.utils.TfIdfUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class IndexService implements IndexRepository {


    @Override
    public List<CourtInfo> IndexQuery(String query) {
        return null;
    }
}

