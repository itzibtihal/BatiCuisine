package com.example.baticuisine.services.implementations;

import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.domain.entities.Quote;
import com.example.baticuisine.repositories.interfaces.QuoteInterface;

import com.example.baticuisine.services.QuoteService;
import com.example.baticuisine.validator.QuoteValidator;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuoteServiceImpl implements QuoteService {

    private final QuoteInterface quoteRepository;
    private final QuoteValidator quoteValidator;


    public QuoteServiceImpl(QuoteInterface quoteRepository, QuoteValidator quoteValidator) {
        this.quoteRepository = quoteRepository;
        this.quoteValidator = quoteValidator;
    }


    @Override
    public Quote save(Quote quote) {
        quoteValidator.validate(quote);
        return quoteRepository.save(quote);
    }


    @Override
    public Optional<Quote> findById(UUID quoteId) {
        Quote quote = new Quote();
        quote.setId(quoteId);
        return quoteRepository.findById(quote);
    }


    @Override
    public List<Quote> findAll() {
        return quoteRepository.findAll();
    }


    @Override
    public Quote update(UUID quoteId, double estimatedAmount, LocalDate issueDate, LocalDate validatedDate, boolean isAccepted, Project project) {
        Optional<Quote> existingQuoteOpt = findById(quoteId);
        if (existingQuoteOpt.isPresent()) {
            Quote existingQuote = existingQuoteOpt.get();
            existingQuote.setEstimatedAmount(estimatedAmount);
            existingQuote.setIssueDate(issueDate);
            existingQuote.setValidatedDate(validatedDate);
            existingQuote.setAccepted(isAccepted);
            existingQuote.setProject(project);
            quoteValidator.validate(existingQuote);

            return quoteRepository.update(existingQuote);
        } else {
            throw new IllegalArgumentException("Quote with ID " + quoteId + " not found.");
        }
    }


    @Override
    public boolean delete(UUID quoteId) {
        Optional<Quote> quoteOpt = findById(quoteId);
        if (quoteOpt.isPresent()) {
            return quoteRepository.delete(quoteOpt.get());
        } else {
            throw new IllegalArgumentException("Quote with ID " + quoteId + " not found.");
        }
    }


    @Override
    public List<Quote> findQuotesByProject(Project project) {
        return quoteRepository.findQuotesByProject(project);
    }


    @Override
    public boolean updateDevisStatus(UUID id) {
        return quoteRepository.updateDevisStatus(id);
    }



}
