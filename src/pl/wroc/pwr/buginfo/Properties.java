package pl.wroc.pwr.buginfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marian
 */
public class Properties {
    
    java.util.Properties mProps;
    
    public Properties() throws IOException{
        init( "buginfo.properties" );
    }
    
    public Properties( String fileName ) throws IOException{
        init( fileName );
    }

    public Object getRepoendrev() {
        String res = getProperty("repoendrev");
        if( res == null){ //TODO: Refactoring - the same code in Repostartrev
            return null;
        }
        if( getRepotype().compareToIgnoreCase("svn") == 0 ){
            return new Long(Long.parseLong(res));
        }
        else if( getRepotype().compareToIgnoreCase("cvs") == 0 ){
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date dat = df.parse(res);
                return dat;
            } catch (ParseException ex) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                    Date dat = df.parse(res);
                    return dat;
                } catch (ParseException ex1) {
                    Logger.getLogger(Properties.class.getName()).log(Level.WARNING, null, ex1);
                    return null;
                }
            }


        }
        return null;
    }

    public String getRepoSvnPass() {
        return getProperty("reposvnpass");
    }

    public String getRepoSvnUser() {
        return getProperty("reposvnuser");
    }

    public String getRepoworkingcopy() {
        return getProperty("repoworkingcopy");
    }
        
    void init( String fileName ) throws IOException{    
        mProps = new java.util.Properties();
        FileInputStream in = new FileInputStream( fileName );
        mProps.load( in );
    } 

    public String getRepotype(){
        return getProperty("repotype");
    }
    
    public String getRepourl(){
        return getProperty("repourl");
    }
    
    public Object getRepostartrev(){
        String res = getProperty("repostartrev");
        if( res == null || res.compareTo("") == 0){
            return null;
        }
        if( getRepotype().compareToIgnoreCase("svn") == 0 ){
            return new Long(Long.parseLong(res));
        }
        else if( getRepotype().compareToIgnoreCase("cvs") == 0 ){
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date dat = df.parse(res);
                return dat;
            } catch (ParseException ex) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                    Date dat = df.parse(res);
                    return dat;
                } catch (ParseException ex1) {
                    Logger.getLogger(Properties.class.getName()).log(Level.WARNING, null, ex1);
                    return null;
                }
            }

            
        }
        return null;
    }   
    
    public String getRepobugfixregex(){
        return getProperty("repobugfixregex");
    }
    
    public String getReposrcpathprefixregex(){
        return getProperty("reposrcpathprefixregex");
    }
    
    public String getReposrcpathpostfixregex(){
        return getProperty("reposrcpathpostfixregex");
    }
    
    public Character getRepofileseparator(){
        String res = getProperty("repofileseparator");
        if( res == null || res.compareTo("") == 0 ){
            return null;
        }
        return new Character(res.charAt(0));
    }
    
    public String getDburl(){
        return getProperty("dburl");
    }
    
    public String getDblogin(){
        return getProperty("dblogin");
    }
    
    public String getDbpass(){
        return getProperty("dbpass");
    }
    
    public String getDbprojname(){
        return getProperty("dbprojname");
    }
    
    public String getDbvernumber(){
        return getProperty("dbvernumber");
    }

    public String getSavedestination(){
        String saveDestination = getProperty("savedestination");
	if( saveDestination == null ){
		throw new RuntimeException("No save destination.");
	}
	return saveDestination;
    }
    
    public String getCsvfilename(){
        return getProperty("csvfilename" );
    }
    
    public String getXMLfilename(){
        return getProperty("xmlfilename" );
    }

    public boolean readNumberOfModifications(){
        String res = mProps.getProperty("readnumberofmodifications");
        return handleBoolean(res);
    }

    public boolean readNumberOfCommiters(){
        String res = mProps.getProperty("readnumberofcommiters");
        return handleBoolean(res);
    }

    public boolean readNumberOfModifiedLines(){
        String res = mProps.getProperty("readnumberofmodifiedlines");
        return handleBoolean(res);
    }
    

    public boolean readNumberOfBugs(){
        String res = mProps.getProperty("readnumberofbugs");
        return handleBoolean(res);
    }

    private String getProperty(String key){
        String res = mProps.getProperty(key);
        if( res == null || res.compareTo("") == 0 ){
            return null;
        }
        return res;
    }

    private boolean handleBoolean(String res) {
        if (res != null && res.compareToIgnoreCase("true") == 0) {
            return true;
        } else {
            return false;
        }
    }


}
