/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.wroc.pwr.buginfo.repo;

import java.util.Iterator;
import org.netbeans.lib.cvsclient.event.*;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public class CVSLogListener extends AbstractRepoLogListener {

    /**
     * Stores a tagged line
     */
    private ProjectInfo mResults = null;
    private String mFileName;
    private final static String mDefaultFileName = "unknown";
    private boolean mIsClass = false;
    private boolean mIsNewRevision = false;
    private double mNumberOfBugs = 0;
    private long mNumberOfModifications = 0;
    private String mWorkingcopy = null;
    private ClassInfo mClassInfo=null;

    public CVSLogListener(ProjectInfo results, String bugFixPattern, String classPrefix, String classPostfix, char fileSeparator, Properties config) {
        super(results, bugFixPattern, classPrefix, classPostfix, fileSeparator);
        mResults = results;
        mFileName = mDefaultFileName;
        mReadModifications = config.readNumberOfModifications();
        mReadCommiters = config.readNumberOfCommiters();
        mReadModifiedLines = config.readNumberOfModifiedLines();
        mReadBugs = config.readNumberOfBugs();
    }

    public void setWorkingcopy(String repo){
        mWorkingcopy = repo;
    }

    public String getWorkingcopy(){
        return mWorkingcopy;
    }
    /**
     * Called when the server wants to send a message to be displayed to
     * the user. The messagpublic class CVSLogListener
     * is only for information purposes and clients
     * can choose to ignore these messages if they wish.
     * @param e the event
     */
    @Override
    public void messageSent(MessageEvent e) {
        handleError(e);
        String line = e.getMessage();
//        System.out.println("LOG: " + line);
//TODO: The order of if construction may be inportant. Lines that returns true in more than one if may be interpreted incorectly. But it shouldn't happened.    
        if (isFileName(line)) {
            handleNewFileBegin(line);
            return;
        }

        if (isNewRevision(line)) {
            handleNewRevision();
            return;
        }

        if(isCommiterLine(line)){
            handleCommiterLine(line);
            handleCommitTime(line);
            return;
        }

        if (isBugFix(line)) {
            handleBugFix();
            return;
        }

        if (isNewFile(line)) {
            handleNewFileEnd(); //File description (log) has a header before the first revision
            return;
        }

    }

    private void handleBugFix() {
        if (mIsNewRevision && mReadBugs) {
            mNumberOfBugs += 1;
            mIsNewRevision = false;
        }
    }

    private void handleNewFileBegin(String line) {
        if (isPathToClass(line)) {
            mFileName = line;
            mClassInfo = new ClassInfo(super.convertPathToClassName(mFileName));
            mIsClass = true;            
        }
    }

    private void handleNewFileEnd() {
        if (mFileName.compareTo(mDefaultFileName) != 0 && mIsClass && (mNumberOfBugs > 0 || (mReadModifications && mNumberOfModifications > 0))) {
            mFileName = super.convertPathToClassName(mFileName);
            mResults.addBugs(mFileName, (long) mNumberOfBugs);
            if (mReadModifications) {
                mResults.addModifications(mFileName, mNumberOfModifications);
            }
            if(mReadCommiters){
                Iterator<String> itr = mClassInfo.getCommiters().iterator();
                while( itr.hasNext() ){
                    mResults.addCommiter(mFileName, itr.next() );
                }
            }
            if(mReadModifiedLines){
                mResults.addModifiedLines(mFileName, mClassInfo.getNumOfModifiedLines());
            }
        }
        mFileName = mDefaultFileName;
        mIsClass = false;
        mNumberOfBugs = 0;
        mNumberOfModifications = 0;
        mIsNewRevision = false; //File description (log) has a header before the first revision
    }

    private void handleNewRevision() {
        mNumberOfModifications++;
        mIsNewRevision = true;
    }

    private boolean isFileName(String line) {
        return line.startsWith("Working file:");
    }

    /** CVS uses in logs a line that consists of '=' as a file separator. */
    private boolean isNewFile(String line) {
        return line.matches("=+");
    }

    private boolean isNewRevision(String line) {
        return line.matches("-+");
    }

    private void handleError(MessageEvent e) {
        if (e.isError()) {
            System.err.println(e.getMessage());
        }
    }

    private boolean isCommiterLine(String line) {
        return line.matches("date.*author.*");
    }

    private void handleCommiterLine(String line) {
        if( mIsClass && mReadCommiters ){
            String[] lineParts = line.split("author: ");
            lineParts = lineParts[1].split(";");
            mClassInfo.addCommiter( lineParts[0] );
        }
        if( mIsClass && mReadModifiedLines ){
            String[] lineParts = line.split("lines: ");
            if( lineParts.length>1 ){
                lineParts = lineParts[1].split(" ");
                if( lineParts[0].startsWith("+") ){
                    lineParts[0]=lineParts[0].substring(1);
                }
                long added = Long.parseLong(lineParts[0]);
                long removed = Math.abs( Long.parseLong(lineParts[1]) );
                mClassInfo.addNumOfModifiedLines( added+removed );
            }
        }
    }

    private void handleCommitTime(String line) {
        if( mIsClass ){
            String hourSubstring = line.substring(17, 19);
            int hour = Integer.parseInt(hourSubstring);
            int eveningHour = 16;

            if( hour >= eveningHour ){
                mResults.addEveningRevisions(mClassInfo.getName(), 1);
            }
        }
    }
/*
    @Override
    public void fileInfoGenerated(FileInfoEvent e) {
      FileInfoContainer fileInfo = e.getInfoContainer();

      if (fileInfo.getClass().equals(LogInformation.class)) {
            LogInformation info = (LogInformation)fileInfo;
            String fileName = getFileNameWithoutWorkingcopyPath(info.getFile().getAbsolutePath());

            Logger.getLogger("File from CVS").logp(Level.INFO, "", "", fileName );
      }
    }

    private String getFileNameWithoutWorkingcopyPath(String workingcopyFileName){
        if( getWorkingcopy() == null ){
            return workingcopyFileName;
        }
        else{
            String workingcopy = getWorkingcopy().replace("\\", "\\\\");
            return workingcopyFileName.replaceFirst( workingcopy+"([/(\\\\)])+", "");
        }
    }

        @Override
    public void fileInfoGenerated(FileInfoEvent e) {

        if (e != null) {
//            LogInformation f = (LogInformation) e.getInfoContainer();
//            //get file details
//
//            System.out.println( "######################");
//            System.out.println( "###1 " + f.getRepositoryFilename());
//            System.out.println( "###2 " + f.toString());
//            System.out.println( "###3 " + f.getAccessList());
//            System.out.println( "###4 " + f.getBranch());
//            System.out.println( "###5 " + f.getDescription());
//            System.out.println( "###6 " + f.getHeadRevision());
//            System.out.println( "###7 " + f.getKeywordSubstitution());
//            System.out.println( "###8 " + f.getLocks());
//            System.out.println( "###9 " + f.getSelectedRevisions());
//            System.out.println( "###10 " + f.getTotalRevisions());
//            System.out.println( "######################");
//            System.out.println( "");

        } else {
//            System.out.println( "### CVS Log Listener: Missing file info" );

        }
    }*/
}
