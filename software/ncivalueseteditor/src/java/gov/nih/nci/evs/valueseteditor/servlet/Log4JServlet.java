/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 */

public class Log4JServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    static boolean isInit = false;

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() throws ServletException {

        String logfile = System.getProperty("NCIvselog4jProperties");
        System.out.println("VSE Logger prop file = [" + logfile + "]");

        if(!isInit) {
            try{
                InputStream log4JConfig = new FileInputStream(logfile);
                Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(log4JConfig);

                DOMConfigurator.configure( doc.getDocumentElement() );
                isInit = true;
            }
            catch(ParserConfigurationException e){
                System.out.println( "*** Error: Failed to parse log4j configuration file!" );
                e.printStackTrace();
            }
            catch(SAXException e){
                System.out.println( "*** SAX Error: Failed to parse log4j configuration file!" );
                e.printStackTrace();
            }
            catch( IOException e){
                System.out.println( "*** IO Error: log4j configuration file!" );
                e.printStackTrace();
            }
        }

    }

} // End Log4JServlet
