package com.foo.offers.repository;

import com.foo.offers.domain.Offer;
import org.junit.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class OffersRepositoryTest {

    private OffersRepository offersRepository = new OffersRepository();

    @Test
    public void shouldAddOffer() {

        // given
        Offer offer = givenAnOfferIsCreated();

        // then
        Optional<Offer> optionalOffer = offersRepository.getById(offer.getId());
        assertThat(optionalOffer.isPresent()).isTrue();
        Offer retrievedOffer = optionalOffer.get();
        assertThat(retrievedOffer).isEqualTo(offer);
    }

    @Test
    public void shouldNotFindNonExistentOffer() {

        // when
        Optional<Offer> optionalOffer = offersRepository.getById(22222l);

        // then
        assertThat(optionalOffer.isPresent()).isFalse();
    }

    @Test
    public void shouldUpdateOffer() {

        // given
        Offer offer = givenAnOfferIsCreated();

        Offer updatedOffer = offer.toBuilder().friendlyDescription("Shoes").build();

        // when
        offersRepository.update(updatedOffer);

        // then
        Optional<Offer> optionalOffer = offersRepository.getById(updatedOffer.getId());
        assertThat(optionalOffer.isPresent()).isTrue();
        Offer retrievedOffer = optionalOffer.get();
        assertThat(retrievedOffer.getFriendlyDescription()).isEqualTo("Shoes");
    }

    @Test
    public void shouldNotUpdateNonExistentOffer() {

        // given
        Offer offer = Offer.builder()
                .id(33333l)
                .friendlyDescription("Jacket")
                .currency("GBP")
                .price(20.00d)
                .isCancelled(false)
                .dateCreated(ZonedDateTime.now().toEpochSecond())
                .expirationPeriod(Duration.ofSeconds(30l).getSeconds())
                .build();

        // when
        Throwable thrown = catchThrowable( () -> offersRepository.update(offer) );

        // then
        assertThat(thrown).isInstanceOf(OfferNotFoundException.class);
    }

    private Offer givenAnOfferIsCreated() {

        // given
        Offer offer = Offer.builder()
                .id(987654l)
                .friendlyDescription("Dress")
                .currency("GBP")
                .price(20.00d)
                .isCancelled(false)
                .dateCreated(ZonedDateTime.now().toEpochSecond())
                .expirationPeriod(Duration.ofSeconds(30l).getSeconds())
                .build();

        // when
        offersRepository.add(offer);
        return offer;
    }
}