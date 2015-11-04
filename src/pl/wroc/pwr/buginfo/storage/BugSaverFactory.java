/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.storage;

import java.io.IOException;
import java.sql.SQLException;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author mjureczk
 */
public class BugSaverFactory {
    public static BugSaver createBugSaver(Properties pr) throws IOException, ClassNotFoundException, SQLException{
        BugSaver bs = null;
        String saverType = pr.getSavedestination();
        if( saverType.compareToIgnoreCase( "csv" ) == 0 ){
            bs = createCSV(pr);
        }
        else if( saverType.compareToIgnoreCase( "db" ) == 0 ){
            bs = createDB(pr);
        }
        return bs;
    }

    private static BugSaver createCSV(Properties pr) throws IOException {
        CSVBugSaver csv = new CSVBugSaver(pr.getCsvfilename(), pr);
        return csv;
    }

    private static BugSaver createDB(Properties pr) throws ClassNotFoundException, SQLException {
        DBBugSaver db = new DBBugSaver( pr );
        return db;
    }
}
