<%
metamodel http://www.eclipse.org/emf/2002/Ecore
%>

<%script type="ecore.EClass" name="xhtml" file="<%name%>.xhtml"%>


<html>
    <head>
        <title>TP3 - Acceleo</title>
    </head>
    <body>
        <h1>Description de la classe: <%name%></h1>
        <%if (eAllAttributes().length > 0) {%>
	        <p>Liste des attributs :</p>
	        <ul>
	        <%for (eAllAttributes()) {%>
	            <li><%eAttributeType().name%> <%name%></li>
	        <%}%>
	        </ul>
        <%}%>
        <%if (eAllOperations().length > 0) {%>
	        <p>Liste des opérations :</p>
	        <ul>
	        <%for (eAllOperations()) {%>
	            <li><%eType().name%> <%name%></li>
	        <%}%>
	        </ul>
        <%}%>
        <%if (eAllReferences().length > 0) {%>
	        <p>Liste des références :</p>
	        <ul>
	        <%for (eAllReferences()) {%>
	            <li><%eType().name%> <%name%></li>
	        <%}%>
	        </ul>
        <%}%>
    </body>
</html>