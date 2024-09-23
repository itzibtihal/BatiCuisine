import config.DatabaseConnection;
import repositories.implementations.*;
import services.ComponentService;
import services.implementations.*;
import validator.*;
import views.Menu;
import views.submenu.LaborMenu;
import views.submenu.MaterialMenu;
import views.submenu.ProjectMenu;
import views.submenu.SettingsMenu;

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


        // Initialize validators
        ClientValidator clientValidator = new ClientValidator();
        ProjectValidator projectValidator = new ProjectValidator();
        MaterialValidator materialValidator = new MaterialValidator();
        LaborValidator laborValidator = new LaborValidator();
        ComponentValidator componentValidator = new ComponentValidator();

        // services
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepository,clientValidator);
        ComponentServiceImpl componentService = new ComponentServiceImpl(componentRepository,componentValidator);
        LaborServiceImpl laborService = new LaborServiceImpl(laborRepository,laborValidator);
        MaterialServiceImpl materialService = new MaterialServiceImpl(materialRepository,materialValidator);
        ProjectServiceImpl projectService = new ProjectServiceImpl(projectRepository,projectValidator);

        //
        // Initialize menus

        MaterialMenu materialMenu = new MaterialMenu(materialService, new ComponentServiceImpl(componentRepository,componentValidator));
        LaborMenu laborMenu = new LaborMenu(laborService, new ComponentServiceImpl(componentRepository,componentValidator));
        ProjectMenu projectMenu = new ProjectMenu(projectService);
        SettingsMenu settingsMenu = new SettingsMenu(projectService,clientService);

        //// Initialize the main menu
        //            Menu menu = new Menu(clientService, projectService, materialMenu, laborMenu);
        Menu menu = new Menu(clientService,projectService,materialMenu,laborMenu,projectMenu,settingsMenu);
        //to make sure everything is uptodate
        menu.display();








    }
}