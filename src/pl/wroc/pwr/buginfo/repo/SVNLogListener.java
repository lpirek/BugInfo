/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.repo;

import java.util.Iterator;
import java.util.Set;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public class SVNLogListener extends AbstractRepoLogListener implements ISVNLogEntryHandler{

    SVNLogListener( ProjectInfo result, Properties config ){
        super( result, 
                config.getRepobugfixregex(),
                config.getReposrcpathprefixregex(),
                config.getReposrcpathpostfixregex(),
                config.getRepofileseparator() );
        mReadModifications = config.readNumberOfModifications();
        mReadCommiters = config.readNumberOfCommiters();
        mReadBugs = config.readNumberOfBugs();
    }
    
    public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
        boolean isBugFix=false;
        if( !isBugFix( logEntry.getMessage() )  ){
            if( !mReadModifications )
                return;
        }
        else{
            isBugFix=true;
        }
        
        

        Set<String> keys = logEntry.getChangedPaths().keySet();
        Iterator<String> itr = keys.iterator();
        while( itr.hasNext() ) {
            String className = itr.next();
            if( isPathToClass(className) ){
                className = convertPathToClassName(className);
                //TODO: if className.length throw Exception("bad prefix!");
                if( isBugFix && mReadBugs )
                    mResult.addBugs(className, 1);
                if( mReadModifications )
                    mResult.addModifications(className, 1);
                handleCommiter(logEntry,className);
            }
        }
    }

    private void handleCommiter(SVNLogEntry logEntry, String className) {
        if( mReadCommiters ){
            String commiter=logEntry.getAuthor();
            mResult.addCommiter(className, commiter);
        }
    }
    

}
