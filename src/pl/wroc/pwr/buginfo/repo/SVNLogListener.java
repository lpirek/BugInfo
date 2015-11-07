/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.repo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;

import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public class SVNLogListener extends AbstractRepoLogListener implements ISVNLogEntryHandler{
	
	private SVNDiffClient mDiffClient;
	private SVNRepository mRepository;
	private long mPreviousRev;

    SVNLogListener( ProjectInfo result, Properties config, SVNClientManager clientManager, SVNURL repoUrl) throws SVNException {
        super( result, 
                config.getRepobugfixregex(),
                config.getReposrcpathprefixregex(),
                config.getReposrcpathpostfixregex(),
                config.getRepofileseparator() );
        mReadModifications = config.readNumberOfModifications();
        mReadCommiters = config.readNumberOfCommiters();
        mReadBugs = config.readNumberOfBugs();
        mDiffClient = clientManager.getDiffClient();
        mRepository = clientManager.createRepository(repoUrl, true);
        mPreviousRev = -1;
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
        
        Map<String, SVNLogEntryPath> paths = logEntry.getChangedPaths();
        Set<String> keys = paths.keySet();
        Iterator<String> itr = keys.iterator();
        String outputName = "diff.txt";
        FileOutputStream fos = null;
        BufferedReader br = null;
        String currentLine = "";        
        long modifiedLines = 0;
        
        while( itr.hasNext() ) {
            String className = itr.next();
            if( isPathToClass(className) ){
            	if (mPreviousRev != -1)
            	{
	            	SVNURL filePath = mRepository.getRepositoryRoot(false).appendPath(className, true);
	            	
	            	System.out.println("FilePath: "+ filePath+ ", Revision: "+ logEntry.getRevision()+", previous revision: "+ mPreviousRev);
	
	       	            	
	            	if (paths.get(className).getType() == SVNLogEntryPath.TYPE_MODIFIED)
	            	{
		                fos = null;
		                br = null;
		                currentLine = "";	                
		                modifiedLines = 0;
		                
		                try
		                {
		                	fos = new FileOutputStream(new File(outputName));
		    	         	mDiffClient.doDiff(filePath,
		    	         			SVNRevision.create(logEntry.getRevision()), filePath,
		    	        			SVNRevision.create(mPreviousRev), SVNDepth.FILES, true, fos);
		    	         	fos.flush();
		    	         	fos.close();
		    	         	
		    	         	br = new BufferedReader(new FileReader(outputName));
		    	         	while ((currentLine = br.readLine()) != null)
		    	         	{
		    	         		if (currentLine.length() > 0 && (currentLine.charAt(0) == '+' || currentLine.charAt(0) == '-'))
		    	         		{
		    	         			modifiedLines++;
		    	         		}
		    	         	}
		    	         	
		    	         	if (modifiedLines >= 2)
		    	         		modifiedLines -=2;
		    	         	
		    	         	br.close();
		    	         	
		    	         	
		    	         	System.out.println("Modified lines: " + modifiedLines);
		    	         	
		                }
		                catch(IOException e)
		                {
		                	System.out.println(e.getMessage());
		                }
		                
		                mResult.addModifiedLines(className, modifiedLines);
		                mResult.addModifiedLinesWeightedWithExp(className, (1 - mResult.getProgrammerByName(logEntry.getAuthor()).getExperience())*modifiedLines);
		                mResult.addModifiedLinesWeightedWithInv(className, (1 - mResult.getProgrammerByName(logEntry.getAuthor()).getInvolvement())*modifiedLines);
	                
	            	}
            	}
            	
           	

                className = convertPathToClassName(className);
                //TODO: if className.length throw Exception("bad prefix!");
                if( isBugFix && mReadBugs )
                    mResult.addBugs(className, 1);
                if( mReadModifications )
                    mResult.addModifications(className, 1);
                handleCommiter(logEntry,className);
            }
        }
        
		mPreviousRev = logEntry.getRevision();
        
    }

    private void handleCommiter(SVNLogEntry logEntry, String className) {
        if( mReadCommiters ){
            String commiter=logEntry.getAuthor();
            if (!mResult.isCommiter(className, commiter))
            {
	            mResult.addCommiter(className, commiter);
	            mResult.addCommiterWeightedWithExp(className, (1 - mResult.getProgrammerByName(commiter).getExperience()));
	            mResult.addCommiterWeightedWithInv(className, (1 - mResult.getProgrammerByName(commiter).getInvolvement()));
            }
        }
    }
    

}
