package com.foo.offers.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Offer {

    private Long    id;
    private String  friendlyDescription;
    private Double  price;
    private String  currency;
    private Long    dateCreated;
    private Long    expirationPeriod;
    private Boolean isCancelled;
}
