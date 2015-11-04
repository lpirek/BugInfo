/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.repo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.command.*;
import org.netbeans.lib.cvsclient.command.log.LogCommand;
import org.netbeans.lib.cvsclient.connection.*;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public class CVSWrapperImpl extends AbstractRepoWrapperImpl implements RepoWrapper{

    private String mWorkingCopy = null;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public CVSWrapperImpl(){
        super();
    }
    
    @Override
    public ProjectInfo getBugs( Properties config) {
        String repositoryUrl = config.getRepourl();
        Object startRevision = config.getRepostartrev();
        Object endRevision = config.getRepoendrev();
        CVSRoot cvsroot = CVSRoot.parse( repositoryUrl );       

        Connection con = createConnection(cvsroot);
        ProjectInfo result = new ProjectInfo();

        try {
            con.open();
            Client client = new Client(con, new StandardAdminHandler());
            client.setLocalPath( getWorkingCopy() );
            CVSLogListener logListener = new CVSLogListener( result, getBugfixPattern(), getSrcPathPrefix(), getSrcPathPostfix(), getFileSeparator(), config );
            logListener.setWorkingcopy( getWorkingCopy() );
            client.getEventManager().addCVSListener(logListener);
            GlobalOptions options = new GlobalOptions(); 
            options.setCVSRoot(repositoryUrl);
            
            System.out.println( "CVS client uses following parameter:" );
            System.out.println( "-repository URL: " + repositoryUrl );
            System.out.println( "-working copy: " + getWorkingCopy() );
            
            LogCommand command = new LogCommand();
            command.setRecursive(true);
            if( startRevision != null ){
                Date startRev = (Date)startRevision;
                String formatedStart = mDateFormat.format(startRev) + "<" + getEndDate(endRevision);
                System.out.println( "-date filter: " + formatedStart );
                command.setDateFilter( formatedStart );
            }
            client.executeCommand(command, options);

        } catch (AuthenticationException ex) {
            Logger.getLogger(CVSWrapperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CommandAbortedException ex) {
            Logger.getLogger(CVSWrapperImpl.class.getName()).log(Level.SEVERE, null, ex);
        }catch (CommandException ex) {
            Logger.getLogger(CVSWrapperImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private Connection createConnection(CVSRoot cvsroot) {
        //  LocalConnection
        Connection con = null;
        if( CVSRoot.METHOD_PSERVER.equals(cvsroot.getMethod()) ){
            con = new PServerConnection(cvsroot);
        }
        else if( CVSRoot.METHOD_LOCAL.equals(cvsroot.getMethod()) ){
            LocalConnection lcon = new LocalConnection();
            lcon.setRepository( cvsroot.getRepository() );
            con = lcon;
        }
        return con;
    }

    private String getEndDate(Object endRev){
        Date end=null;

        if( endRev==null ){
            end = Calendar.getInstance().getTime();
        }
        else{
            end = (Date)endRev;
        }

        return mDateFormat.format(end);
    }

    public void setWorkingCopy( String workingCopy ){
        mWorkingCopy = workingCopy;
    }

    public String getWorkingCopy() {
        if( mWorkingCopy == null ){
            throw new RuntimeException( "No value for working copy parameter.");
        }
        return mWorkingCopy;
    }
}



