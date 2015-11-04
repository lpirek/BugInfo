/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.storage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public class DBBugSaver extends BugSaver{

    private int mProjId;
    private int mVerId;
    private Connection mConn;
    
    public DBBugSaver( Properties config) throws ClassNotFoundException, SQLException {
        String dbUrl = config.getDburl();
        String login = config.getDblogin();
        String pass = config.getDbpass();
        String projectName = config.getDbprojname();
        String VersionNumber = config.getDbvernumber();
        Class.forName("com.mysql.jdbc.Driver");
        mConn = DriverManager.getConnection(dbUrl, login, pass);
        
        Statement stmt = mConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet proj = stmt.executeQuery( "select ID from Project where Name = \"" + projectName + "\"" );
        mProjId = getIdFromResultSet(proj, "project");

        ResultSet ver = stmt.executeQuery("select ID from Version where Number = \"" + VersionNumber + "\" and ProjectId = " + String.valueOf(mProjId) );
        mVerId = getIdFromResultSet(ver, "version");

        mWriteModifications = config.readNumberOfModifications();
        mWriteCommiters = config.readNumberOfCommiters();
        mWriteModifiedLines = config.readNumberOfModifiedLines();
        mWriteBugs = config.readNumberOfBugs();

        removeOldBugInfo();
    }

    /** Sets bug for class with given ID. */
    private void alterBugInfoInDbUpdate(int classId, Double bug, Double modif, Double commit, Double modifiedLines, Double eveningRevisions) throws SQLException {
        Statement stmt = mConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("select ID, bug, revisions, commiters, modifiedLines, eveningRevisions  from Class where ID = " + String.valueOf(classId) );
        rs.next();
        if( mWriteBugs )
            rs.updateDouble("bug", bug);
        if( mWriteModifications )
            rs.updateDouble("revisions", modif);
        if( mWriteCommiters )
            rs.updateDouble("commiters", commit);
        if( mWriteModifiedLines )
            rs.updateDouble("modifiedLines", modifiedLines );
        rs.updateDouble("eveningRevisions", eveningRevisions);
        rs.updateRow();
    }

    private boolean alterBugInfoInDbGetId(String className, Double bug, Double modif, Double commit, Double modifiedLines, Double eveningRevisions){
        try {
            Statement stmt = mConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery( "select ID from Class where VersionID = "+String.valueOf(mVerId)+" and Name = \""+className+"\"");
            int classId;
            if( rs.next() ){
                classId = rs.getInt("ID");
                if( rs.next() ){
                    Logger.getLogger(  this.getClass().getName() ).log(Level.WARNING, "In current Version of Project are more than one class with name: "
                            + className +"\n\tIt is possible, that the bug info has been altered for wrong class!" );
                }
                alterBugInfoInDbUpdate( classId, bug, modif , commit, modifiedLines, eveningRevisions);
            }
            else{
                return false; //Class with given className hasn't been found.
            }
        } catch (SQLException se) {
            Logger.getLogger(  this.getClass().getName() ).log(Level.SEVERE, "SQL exception during altering bug info: " + se.getMessage() );
            return false;
        }
        return true;
    }
    
    
    private int getIdFromResultSet( ResultSet rs, String entity) throws SQLException{
       int id=0;
        
        if( rs.next() ){
            id = rs.getInt("ID");
            if( rs.next() ){
                closeConnectionWithException("There are more than one " + entity + " with given name!");
            }
        }
        else{
            closeConnectionWithException("There are no " + entity + " with given name!");
        }
       return id;
    }
    
    
    private void closeConnectionWithException(String exception) throws SQLException{
        mConn.close();
        throw new SQLException(exception);
    }

    /**
     * 
     * @param bugInfo String - class name, Double - number of bugs
     * @return Names of class, that wasn't been found in DB
     */
    @Override
    public List<String> save( ProjectInfo bugInfo ){
        Set<String> keys = bugInfo.getClassNames();
        Iterator<String> itr = keys.iterator();
        List<String> result = new LinkedList<String>();
        
        while( itr.hasNext() ){
            String className = itr.next();
            ClassInfo currentClass = bugInfo.getClassByName( className );
            if( !alterBugInfoInDbGetId(className,
                    new Double(currentClass.getNumOfBugs()),
                    new Double(currentClass.getNumOfModifications()),
                    new Double(currentClass.getNumOfCommiters()), 
                    new Double(currentClass.getNumOfModifiedLines()),
                    new Double(currentClass.getNumOfEveningRevisions()))
                    ){
                result.add(className);
            }
        }

        closeConnection();
        return result;
    }
    
    private void closeConnection(){
        try {
            mConn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBBugSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void removeOldBugInfo() throws SQLException {
        Statement stmt = mConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("select ID, bug, revisions, commiters, modifiedLines, eveningRevisions from Class where (bug <> 0  or Class.revisions<>0 or Class.commiters<>0 or Class.modifiedLines<>0) and VersionID = " + String.valueOf(mVerId) );
        while( rs.next() ){
            if( mWriteBugs )
                rs.updateDouble("bug", 0.0);
            if( mWriteModifications )
                rs.updateDouble("revisions", 0.0);
            if( mWriteCommiters)
                rs.updateDouble("commiters", 0.0);
            if( mWriteModifiedLines )
                rs.updateDouble("modifiedLines", 0.0);
            rs.updateDouble("eveningRevisions", 0.0);
            rs.updateRow();
        }
    }
}
