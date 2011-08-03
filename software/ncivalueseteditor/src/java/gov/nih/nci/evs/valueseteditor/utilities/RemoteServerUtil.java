package gov.nih.nci.evs.valueseteditor.utilities;

import java.util.*;

import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Impl.*;

import gov.nih.nci.system.client.*;

import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.evs.valueseteditor.properties.ApplicationProperties;
import org.apache.log4j.*;

import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 *     Modification history Initial implementation kim.ong@ngc.com
 *
 */
public class RemoteServerUtil {
	private static Logger _logger = Logger.getLogger(RemoteServerUtil.class);
	private static boolean _debug = false;
	public static final String SEPARATOR =
		"----------------------------------------" +
		"----------------------------------------";

	/**
	 * Constructor
	 */
	public RemoteServerUtil() {
		// Do nothing
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static LexBIGService createLexBIGService() { //throws Exception {
		String url = null;
		try {
			url = ApplicationProperties.getServiceurl();
			return createLexBIGService(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * @param serviceUrl
	 * @return
	 */
	private static LexBIGService createLexBIGService(String serviceUrl) {
		try {
			if (serviceUrl == null || serviceUrl.compareTo("") == 0) {
				String lg_config_file = ApplicationProperties.getLgconfigfile();
				System.setProperty(ApplicationProperties.LG_CONFIG_FILE,
						lg_config_file);
				LexBIGService lbSvc = new LexBIGServiceImpl();
				return lbSvc;
			}
			if (_debug) {
				_logger.debug(SEPARATOR);
				_logger.debug("LexBIGService(remote): " + serviceUrl);
			}

			LexEVSApplicationService lexevsService = (LexEVSApplicationService) ApplicationServiceProvider
					.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
			lexevsService = registerAllSecurityTokens(lexevsService);
			return (LexBIGService) lexevsService;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param lexevsService
	 * @return
	 * @throws Exception
	 */
	public static LexEVSApplicationService registerAllSecurityTokens(
			LexEVSApplicationService lexevsService) throws Exception {
		HashMap<String,String> map = ApplicationProperties.getSecurityTokenMap();
		map.entrySet().iterator();

		for (Map.Entry<String, String> entry: map.entrySet()) {
			lexevsService = registerSecurityToken(lexevsService,
					entry.getKey(), entry.getValue());

		}
		return lexevsService;
	}

	/**
	 * @param lexevsService
	 * @param codingScheme
	 * @param token
	 * @return
	 */
	public static LexEVSApplicationService registerSecurityToken(
			LexEVSApplicationService lexevsService, String codingScheme,
			String token) {
		SecurityToken securityToken = new SecurityToken();
		securityToken.setAccessToken(token);
		Boolean retval = null;
		try {
			retval = lexevsService.registerSecurityToken(codingScheme,
					securityToken);
			/*
			if (retval != null && retval.equals(Boolean.TRUE)) { //
				_logger.debug("Registration of SecurityToken was successful.");
			} else {
				_logger.error("WARNING: Registration of SecurityToken failed.");
			}
			*/
		} catch (Exception e) {
			_logger.error("WARNING: Registration of SecurityToken failed.");
		}
		return lexevsService;
	}



    public static LexEVSDistributed getLexEVSDistributed() {
		String url = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";
		ApplicationProperties properties = null;
		try {
            properties = ApplicationProperties.getInstance();
            url = properties.getServiceurl();
            return getLexEVSDistributed(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
	}


    public static LexEVSDistributed getLexEVSDistributed(String serviceUrl) {
		try {
			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

			return distributed;
		} catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}


    public static LexEVSValueSetDefinitionServices getLexEVSValueSetDefinitionServices() {

		ApplicationProperties properties = null;
		try {
            properties = ApplicationProperties.getInstance();
            String serviceUrl = properties.getServiceurl();
            if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareToIgnoreCase("null") == 0) {
				return LexEVSValueSetDefinitionServicesImpl.defaultInstance();
			}
			LexEVSDistributed distributed = getLexEVSDistributed();
			LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();
			return vds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
	}


    public static LexEVSValueSetDefinitionServices getLexEVSValueSetDefinitionServices(String serviceUrl) {
		try {
			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

			LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();
			return vds;
		} catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}




} // End of RemoteServerUtil