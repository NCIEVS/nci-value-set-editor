/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-value-set-editor/LICENSE.txt for details.
 */

package gov.nih.nci.evs.valueseteditor.utilities;


import java.util.*;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.apache.log4j.*;
import org.LexGrid.valueSets.ValueSetDefinition;


import gov.nih.nci.evs.valueseteditor.beans.*;




/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */

public class SortComparator implements Comparator<Object> {
    private static Logger _logger = Logger.getLogger(SortComparator.class);
    private static final int SORT_BY_NAME = 1;
    private static final int SORT_BY_CODE = 2;
    private int _sort_option = SORT_BY_NAME;

    public SortComparator() {

    }

    public SortComparator(int sort_option) {
        _sort_option = sort_option;
    }

    private String getKey(Object c, int sort_option) {
        if (c == null)
            return "NULL";
        if (c instanceof org.LexGrid.concepts.Entity) {
            org.LexGrid.concepts.Entity concept =
                (org.LexGrid.concepts.Entity) c;
            if (sort_option == SORT_BY_CODE)
                return concept.getEntityCode();
            if (concept.getEntityDescription() == null)
                return null;
            return concept.getEntityDescription().getContent();
        }

        else if (c instanceof AssociatedConcept) {
            AssociatedConcept ac = (AssociatedConcept) c;
            if (sort_option == SORT_BY_CODE)
                return ac.getConceptCode();
            if (ac.getEntityDescription() == null)
                return null;
            return ac.getEntityDescription().getContent();
        }

        else if (c instanceof ResolvedConceptReference) {
            ResolvedConceptReference ac = (ResolvedConceptReference) c;
            if (sort_option == SORT_BY_CODE)
                return ac.getConceptCode();

            if (ac.getEntityDescription() == null) {
                _logger.warn("WARNING: ac.getEntityDescription() == null");
                return null;
            }
            return ac.getEntityDescription().getContent();
        }
/*
        else if (c instanceof TreeItem) {
            TreeItem ti = (TreeItem) c;
            if (sort_option == SORT_BY_CODE)
                return ti._code;
            return ti._text;
        }
*/
        else if (c instanceof ValueSetDefinition) {
            ValueSetDefinition vsd = (ValueSetDefinition) c;
            if (sort_option == SORT_BY_CODE)
                return vsd.getValueSetDefinitionURI();
            return vsd.getValueSetDefinitionName();
        }

        else if (c instanceof ValueSetBean.ComponentObject) {
            ValueSetBean.ComponentObject co = (ValueSetBean.ComponentObject) c;
            return co.getLabel();
        }


        else if (c instanceof String) {
            String s = (String) c;
            return s;
        }


        return c.toString();
    }

    public int compare(Object object1, Object object2) {
        // case insensitive sort
        String key1 = getKey(object1, _sort_option);
        String key2 = getKey(object2, _sort_option);

        if (key1 == null || key2 == null)
            return 0;
        key1 = getKey(object1, _sort_option).toLowerCase();
        key2 = getKey(object2, _sort_option).toLowerCase();

        return key1.compareTo(key2);
    }
}
