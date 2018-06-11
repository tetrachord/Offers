package com.foo.offers.resource;

import com.foo.offers.domain.Offer;
import com.foo.offers.repository.OfferNotFoundException;
import com.foo.offers.repository.OffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/offers")
public class OffersResource {

    @Autowired
    private OffersRepository offersRepository;

    @PostMapping("/offer")
    ResponseEntity<Offer> createOffer(@RequestBody Offer offer) {

        offersRepository.add(offer);

        return new ResponseEntity<>(offer, HttpStatus.CREATED);
    }

    @PutMapping("/offer")
    ResponseEntity<Offer> updateOffer(@RequestBody Offer offer) {

        try {
            offersRepository.update(offer);
        } catch(OfferNotFoundException onfe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(offer, HttpStatus.ACCEPTED);
    }

    @GetMapping("/offer/{offerId}")
    ResponseEntity<Offer> queryOffer(@PathVariable Long offerId) {

        Optional<Offer> OptionalOffer = offersRepository.getById(offerId);

        if (OptionalOffer.isPresent()) {
            return new ResponseEntity<>(OptionalOffer.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
