<%
metamodel http://www.eclipse.org/emf/2002/Ecore
%>

<%script type="ecore.EPackage" name="sql" file="<%name%>.sql"%>


<%for (eAllContents().filter("EClass")) {%>
    CREATE TABLE <%name%> VALUES(
    <%for (eAllContents().filter("EAttribute")) {%>
        <%name%> VARCHAR(255),
    <%}%>
    );
<%}%>