package org.doctordrue.sharedcosts.business.model.debt_calculation;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Person;

public class Total extends Money {
    private Person person;

    public Person getPerson() {
        return person;
    }

    public Total setPerson(Person person) {
        this.person = person;
        return this;
    }

    @Override
    public Total setAmount(Double amount) {
        super.setAmount(amount);
        return this;
    }

    @Override
    public Total setCurrency(Currency currency) {
        super.setCurrency(currency);
        return this;
    }
}
