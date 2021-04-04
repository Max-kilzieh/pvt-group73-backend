package com.pvt73.recycling;

import com.pvt73.recycling.repository.RecycleStationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecyclingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecyclingApplication.class);
    }

    @Bean
    public CommandLineRunner readJSONtoDatabase(RecycleStationRepository repository) {
        return (args) -> {

            //Todo: remove this method when done with reading JSON files

/*            ObjectMapper mapper = new ObjectMapper();
            List<RecycleStation> recycleStationList = Arrays.asList(mapper.readValue(new File("C:\\Users\\Max\\Desktop\\ftistations.json"), RecycleStation[].class));
                repository.saveAll(recycleStationList);*/

        };

    }

}

