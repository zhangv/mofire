/* kxmlrpc
 *
 * The contents of this file are subject to the Enhydra Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License
 * on the Enhydra web site ( http://www.enhydra.org/ ).
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific terms governing rights and limitations
 * under the License.
 *
 * The Initial Developer of kxmlrpc is Kyle Gabhart. Copyright (C) 2001 
 * Kyle Gabhart -- kyle.gabhart@enhydra.org . All Rights Reserved.
 *
 * Contributor(s): Stefan Haustein
 */

package org.kxmlrpc;

import java.io.*;
import java.util.*;

import org.kxml.*;
import org.kxml.parser.*;

import org.kobjects.isodate.IsoDate;
import org.kobjects.base64.Base64;

/**
 * This abstract base class provides basic XML-RPC parsing capabilities. The 
 * kxml parser is required by this class.
 */
public class XmlRpcParser {

    AbstractXmlParser   parser;
    String              methodName;
    Vector              params = new Vector();

    /**
     * @param parser    a kxml parser object reference
     */
    public XmlRpcParser( AbstractXmlParser parser ) {
	this.parser = parser;
    }//end XmlRpcParser( AbstractXmlParser )
    
    public String getMethodName() {
	return methodName;
    }//end getMethodName()

    public Vector getParams() {
	return params;
    }//end getParams()

    /*
    // This method is used to parse an incoming Client request
    //  kxmlrpc does not currently support this feature
    public void parseCall() throws IOException {
	parser.skip(); 
	parser.read( Xml.START_TAG, "", "methodCall" );

	parser.skip();
	parser.read( Xml.START_TAG, "", "methodName" );
        methodName = parser.readText();
	parser.read( Xml.END_TAG, "", "methodName" );

	parser.skip();

	if( parser.peek().getType() != Xml.END_TAG ) 
	    parseParams();

	parser.read( Xml.END_TAG, "", "methodCall" );
	parser.skip();
	parser.read( Xml.END_DOCUMENT, null, null );
    }//end parseCall()
    */
    
    /** 
     * Called by a client to parse an XML-RPC response returned by a server.
     *
     * @return The return parameter sent back by the server.
     */
    public Object parseResponse() throws IOException {
        ParseEvent      event;
        Object          result;
        
        parser.skip();
        //parser.peek(Xml.START_TAG, "", "html");
	parser.read( Xml.START_TAG, "", "methodResponse" );
	parser.skip();

	event = parser.peek();
	result = null;

	if( event.getType() == Xml.START_TAG ) {
	    // If an error occurred, the server will return a Fault
            if( "fault".equals( event.getName() ) ) {
		parser.read();
                // Fault's are returned as structs (which are mapped to Hashtables)
		Hashtable fault = (Hashtable) parseValue();
		parser.skip();
		parser.read( Xml.END_TAG, "", "fault" );
                // Ultimately, a client-side exception object is generated
		result = new XmlRpcException 
		    ( ( (Integer) fault.get( "faultCode" ) ).intValue(),
		     (String) fault.get( "faultString" ) );
	    }
            /* The current version of the XML-RPC spec -- http://www.xmlrpc.org/spec
                does not permit multiple parameter values to be returned, although
                a complex type (struct or array) containing multiple values (and even
                other complext types) is permitted.  This aspect of the spec is currently
                being debated and may be changed in the future. */
	    else if( "params".equals( event.getName() ) ) {
		parseParams();
		if( params.size() > 1 ) 
		    throw new IOException( "too many return parameters" );
		else if( params.size() == 1 ) 
		    result = params.elementAt(0);
	    }
	    else throw new IOException 
		( "<fault> or <params> expected instead of " + event );
	}//end if( event.getType() == Xml.START_TAG ) {

	parser.skip();
	parser.read( Xml.END_TAG, "", "methodResponse" );
	parser.skip();
	parser.read( Xml.END_DOCUMENT, null, null );

	return result;
    }//end parseResponse()

