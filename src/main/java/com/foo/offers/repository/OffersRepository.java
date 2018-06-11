package com.foo.offers.repository;

import com.foo.offers.domain.Offer;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class OffersRepository {

    private Map<Long, Offer> offersStore = new HashMap<>();


    public void add(Offer offer) {

        offersStore.put(offer.getId(), offer);
    }

    public Optional<Offer> getById(Long offerId) {

        Offer offer = offersStore.get(offerId);

        if (offer == null) {
            return Optional.empty();
        }

        return hasExpired(offer) ? Optional.empty() : Optional.of(offer);
    }

    public void update(Offer updatedOffer) {

        Optional<Offer> OptionalOffer = getById(updatedOffer.getId());

        if ( OptionalOffer.isPresent() ) {
            offersStore.put(updatedOffer.getId(), updatedOffer);
        } else {
            throw new OfferNotFoundException();
        }
    }

    private boolean hasExpired(Offer offer) {

        Long dateCreated = offer.getDateCreated();
        Long expirationPeriod = offer.getExpirationPeriod();

        long now = ZonedDateTime.now().toEpochSecond();

        return (now > dateCreated + expirationPeriod);
    }
}
