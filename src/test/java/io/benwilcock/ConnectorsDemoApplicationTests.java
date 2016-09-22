package io.benwilcock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectorsDemoApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Status status;

    @Autowired
    StatusController statusController;

	@Test
	public void contextLoads() throws ExceptionIncludingMockitoWarnings{
	    assertThat(statusController).isNotNull();
	}

    @Test
    public void homePageHasMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).isNotEmpty();
    }

    @Test
    public void infoPageHasMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/info",
                String.class)).isNotEmpty();
    }

    @Test
    public void testStatus(){
        assertNotNull(status);
        assertNotNull(status.getId());
        System.out.println(status);
    }

    @Test
    public void testMongo(){
        assertNotNull(status);
        assertTrue(status.isMongo());
    }

    @Test
    public void testMongoString(){
        assertNotNull(status);
        assertThat(status.getMongo()).contains(":UP");
    }

    @Test
    public void testRabbit(){
        assertNotNull(status);
        assertTrue(status.isRabbit());
    }

    @Test
    public void testRabbitString(){
        assertNotNull(status);
        assertThat(status.getRabbit()).contains(":UP");
    }

    @Test
    public void testSql(){
        assertNotNull(status);
        assertTrue(status.isSql());
    }

    @Test
    public void testSqlString(){
        assertNotNull(status);
        assertThat(status.getSql()).contains(":UP");
    }

}
