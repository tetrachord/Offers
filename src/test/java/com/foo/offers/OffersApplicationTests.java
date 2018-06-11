package com.foo.offers;

import com.foo.offers.domain.Offer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OffersApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String base;

	private Offer offer;


	@Before
	public void setUp() throws Exception {
		this.base = "http://localhost:" + port + "/";
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldCreateAnOffer() throws URISyntaxException {

		assertThatOfferIsCreated();
	}

	@Test
	public void shouldQueryAnExistingOffer() throws URISyntaxException {

		// given
		assertThatOfferIsCreated();

		String url = base + "offers/offer/12345";

		// when
		ResponseEntity<Offer> response = restTemplate.getForEntity(url, Offer.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(offer);
	}

	@Test
	public void shouldReturnNotFoundForAnExpiredOffer() throws InterruptedException {

		// given
		assertThatOfferIsCreated();

		Thread.sleep(35000);

		String url = base + "offers/offer/12345";

		// when
		ResponseEntity<Offer> response = restTemplate.getForEntity(url, Offer.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void shouldCancelAnOfferBeforeItHasExpired() throws InterruptedException {

		// given
		assertThatOfferIsCreated();

		String getUrl = base + "offers/offer/12345";

		// when
		ResponseEntity<Offer> getResponse = restTemplate.getForEntity(getUrl, Offer.class);

		// then
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().getIsCancelled()).isFalse();

		// given
		Offer updatedOffer = offer.toBuilder().isCancelled(true).build();
		HttpEntity<Offer> request = new HttpEntity<>(updatedOffer);

		String putUrl = base + "offers/offer";

		// when
		ResponseEntity<Offer> putResponse = restTemplate.exchange(putUrl, HttpMethod.PUT, request, Offer.class);

		// then
		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
	}

	private void assertThatOfferIsCreated() {

		// given
		String url = base + "offers/offer";

		offer = Offer.builder()
				.id(12345l)
				.friendlyDescription("Jeans")
				.currency("GBP")
				.price(10.00d)
				.isCancelled(false)
				.dateCreated(ZonedDateTime.now().toEpochSecond())
				.expirationPeriod(Duration.ofSeconds(30l).getSeconds())
				.build();

		HttpEntity<Offer> request = new HttpEntity<>(offer);

		// when
		ResponseEntity<Offer> response = restTemplate.postForEntity(url, request, Offer.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo(offer);
	}

}
