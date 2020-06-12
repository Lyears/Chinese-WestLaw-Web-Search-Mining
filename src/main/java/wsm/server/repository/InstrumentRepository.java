package wsm.server.repository;

import org.springframework.stereotype.Component;
import wsm.models.CourtInfo;

import java.util.List;

@Component
public interface InstrumentRepository {

    List<CourtInfo> queryInstrument(String query);

}
