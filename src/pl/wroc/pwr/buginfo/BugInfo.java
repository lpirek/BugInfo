/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo;

import pl.wroc.pwr.buginfo.storage.*;
import pl.wroc.pwr.buginfo.repo.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author marian
 */
public class BugInfo {

    protected Properties properties;
    protected RepoWrapper repo;
    protected BugSaver saver;

    public BugInfo() throws IOException, ClassNotFoundException, SQLException {
        properties = new Properties();
        initFields();
    }

    public BugInfo(String proprtiesFile)throws IOException, ClassNotFoundException, SQLException {
        properties = new Properties(proprtiesFile);
        initFields();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        BugInfo bi = new BugInfo();
        bi.collectAndSaveBugs();
        
    }

    void collectAndSaveBugs() throws ClassNotFoundException, SQLException, IOException {
        System.out.println("Retriving data from the repository...");
        ProjectInfo bugs = collectBugs();

        if (bugs.isEmpty()) {
            System.out.println("There is no information about the bugs." + 
                    System.getProperty("line.separator") + "EXIT");
            return;
        }

        long n = bugs.size();
        System.out.println("Storing data about bugs...");
        List<String> notImported = saveBugInfo(properties, bugs);
        informAboutImportResults(n, notImported);
    }


    protected ProjectInfo collectBugs() {
        return repo.getBugs( properties);
    }
    

    /** @return list with not imported bugs, in fact that are class names that contained those bugs */
    protected List<String> saveBugInfo(Properties props, ProjectInfo bugs) throws ClassNotFoundException, SQLException {
        List<String> notImported = saver.save(bugs);
        
        return notImported;
    }

    private void informAboutImportResults(long numOfClassesWithBugs, List<String> notImported) {
        System.out.println("Information about " + String.valueOf(numOfClassesWithBugs - notImported.size()) +
                " classes has been collected.");
        if (notImported.size() > 0) {
            System.out.println("Following classes hasn't been found in the data base and thus the information about the following classes hasen't been imported:");
            Iterator<String> itr = notImported.iterator();
            while (itr.hasNext()) {
                System.out.println(itr.next());
            }
        }
    }

    private void initFields() throws SQLException, IOException, ClassNotFoundException {
        repo = RepoWrapperFactory.createWrapper(properties);
        saver = BugSaverFactory.createBugSaver(properties);
    }

    

}
