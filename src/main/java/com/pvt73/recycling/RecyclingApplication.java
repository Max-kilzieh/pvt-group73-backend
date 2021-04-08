package com.pvt73.recycling;


import com.pvt73.recycling.repository.RecycleStationRepository;
import com.pvt73.recycling.repository.WasteBinRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecyclingApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(RecyclingApplication.class);
        applicationContext.getBean("recycleStationRepository", RecycleStationRepository.class);
    }

    @Bean
    public CommandLineRunner importToDatabase(RecycleStationRepository repository, WasteBinRepository wasteBinRepository) {
        return (args) -> {

/*            //  Total: 245 recycling station
            int recycleStationCounter = 1;
            for (RecycleStation recycleStation: repository.findAll()){
                System.out.printf("recycle Station Nr. %d:   latitude: %s , longitude: %s%n", recycleStationCounter, recycleStation.getLatitude(), recycleStation.getLongitude());
                recycleStationCounter++;
            }*/


/*            //  Total: 12538 waste bin
            int wasteBinCounter = 1;
            for (WasteBin wasteBin:wasteBinRepository.findAll()) {
                System.out.printf("Waste bin Nr. %d:   latitude: %s , longitude: %s%n", wasteBinCounter, wasteBin.getLatitude(), wasteBin.getLongitude());
                wasteBinCounter++;
            }*/


            //Todo: delete this method when done with importing data to the database.

/*            BufferedReader csvReader = new BufferedReader(new FileReader("C:\\Users\\Max\\Desktop\\exel.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(";");
              System.out.println(Arrays.toString(data));

                double lat = Double.parseDouble(data[0]);
                double lon = Double.parseDouble(data[1]);
                wasteBinRepository.save(new WasteBin(lat,lon));
                wasteBinCounter++;
            }
            csvReader.close();*/



/*            ObjectMapper mapper = new ObjectMapper();
            List<RecycleStation> recycleStationList = Arrays.asList(mapper.readValue(new File("C:\\Users\\Max\\Desktop\\ftistations.json"), RecycleStation[].class));
                repository.saveAll(recycleStationList);*/

        };

    }


}

