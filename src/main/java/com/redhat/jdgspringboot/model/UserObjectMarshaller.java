package com.redhat.jdgspringboot.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class UserObjectMarshaller implements MessageMarshaller<UserObject> {

	   @Override
	    public String getTypeName() {
	        return "jdgspringboot.UserObject";
	    }
	    @Override
	    public Class<? extends UserObject> getJavaClass() {
	        return UserObject.class;
	    }
	    @Override
	    public void writeTo(ProtoStreamWriter writer, UserObject user) throws IOException {
	        writer.writeString("userId", user.getUserId());
	        writer.writeString("name", user.getName());
	        //writer.writeCollection("authors", book.getAuthors(), Author.class);
	    }
	    @Override
	    public UserObject readFrom(ProtoStreamReader reader) throws IOException {
	        String userId = reader.readString("userId");
	        String name = reader.readString("name");
	        //int publicationYear = reader.readInt("publicationYear");
	        //Set<Author> authors = reader.readCollection("authors", 
	            //new HashSet<Author>(), Author.class);
	        UserObject user=new UserObject();
	        user.setUserId(userId);
	        user.setName(name);
	        return user;
	    }	
}
