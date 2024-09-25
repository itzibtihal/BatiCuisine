package com.example.baticuisine.views.submenu;

import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.domain.entities.Quote;
import com.example.baticuisine.exceptions.QuotesNotFoundException;
import com.example.baticuisine.services.implementations.ClientServiceImpl;
import com.example.baticuisine.services.implementations.ProjectServiceImpl;
import com.example.baticuisine.services.implementations.QuoteServiceImpl;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class QuoteMenu {

    private final ProjectServiceImpl projectService;
    private final ClientServiceImpl clientService;
    private final QuoteServiceImpl quoteService;
    private final Scanner scanner;

    public QuoteMenu(ProjectServiceImpl projectService, ClientServiceImpl clientService, QuoteServiceImpl quoteService) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.quoteService = quoteService;
        this.scanner = new Scanner(System.in);
    }


    public void findQuotesByProject(UUID projectId) {
        Project project = new Project();
        project.setId(projectId);

        List<Quote> quotes = this.quoteService.findQuotesByProject(project);

        if (quotes.isEmpty()) {
            throw new QuotesNotFoundException("Devis not found!");
        }


        System.out.printf("+--------------------------------------+-------------------+--------------+----------------+-------------+--------------------+--------------------+%n");
        System.out.printf("| %-36s | %-17s | %-12s | %-14s | %-11s | %-18s | %-18s |%n",
                "Devis ID", "Estimated Amount", "Issue Date", "Validated Date", "Is Accepted", "Project Name", "Client Name");
        System.out.printf("+--------------------------------------+-------------------+--------------+----------------+-------------+--------------------+--------------------+%n");


        for (Quote devis1 : quotes) {
            String projectName = "N/A";
            String clientName = "N/A";

            if (devis1.getProject() != null) {
                projectName = devis1.getProject().getProjectName() != null
                        ? devis1.getProject().getProjectName()
                        : "N/A";

                if (devis1.getProject().getClient() != null) {
                    clientName = devis1.getProject().getClient().getName() != null
                            ? devis1.getProject().getClient().getName()
                            : "N/A";
                }
            }

            System.out.printf("| %-36s | %-17.2f | %-12s | %-14s | %-11b | %-18s | %-18s |%n",
                    devis1.getId().toString(),
                    devis1.getEstimatedAmount(),
                    devis1.getIssueDate(),
                    devis1.getValidatedDate() != null ? devis1.getValidatedDate() : "N/A",
                    devis1.isAccepted(),
                    projectName,
                    clientName
            );
        }



        System.out.printf("+--------------------------------------+-------------------+--------------+----------------+-------------+--------------------+--------------------+%n");
    }







}
