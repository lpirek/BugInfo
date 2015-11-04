/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo;

import pl.wroc.pwr.buginfo.storage.DBBugSaver;
import pl.wroc.pwr.buginfo.repo.CVSLogListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.netbeans.lib.cvsclient.event.MessageEvent;

/**
 *
 * @author marian
 */
public class MainLoadFromLogFile {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Properties props = new Properties();
        
        ProjectInfo bugs = readfile("server-zvk.log",props);
        if( bugs.isEmpty() ){
            System.err.println( "There is no information about the bugs. \nEXIT");
            return;
        }
        
        System.out.println("Importing data to data base...");
        DBBugSaver db = new DBBugSaver( props );
        List<String> notImported = db.save(bugs);
        
        System.out.println( "Information about " + String.valueOf(notImported.size()) + " classes hasn't been imported to db." );
        
        if( notImported.size() > 0 ){
            System.out.println( "Following classes hasn't been found in the data base and thus the information about the following classes hasen't been imported:" );
            Iterator<String> itr = notImported.iterator();
            while( itr.hasNext() ){
                System.out.println( itr.next() );
            }
        }
    }

    private static ProjectInfo readfile(String fileNamen, Properties props) throws FileNotFoundException, IOException {
        ProjectInfo results = new ProjectInfo();
        String pre = props.getReposrcpathprefixregex();
        String post = props.getReposrcpathpostfixregex();
        char sep = props.getRepofileseparator();
        CVSLogListener lListener = new CVSLogListener(results, props.getRepobugfixregex(), pre, post, sep, props );
        BufferedReader in = new BufferedReader( new InputStreamReader( new FileInputStream(fileNamen) ));
        
        String s;        
        while( (s=in.readLine()) != null ){
            lListener.messageSent( new MessageEvent(results, s, false) );
        }
        
//        Set<String> keys = results.keySet();
//        Iterator<String> itr = keys.iterator();
//        while( itr.hasNext() ){
//            String next = itr.next();
//            System.out.println( "klasa: " + next + " bug: " + results.get( next )  );
//        }
        
        return results;
    }
    
    

}
