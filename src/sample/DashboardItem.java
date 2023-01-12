package sample;

import java.util.ArrayList;

/**
 * This class holds all the projects
 * If the user is logged in as admin fetch all projects else fetch projects relevant to the client
 * Used by the dashboard method (in Controller class)
 * @author Kavishka Timashan
 */
public class DashboardItem{
    private static ArrayList<Item> dashboardItem=new ArrayList();

    /**
     * Constructor
     * Retrieves all project data from the database
     */
    DashboardItem(){
        Datasource datasource = new Datasource();
        if(datasource.Details()[0].equals("Admin")){           //If admin retrieve all projects
            dashboardItem=datasource.getProjects(true);
        }else{
            dashboardItem=datasource.getProjects(false);
        }
        datasource.close();
    }

    /**
     * Returns project data
     * @return dashboardItem (ArrayList)
     */
    public static ArrayList<Item> getDashboardItem() {
        return dashboardItem;
    }

//    /**
//     * Loads first
//     * Retrieves all project data from the database
//     */
//    static {
//        Datasource datasource = new Datasource();
//        if(datasource.Details()[0].equals("Admin")){           //If admin retrieve all projects
//            dashboardItem=datasource.getProjects(true);
//        }else{
//            dashboardItem=datasource.getProjects(false);
//        }
//        datasource.close();
//    }
}
