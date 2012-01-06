<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>


<FORM NAME="paginationForm" METHOD="POST" action="<%=request.getContextPath() %>/pages/search_results.jsf?" >
  <table>
    <tr>
      <td class="textbody" align=left>
    
<%    

String search_key = (String) request.getAttribute("key");
request.setAttribute("key", search_key);
%>
<input type="hidden" id="key" name="key" value="<%=key%>" />
<input type="hidden" id="dictionary" name="dictionary" value="<%=search_results_dictionary%>" />
<input type="hidden" id="version" version="key" value="<%=search_results_version%>" />
<%

/*
IteratorBeanManager iteratorBeanMgr = (IteratorBeanManager) FacesContext.getCurrentInstance().getExternalContext()
.getSessionMap().get("iteratorBeanManager");

IteratorBean itrBean = iteratorBeanMgr.getIteratorBean(search_key);
*/
IteratorBean itrBean = (IteratorBean) request.getSession().getAttribute("iteratorBean");


%>
       <b>Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%></b>
        
      </td>
      <td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
      <td class="textbody" align=right>
        <%
          if (page_num > 1) {
        %>
        &nbsp;
        <i>
          <a href="<%=request.getContextPath() %>/pages/search_results.jsf?page_number=<%=prev_page_num_str%>">Prev</a>
        </i>&nbsp;
        <%
          }
          if (num_pages > 1) {
          
          
          int sliding_window_start = 1;
          int sliding_window_end = num_pages;
          int sliding_window_half_width = 10;//NCItBrowserProperties.getSlidingWindowHalfWidth();
          
          sliding_window_start = page_num - sliding_window_half_width;
          if (sliding_window_start < 1) sliding_window_start = 1;
          
          sliding_window_end = sliding_window_start + sliding_window_half_width * 2 - 1;
          if (sliding_window_end > num_pages) sliding_window_end = num_pages;
       
          
		  //for (int idx=1; idx<=num_pages; idx++) {
		 for (int idx=sliding_window_start; idx<=sliding_window_end; idx++) { 
		    String idx_str = Integer.toString(idx);
		    if (page_num != idx) {
		      %>
			<a href="<%=request.getContextPath() %>/pages/search_results.jsf?page_number=<%=idx_str%>"><%=idx_str%></a>
			&nbsp;
		      <%
		    } else {
		      %>
			<%=idx_str%>&nbsp;
		      <%
		    }
		  }
          }
          
          if (num_pages > 1 && next_page_num <= num_pages) {
        %>
          &nbsp;
          <i>
            <a href="<%=request.getContextPath() %>/pages/search_results.jsf?page_number=<%=next_page_num_str%>">Next</a>
          </i>
        <%
          }
        %>
          </td>
    </tr>
    <tr>
      <td class="textbody" align=left>
        Show
  
  <select name=resultsPerPage size=1 onChange="paginationForm.submit();">
  <%
    
    List resultsPerPageValues = UserSessionBean.getResultsPerPageValues();
    
    for (int i=0; i<resultsPerPageValues.size(); i++) {
        String resultsPerPageValue = (String) resultsPerPageValues.get(i);
        
        if (selectedResultsPerPage.compareTo(resultsPerPageValue) == 0) {
  %>      
        <option value="<%=resultsPerPageValue%>" selected><%=resultsPerPageValue%></option>
  <%        
        
        } else {
  %>      
        <option value="<%=resultsPerPageValue%>"><%=resultsPerPageValue%></option>
  <%        
        }

  }
  %>
  </select>
    
        &nbsp;results per page
        
      </td>
      <td>
        &nbsp;&nbsp;
      </td>
      <td>
        &nbsp;
      </td>
    </tr>
  </table>
</form>