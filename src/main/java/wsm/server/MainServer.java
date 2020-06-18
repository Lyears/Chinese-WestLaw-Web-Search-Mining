package wsm.server;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import wsm.server.repository.IndexRepository;
import wsm.server.repository.InstrumentRepository;
import wsm.server.service.IndexService;
import wsm.server.service.InstrumentService;

import java.util.concurrent.TimeUnit;

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

    @Bean
    public Cache<String, Object> caffeineCache(){
        return Caffeine.newBuilder()
                .expireAfterWrite(180, TimeUnit.SECONDS)
                .initialCapacity(10)
                .maximumSize(100)
                .build();
    }

}
