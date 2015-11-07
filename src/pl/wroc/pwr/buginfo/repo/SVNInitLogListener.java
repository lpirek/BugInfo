package pl.wroc.pwr.buginfo.repo;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;

import pl.wroc.pwr.buginfo.ProjectInfo;
import pl.wroc.pwr.buginfo.Properties;

public class SVNInitLogListener extends AbstractRepoLogListener implements ISVNLogEntryHandler{
	
    SVNInitLogListener( ProjectInfo result, Properties config){
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
        if (mResult.getFirstCommit() == null)
        {
        	mResult.setFirstCommit(logEntry.getDate());
        }
        
        mResult.setLastCommit(logEntry.getDate());
        mResult.addCommit(logEntry.getAuthor(), logEntry.getDate());
    }

}
