package com.uofc.roomfinder.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.uofc.roomfinder.dao.ContactDAO;
import com.uofc.roomfinder.dao.ContactDAOImpl;
import com.uofc.roomfinder.dao.UserDAOImpl;
import com.uofc.roomfinder.entities.ContactList;

@Path("/friend")
public class FriendManager {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/add")
	public String addFriend(@PathParam("user_name") String userName, @PathParam("friend_name") String friendName) {

		// search directory for names and buildings
		boolean check = new UserDAOImpl().addFriend(userName, friendName);

		// return result
		if (check)
			return "success";
		else
			return "error";
	}

}
