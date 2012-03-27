package com.uofc.roomfinder.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ContactListTest {

	final String JSON_STRING = "[" + "  {" + "    \"preName\": \"Frank\"," + "    \"surName\": \"Maurer\"," + "    \"commonName\": \"Frank Maurer\","
			+ "    \"emails\": [" + "      \"maurer@cpsc.ucalgary.ca\"," + "      \"fmaurer@ucalgary.ca\"" + "    ]," + "    \"telephoneNumbers\": ["
			+ "      \"+1 (403) 220-3531\"," + "      \"+1 (403) 220-7016\"" + "    ]," + "    \"roomNumber\": [" + "      \"ICT550\"," + "      \"A100\""
			+ "    ]," + "    \"departments\": [" + "      \"Computer Science, Department\"," + "      \"EXECUTIVE SUITE\"" + "    ]" + "  }," + "  {"
			+ "    \"preName\": \"Christian\"," + "    \"surName\": \"Maurer\"," + "    \"commonName\": \"Christian Maurer\"," + "    \"emails\": ["
			+ "      \"cmaurer@ucalgary.ca\"" + "    ]," + "    \"telephoneNumbers\": []," + "    \"roomNumber\": []," + "    \"departments\": ["
			+ "      \"Faculty of Kinesiology\"" + "    ]" + "  }," + "  {" + "    \"preName\": \"Claudia\"," + "    \"surName\": \"Maurer\","
			+ "    \"commonName\": \"Claudia Maurer\"," + "    \"emails\": [" + "      \"ilabadm@ucalgary.ca\"" + "    ]," + "    \"telephoneNumbers\": ["
			+ "      \"+1 (403) 220-7791\"" + "    ]," + "    \"roomNumber\": [" + "      \"MS680A\"" + "    ]," + "    \"departments\": ["
			+ "      \"Computer Science, Department\"" + "    ]" + "  }" + "]";


	@Test
	public void testJsonConstructorAndToJsonString() {
		//create object out of JSON string		
		ContactList newContactList = new ContactList(JSON_STRING);
		
		//check if the JSON string of the created object is the same as the original string
		assertEquals(newContactList.toJsonString().replace("\n", "").replace(" ", ""), JSON_STRING.replace("\n", "").replace(" ", ""));
	}

}
