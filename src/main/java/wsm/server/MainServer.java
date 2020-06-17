package wsm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import wsm.server.repository.IndexRepository;
import wsm.server.repository.InstrumentRepository;
import wsm.server.service.IndexService;
import wsm.server.service.InstrumentService;

@SpringBootApplication
public class MainServer {

    public static void main(String[] args) {
        String wsmRootDir = System.getenv("WSM_ROOT_DIR");
        if (wsmRootDir == null) {
            throw new RuntimeException("Please first set environment variable WSM_ROOT_DIR");
        }
        SpringApplication.run(MainServer.class, args);

    }

    @Bean
    public InstrumentRepository instrumentRepository() {
        return new InstrumentService();
    }

    @Bean
    public IndexRepository indexRepository() {
        return new IndexService();
    }

}
