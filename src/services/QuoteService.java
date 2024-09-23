package services;

import domain.entities.Project;
import domain.entities.Quote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteService {
    public Quote save(Quote quote);

    Optional<Quote> findById(UUID quoteId);

    List<Quote> findAll();

    Quote update(UUID quoteId, double estimatedAmount, LocalDate issueDate, LocalDate validatedDate, boolean isAccepted, Project project);

    boolean delete(UUID quoteId);

    List<Quote> findQuotesByProject(Project project);

    boolean updateDevisStatus(UUID id);
}
