/**
 * @since 03/03/2010
 * @author Marco Aurelio Sene Santos
 */
package br.com.portalnet.util;

import java.io.File;


public class FileUtil {

	public static boolean delete(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i=0; i<children.length; i++) {
                boolean success = delete(new File(file, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        return file.delete();
    }
    
}
