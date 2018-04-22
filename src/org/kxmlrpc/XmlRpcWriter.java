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
import org.kxml.io.*;

import org.kobjects.isodate.IsoDate;
import org.kobjects.base64.Base64;

/** 
 * This class builds XML-RPC method calls using the kxml pull parser's
 * AbstractXmlWriter class
 */
public class XmlRpcWriter {

    /**
     * Used to access the kxml parser
     */
    AbstractXmlWriter writer;

    public XmlRpcWriter( AbstractXmlWriter writer ) {
	this.writer = writer;
    }//end XmlRpcWriter( AbstractXmlWriter )

    /**
     * Builds the XML-RPC XML document
     *
     * @param name the method's name
     * @param params the parameters to be passed to the server
     */
    public void writeCall( String name, Vector params ) throws IOException {
	writer.startTag( "methodCall" );
	writer.startTag( "methodName" );
	writer.write( name );
	writer.endTag();

	if( params != null && params.size () > 0 ) {
	    writer.startTag( "params" );

	    for( int i = 0; i < params.size (); i++ ) {
		writer.startTag( "param" );
                // The writeValue() method is called for each parameter that is
                //  encoded in the call
		writeValue( params.elementAt(i) );
		writer.endTag();
	    }//end for( int i = 0; i < params.size (); i++ )
	    
	    writer.endTag();
	}//end if( params != null && params.size () > 0 )
	writer.endTag();
    }//end writeCall( String, Vector ) 

    /*
     * Maps from Java data types to XML-RPC data types and encodes the parameter 
     * value(s) using XML-RPC elements
     */
    private void writeValue( Object value ) throws IOException {
	writer.startTag( "value" );

	if( value instanceof String ) {
	    writer.startTag( "string" );
	    writer.write( (String) value );
	}
	else if( value instanceof Integer ) {
	    writer.startTag( "i4" );
	    writer.write( "" + ( (Integer) value ).intValue() );
	}
	else if( value instanceof Boolean ) {
	    writer.startTag( "boolean" );
	    writer.write( ( (Boolean) value ).booleanValue() ? "1" : "0" );
	}
        // XML-RPC dates must be formatted using the iso8601 standard
        else if( value instanceof Date ) {
	    writer.startTag( "dateTime.iso8601" );
	    writer.write 
		( IsoDate.dateToString ( (Date) value, IsoDate.DATE_TIME ) );
	}
        // java.util.Vector maps to an XML-RPC array
	else if( value instanceof Vector ) {
	    writer.startTag( "array" );
	    Vector v = (Vector) value;
	    for( int i = 0; i < v.size(); i++ )
		writeValue( v.elementAt(i) );// recursive call
	}
        // java.util.Hashtable maps to an XML-RPC struct
	else if( value instanceof Hashtable ) {
	    writer.startTag( "struct" );
	    Hashtable h = (Hashtable) value;
	    for( Enumeration e = h.keys(); e.hasMoreElements(); ) {
		Object key = e.nextElement();
		writer.startTag( "member" );
		writer.startTag( "name" );
		writer.write( key.toString() );// recursive call
		writer.endTag();// </name>
		writeValue( h.get(key) );
		writer.endTag();// </member>
	    }//end for( Enumeration e = h.keys (); e.hasMoreElements(); )
	}
        // byte arrays must be encoded using the Base64 encoding
	else if( value instanceof byte[] ) {
	    writer.startTag( "base64" );
	    writer.write( Base64.encode( (byte[]) value ) );
	}
     	else throw new IOException( "Unknown data type: " + value );

	writer.endTag();// close specific data type tag
	writer.endTag();// </value>
    }//end writeValue( Object )
}//end class XmlRpcWriter