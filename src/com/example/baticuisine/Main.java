package com.example.baticuisine;

import com.example.baticuisine.config.DatabaseConnection;
import com.example.baticuisine.repositories.implementations.*;
import com.example.baticuisine.services.implementations.*;
import com.example.baticuisine.validator.*;
import com.example.baticuisine.views.Menu;
import com.example.baticuisine.views.submenu.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseConnection dbConnection = new DatabaseConnection();
        //Connection connection = dbConnection.getConnection();
        Connection connection = dbConnection.getConnection();

        //repositories
        ClientRepository clientRepository = new ClientRepository(connection);
        ComponentRepository componentRepository = new ComponentRepository();
        LaborRepository laborRepository = new LaborRepository(componentRepository);
        MaterialRepository materialRepository = new MaterialRepository(componentRepository);
        ProjectRepository projectRepository = new ProjectRepository(clientRepository,componentRepository,materialRepository,laborRepository);
        QuoteRepository quoteRepository = new QuoteRepository();


        // validators
        ClientValidator clientValidator = new ClientValidator();
        ProjectValidator projectValidator = new ProjectValidator();
        MaterialValidator materialValidator = new MaterialValidator();
        LaborValidator laborValidator = new LaborValidator();
        ComponentValidator componentValidator = new ComponentValidator();
        QuoteValidator quoteValidator = new QuoteValidator();

        // services
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepository,clientValidator);
        ComponentServiceImpl componentService = new ComponentServiceImpl(componentRepository,componentValidator);
        LaborServiceImpl laborService = new LaborServiceImpl(laborRepository,laborValidator,componentRepository);
        MaterialServiceImpl materialService = new MaterialServiceImpl(materialRepository,materialValidator,componentRepository);
        ProjectServiceImpl projectService = new ProjectServiceImpl(projectRepository,projectValidator);
        QuoteServiceImpl quoteService = new QuoteServiceImpl(quoteRepository,quoteValidator);


        // menus

        MaterialMenu materialMenu = new MaterialMenu(materialService, new ComponentServiceImpl(componentRepository,componentValidator));
        LaborMenu laborMenu = new LaborMenu(laborService, new ComponentServiceImpl(componentRepository,componentValidator));
        ProjectMenu projectMenu = new ProjectMenu(projectService);
        SettingsMenu settingsMenu = new SettingsMenu(projectService,clientService);
        QuoteMenu quoteMenu = new QuoteMenu(projectService,clientService,quoteService);
        CostMenu costMenu = new CostMenu(projectRepository,componentRepository,materialService,laborService,quoteService,quoteMenu,projectService);

        // the main menu
        //            Menu menu = new Menu(clientService, projectService, materialMenu, laborMenu);
        Menu menu = new Menu(clientService,projectService,materialMenu,laborMenu,projectMenu,settingsMenu,costMenu);

        menu.display();








    }
}