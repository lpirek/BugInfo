/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.repo;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
class SVNWrapperImpl extends AbstractRepoWrapperImpl implements RepoWrapper{

    private String mUser;
    private String mPass;
    
    SVNWrapperImpl() {
        super();
    }

    
    /**
     * 
     * @param repositoryUrl
     * @param startRevision That must be a instance of Long
     * @return
     */
    @Override
    public ProjectInfo getBugs( Properties config) {
        String repositoryUrl = config.getRepourl();
        Object startRevision = config.getRepostartrev();
        Object endRevision = config.getRepoendrev();

        long stRev = getStartRevision( startRevision );
        SVNRevision endRev = getEndRevision( endRevision );
        setupLibrary();

        SVNClientManager svnClientManager =createSVNClientManager();
        SVNLogClient svnLogClient = svnClientManager.getLogClient();
        try {
            SVNURL mSvnUrl = SVNURL.parseURIEncoded(repositoryUrl);
            final ProjectInfo result = new ProjectInfo();

            System.out.println( "SVN client uses following parameter:" );
            System.out.println( "-repository URL: " + repositoryUrl );
            System.out.println( "-revision filter: " + stRev + ":" + endRev.toString() );
         
            svnLogClient.doLog(mSvnUrl, null, SVNRevision.UNDEFINED, SVNRevision.create(stRev), endRev, false, true, 0, new SVNLogListener(result, config) );
            
            result.calculateProgrammerAttributes();
            result.calculateWeightedCommitersForClasses();
            
            return result;
        } catch (SVNException ex) {
            Logger.getLogger(SVNWrapperImpl.class.getName()).log(Level.WARNING, "SVNException", ex);
            return null;
        } finally {
            svnClientManager.dispose();
        }
        
    }

    SVNClientManager createSVNClientManager(){
        SVNClientManager newClientManager=null;
        if( mPass!=null && mUser!=null ){
            DefaultSVNOptions svnOptions = SVNWCUtil.createDefaultOptions( true );
            newClientManager  = SVNClientManager.newInstance(svnOptions, mUser, mPass);
        }
        else{
            newClientManager =  SVNClientManager.newInstance();
        }
        return newClientManager;
    }

    private long getStartRevision(Object startRevision) {
        long result=0;
        
        if( startRevision != null){
            try {
                result = ((Long) startRevision).longValue();
            } catch (Exception e) {
                result = 0;
            }
        }
        
        return result;
    }
    
    private SVNRevision getEndRevision(Object endRevision) {
        SVNRevision result=SVNRevision.HEAD;
        
        if( endRevision != null ){
            try{
                long r = ((Long)endRevision).longValue();
                result = SVNRevision.create(r);
            } catch (Exception e ){
                //do nothing, result has already the default value
            }
            
        }
        
        return result;
    }
    
    void setUser(String user) {
        mUser = user;
    }

    String getUser(){
        return mUser;
    }

    void setPass(String pass) {
        mPass = pass;
    }

    String getPass(){
        return mPass;
    }
    
    
    /*
     * Initializes the library to work with a repository via different protocols.
     */
    private void setupLibrary() {

        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();

    }   

}
