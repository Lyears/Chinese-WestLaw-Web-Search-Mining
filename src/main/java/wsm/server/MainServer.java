package wsm.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ModelAndView;
import wsm.server.repository.InstrumentRepository;
import wsm.server.service.InstrumentService;

@SpringBootApplication
public class MainServer {

    public static void main(String[] args) {
        SpringApplication.run(MainServer.class, args);

    }

    @Bean
    public InstrumentRepository instrumentRepository(){
        return new InstrumentService();
    }

}
