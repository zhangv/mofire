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

import javax.microedition.io.*;

import org.kxml.*;
import org.kxml.io.*;
import org.kxml.parser.*;

/**
 * A single-threaded, reusable XML-RPC client object.
 */
public class XmlRpcClient {

	/**
	 * Stores the full URL the client will connect with
	 */
	String url;

	/**
	 * Stores the response sent back by the server
	 */
	Object result = null;

	/**
	 * Turns debugging on/off
	 */
	boolean debug = false;

	/**
	 * Constructs an XML-RPC client with a specified string representing a URL.
	 * 
	 * @param url
	 *            The full URL for the XML-RPC server
	 */
	public XmlRpcClient(String url) {
		this.url = url;
	}// end KxmlRpcClient( String )

	/**
	 * Construct an XML-RPC client for the specified hostname and port.
	 * 
	 * @param hostname
	 *            the name of the host server
	 * @param the
	 *            server's port number
	 */
	public XmlRpcClient(String hostname, int port) {
		this.url = "http://" + hostname + ":" + port;
	}// end KxmlRpcClient( String, int )

	public String getURL() {
		return url;
	}// end getURL()

	public void setURL(String newUrl) {
		url = newUrl;
	}// end setURL( String )

	/**
	 * This method is the brains of the XmlRpcClient class. It opens an
	 * HttpConnection on the URL stored in the url variable, sends an XML-RPC
	 * request and processes the response sent back from the server.
	 * 
	 * @param method
	 *            contains the method on the server that the client will access
	 * @param params
	 *            contains a list of parameters to be sent to the server
	 * @return the primitive, collection, or custom object returned by the
	 *         server
	 */
	public Object execute(String method, Vector params) throws Exception {
		// Kxmlrpc classes
		XmlWriter xw = null;
		XmlRpcWriter writer = null;
		XmlRpcParser parser = null;
		// J2ME classes
		HttpConnection con = null;
		InputStream in = null;
		OutputStream out = null;
		// Misc objects for buffering request
		ByteArrayOutputStream bos = null;
		byte[] request;
		int messageLength;

		try {
			bos = new ByteArrayOutputStream();
			xw = new XmlWriter(new OutputStreamWriter(bos, "UTF-8"));
			writer = new XmlRpcWriter(xw);

			writer.writeCall(method, params);
			xw.flush();

			if (debug)
				System.out.println(bos.toString());
			request = bos.toByteArray();

			messageLength = request.length;

			con = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
			con.setRequestMethod(HttpConnection.POST);
			con.setRequestProperty("Content-Length", Integer
					.toString(messageLength));
			con.setRequestProperty("Content-Type", "text/xml");

			// Obtain an output stream
			out = con.openOutputStream();
			// Push the request to the server
			out.write(request);
			// Open an input stream on the server's response
			in = con.openInputStream();
			// Parse response from server
			parser = new XmlRpcParser(new XmlParser(new InputStreamReader(in,
					"UTF-8")));
			result = parser.parseResponse();
//			System.out.println("result:" + result);
		} catch (Exception x) {
//			x.printStackTrace();
			throw x;
		} finally {
			try {
				if (con != null)
					con.close();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}// end try/catch
		}// end try/catch/finally

		if (result instanceof Exception)
			throw (Exception) result;

		return result;
	}// end execute( String, Vector )

	/**
	 * Called when the return value has been parsed.
	 */
	void setParsedObject(Object parsedObject) {
		result = parsedObject;
	}// end objectCompleted( Object )

}// end class KxmlRpcclient
