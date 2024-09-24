package com.example.baticuisine.validator;


import com.example.baticuisine.domain.entities.Quote;

public class QuoteValidator {

    public void validate(Quote quote) {
        if (quote.getEstimatedAmount() <= 0) {
            throw new IllegalArgumentException("Estimated amount must be greater than 0.");
        }

        if (quote.getIssueDate() == null) {
            throw new IllegalArgumentException("Issue date cannot be null.");
        }

        if (quote.getProject() == null) {
            throw new IllegalArgumentException("Quote must be associated with a project.");
        }

    }
}

