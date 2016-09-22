package io.benwilcock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectorsDemoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
    public void testIndex(){
	    System.out.println(restTemplate.getForEntity("/", String.class).toString());
    }

}