    /**
     * All data in an XML-RPC call is passed as a parameter. This method parses 
     * the parameter values out of each parameter by calling the parseValue() 
     * method. 
     */
    void parseParams() throws IOException {
	parser.read( Xml.START_TAG, "", "params" );
	parser.skip();
        
	while( parser.peek().getType() != Xml.END_TAG ) {
	    parser.read( Xml.START_TAG, "", "param" );
            // Retrieve a Java representation of the XML-RPC parameter value
	    params.addElement( parseValue() );
	    parser.skip();
	    parser.read( Xml.END_TAG, "", "param" );
	    parser.skip();
	}//end while( parser.peek().getType() != Xml.END_TAG )
	
	parser.read( Xml.END_TAG, "", "params" );
	parser.skip();
    }//end parseParams()

    /** 
     * Core method for parsing XML-RPC values into Java data types. It is called 
     * recursively by the parseStruct() and parseArray() methods when handling 
     * complex data types 
     * 
     * @return A Java representation of the XML-RPC value
     */
    Object parseValue() throws IOException {
	Object      result = null;
	parser.skip();
	parser.read( Xml.START_TAG, "", "value" );
	parser.skip();

	if( parser.peek().getType() != Xml.START_TAG ) 
	    result = parser.readText();
	else {
	    ParseEvent event = parser.read( Xml.START_TAG, "", null );
	    String name = event.getName();
	    if( name.equals("string") )
		result = parser.readText();
	    else if( name.equals("i4") || name.equals("int") )
		result = new Integer 
		    ( Integer.parseInt( parser.readText().trim() ) );
	    else if( name.equals("boolean") )
		result = new Boolean( parser.readText().trim().equals("1") );
	    else if( name.equals("dateTime.iso8601") ){
	    	String text = parser.readText();
	    	result = IsoDate.stringToDate(text, IsoDate.DATE_TIME );
	    }
	    else if( name.equals("base64") )
	    	result = Base64.decode( parser.readText() );
        else if( name.equals("struct") ) 
		result = parseStruct(); 
	    else if( name.equals("array") )
		result = parseArray();
            // kxmlrpc does not currently support the XML-RPC double data type
            //  the temporary workaround is to process double values as strings
            else if( name.equals("double") )
                result = parser.readText();

	    parser.read( Xml.END_TAG, "", name );
	    parser.skip();
	}//end if( parser.peek().getType() != Xml.START_TAG )

	parser.read( Xml.END_TAG, "", "value" );
	return result;
    }//end parseValue()

    /**
     * @return kxmlrpc maps XML-RPC structs to java.util.Hashtables
     */
    Hashtable parseStruct() throws IOException {
	Hashtable h = new Hashtable();
	parser.skip();
	
	while( parser.peek().getType() != Xml.END_TAG ) {
	    parser.read( Xml.START_TAG, "", "member" );
	    parser.skip();
	    parser.read( Xml.START_TAG, "", "name" );
	    String name = parser.readText();
	    parser.read( Xml.END_TAG, "", "name" );
	    parser.skip();
	    h.put( name, parseValue() ); // parse this member value
	    parser.skip();
	    parser.read( Xml.END_TAG, "", "member" );
	    parser.skip();
	}//end while( parser.peek().getType() != Xml.END_TAG )
	return h;
    }//end parseStruct()

    /**
     * @return kxmlrpc maps XML-RPC arrays to java.util.Vectors
     */
    Vector parseArray() throws IOException {
    	parser.read(Xml.START_TAG, "", "data");
    	parser.skip();
	Vector v = new Vector();
	parser.skip();

	while( parser.peek().getType() != Xml.END_TAG ) {
	    v.addElement( parseValue() ); // parse this element value
	    parser.skip();
	}//end while( parser.peek().getType() != Xml.END_TAG )
	parser.read(Xml.END_TAG, "", "data");
	parser.skip();
	return v;
    }//end parseArray()
}//end XmlRpcParser