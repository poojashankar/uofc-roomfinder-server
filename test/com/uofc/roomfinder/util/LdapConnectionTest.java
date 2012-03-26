package com.uofc.roomfinder.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.directory.*;

import java.util.*;

import junit.framework.Assert;

public class LdapConnectionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	final static String ldapServerName = "directory.ucalgary.ca";
	final static String rootContext = "o=ucalgary.ca Scope=LDAP_SCOPE_SUBTREE";

	@Test
	public void testLdapConnection() {

		// set up environment to access the server
		Properties env = new Properties();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + ldapServerName + "/" + rootContext);

		try {
			// obtain initial directory context using the environment
			DirContext ctx = new InitialDirContext(env);
	
			SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            
            NamingEnumeration<SearchResult> enumeration = ctx.search( "" , "cn=Frank*Maurer", ctrl);
            String answer = "";
            
            while (enumeration.hasMore()) {
                SearchResult result = (SearchResult) enumeration.next();
                Attributes attribs = result.getAttributes();
                NamingEnumeration<? extends Attribute> values = attribs.getAll();
                while (values.hasMore()) {                 
                  answer += values.next().toString();
                }
            }
			Assert.assertTrue(answer.contains("Frank Maurer"));
			Assert.assertTrue(answer.contains("ICT550"));
            
		} catch (NameAlreadyBoundException nabe) {
			fail();
			System.err.println("value has already been bound!");
		} catch (Exception e) {
			fail();
			System.err.println(e);
		}
	}

}
