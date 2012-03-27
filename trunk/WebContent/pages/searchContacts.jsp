<%@page import="com.uofc.roomfinder.entities.ContactList"%>
<%@page import="com.uofc.roomfinder.entities.Contact"%>
<%@page import="com.uofc.roomfinder.util.UrlReader"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK href="/UofC_Roomfinder_Server/ressources/css/style.css" rel="stylesheet" type="text/css">
<title>Contact search</title>
</head>
<body>
	<div class="linkbox">
		<table id="tabledesign">
			<thead>
				<th>EXAMPLE SEARCHES</th>
			</thead>
			<tbody>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Frank">./searchContacts.jsp?searchString=Frank</a>
					</td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Maurer">./searchContacts.jsp?searchString=Maurer</a>
					</td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=maurer">./searchContacts.jsp?searchString=maurer</a>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Frank%20Maurer">./searchContacts.jsp?searchString=Frank
							Maurer</a>
					</td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Maurer%20Frank">./searchContacts.jsp?searchString=Maurer
							Frank</a></td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Fra%20Mau">./searchContacts.jsp?searchString=Fra Mau</a>
					</td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=ICT%20550">./searchContacts.jsp?searchString=ICT 550</a>
					</td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=ICT550">./searchContacts.jsp?searchString=ICT550</a></td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Information%20550">./searchContacts.jsp?searchString=Information
							550</a>
					</td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Information%20Communication%20550">./searchContacts.jsp?searchString=Information
							Communication 550</a></td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=Information%20550">./searchContacts.jsp?searchString=Information
							550</a></td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=math%20680">./searchContacts.jsp?searchString=math 680</a></td>
				</tr>
				<tr>
					<td><a href="/UofC_Roomfinder_Server/pages/searchContacts.jsp?searchString=680%20math">./searchContacts.jsp?searchString=680 math</a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="searchbox">
		<table id="tabledesign">
			<thead>
				<th>LDAP DIRECTORY LOOKUP</th>
			</thead>
			<tbody>
				<tr>
					<td><form method="get">
							<input style="width: 200px;" type="text" name="searchString" value="<%=request.getParameter("searchString")%>" /> <input type="submit"
								name="search"
							/>
						</form>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="searchbox">
		<table id="tabledesign">
			<thead>
				<th>PRENAME</th>
				<th>SURNAME</th>
				<th>TELEPHONE</th>
				<th>ROOM</th>
			</thead>
			<tbody>
				<%
					if (request.getParameter("searchString") != null) {

						ContactList contacts = new ContactList(UrlReader.readFromURL("http://localhost:8080/UofC_Roomfinder_Server/rest/contact/name/"
								+ request.getParameter("searchString")));

						for (Contact contact : contacts) {
				%>
				<tr>
					<td><%=contact.getPreName()%></td>
					<td><%=contact.getSurName()%></td>
					<td><%=contact.getTelephoneNumbers().toString()%></td>
					<td><%=contact.getRoomNumber().toString()%></td>
				</tr>
				<%
					}
					}
				%>
			</tbody>
		</table>
	</div>
</body>
</html>