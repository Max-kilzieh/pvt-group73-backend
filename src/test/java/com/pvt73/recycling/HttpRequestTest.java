package com.pvt73.recycling;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void wasteBinsWithinHundredMeterFromSkrapan() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/waste_bins/nearest_waste_bins?latitude=59.31805206&longitude=18.06298083",
                String.class)).contains("[{\"id\":4659,\"latitude\":59.31868344,\"longitude\":18.06215355},{\"id\":4660,\"latitude\":59.31875259,\"longitude\":18.06255562},{\"id\":4661,\"latitude\":59.31819069,\"longitude\":18.06264087},{\"id\":4662,\"latitude\":59.31864867,\"longitude\":18.06271596},{\"id\":4663,\"latitude\":59.31835781,\"longitude\":18.06283144},{\"id\":4664,\"latitude\":59.31805206,\"longitude\":18.06298083},{\"id\":4665,\"latitude\":59.31881556,\"longitude\":18.06298616},{\"id\":4666,\"latitude\":59.31859865,\"longitude\":18.06300566},{\"id\":4667,\"latitude\":59.31770034,\"longitude\":18.06317187},{\"id\":4668,\"latitude\":59.31827077,\"longitude\":18.06322316},{\"id\":4669,\"latitude\":59.31880872,\"longitude\":18.06332445},{\"id\":4671,\"latitude\":59.31781586,\"longitude\":18.06337702},{\"id\":4672,\"latitude\":59.31762451,\"longitude\":18.06385984}]");
    }
}
