/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.storage;

import java.util.List;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author mjureczk
 */
public abstract class  BugSaver {
    protected boolean mWriteModifications;
    protected boolean  mWriteCommiters;
    protected boolean mWriteModifiedLines;
    protected boolean mWriteBugs;

    abstract public List<String> save( ProjectInfo bugInfo );
}
