package repositories.interfaces;

import domain.entities.Project;
import domain.entities.Quote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteInterface extends CrudInterface<Quote> {

    @Override
    public Quote save(Quote entity);

    @Override
    public Optional<Quote> findById(Quote quote);

    @Override
    public List<Quote> findAll();

    @Override
    public Quote update(Quote entity);

    @Override
    public boolean delete(Quote entity);

    List<Quote> findQuotesByProject(Project project);

    boolean updateDevisStatus(UUID id);

}
