package com.modofo.mofire;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Image;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;

import com.modofo.jmeutil.Utils;

public class GoogleMaps {
	private static final String URL_UNRESERVED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "abcdefghijklmnopqrstuvwxyz" + "0123456789-_.~";
	private static final char[] HEX = "0123456789ABCDEF".toCharArray();

	// these 2 properties will be used with map scrolling methods. You can
	// remove them if not needed
	public static final int offset = 268435456;
	public static final double radius = offset / Math.PI;

	private static String apiKey = null;

	public GoogleMaps(String key) {
		apiKey = key;
	}

	public static String getCurrentLocationMapUrl() throws LocationException,
			InterruptedException {
		Criteria crit1 = new Criteria();
		crit1.setHorizontalAccuracy(25); // 25m
		crit1.setVerticalAccuracy(25); // 25m
		crit1.setPreferredResponseTime(Criteria.NO_REQUIREMENT);
		crit1.setPreferredPowerConsumption(Criteria.NO_REQUIREMENT);
		crit1.setCostAllowed(false);
		crit1.setSpeedAndCourseRequired(true);
		crit1.setAltitudeRequired(true);
		crit1.setAddressInfoRequired(true);
		// Get an instance of the provider
		LocationProvider lp = null;
		try{
			lp = LocationProvider.getInstance(null);
		}catch (Exception e) {
			Utils.alert(MoFire.getMidlet(), e.toString(), "ERROR", Alert.FOREVER);
			return null;
		}

		// Request the location, setting a one-minute timeout
		//timeout 10 秒
		Location l = lp.getLocation(10);
		Coordinates c = l.getQualifiedCoordinates();

		String mapurl = null;
		if (c != null) {
			// Use coordinate information
			double lat = c.getLatitude();
			double lon = c.getLongitude();
			mapurl = getMapUrl(400, 400, lon, lat, 8, "png32");
		}
		return mapurl;

	}

	public double[] geocodeAddress(String address) throws Exception {
		byte[] res = loadHttpFile(getGeocodeUrl(address));
		String[] data = split(new String(res), ',');

		if (!data[0].equals("200")) {
			int errorCode = Integer.parseInt(data[0]);
			throw new Exception("Google Maps Exception: "
					+ getGeocodeError(errorCode));
		}

		return new double[] { Double.parseDouble(data[2]),
				Double.parseDouble(data[3]) };
	}

	public Image retrieveStaticImage(int width, int height, double lat,
			double lng, int zoom, String format) throws IOException {
		byte[] imageData = loadHttpFile(getMapUrl(width, height, lng, lat,
				zoom, format));

		return Image.createImage(imageData, 0, imageData.length);
	}

	private static String getGeocodeError(int errorCode) {
		switch (errorCode) {
		case 400:
			return "Bad request";
		case 500:
			return "Server error";
		case 601:
			return "Missing query";
		case 602:
			return "Unknown address";
		case 603:
			return "Unavailable address";
		case 604:
			return "Unknown directions";
		case 610:
			return "Bad API key";
		case 620:
			return "Too many queries";
		default:
			return "Generic error";
		}
	}

	public String getGeocodeUrl(String address) {
		return "http://maps.google.com/maps/geo?q=" + urlEncode(address)
				+ "&output=csv&key=" + apiKey;
	}

	public static String getMapUrl(int width, int height, double lng, double lat,
			int zoom, String format) {
		return "http://maps.google.com/staticmap?center=" + lat + "," + lng
				+ "&format=" + format + "&zoom=" + zoom + "&size=" + width
				+ "x" + height ;//+ "&key=" + apiKey;
	}

	private static String urlEncode(String str) {
		StringBuffer buf = new StringBuffer();
		byte[] bytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeUTF(str);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			// ignore
		}
		for (int i = 2; i < bytes.length; i++) {
			byte b = bytes[i];
			if (URL_UNRESERVED.indexOf(b) >= 0) {
				buf.append((char) b);
			} else {
				buf.append('%').append(HEX[(b >> 4) & 0x0f]).append(
						HEX[b & 0x0f]);
			}
		}
		return buf.toString();
	}

	private static byte[] loadHttpFile(String url) throws IOException {
		byte[] byteBuffer;

		HttpConnection hc = (HttpConnection) Connector.open(url);
		try {
			hc.setRequestMethod(HttpConnection.GET);
			InputStream is = hc.openInputStream();
			try {
				int len = (int) hc.getLength();
				if (len > 0) {
					byteBuffer = new byte[len];
					int done = 0;
					while (done < len) {
						done += is.read(byteBuffer, done, len - done);
					}
				} else {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buffer = new byte[512];
					int count;
					while ((count = is.read(buffer)) >= 0) {
						bos.write(buffer, 0, count);
					}
					byteBuffer = bos.toByteArray();
				}
			} finally {
				is.close();
			}
		} finally {
			hc.close();
		}

		return byteBuffer;
	}

	private static String[] split(String s, int chr) {
		Vector res = new Vector();

		int curr;
		int prev = 0;

		while ((curr = s.indexOf(chr, prev)) >= 0) {
			res.addElement(s.substring(prev, curr));
			prev = curr + 1;
		}
		res.addElement(s.substring(prev));

		String[] splitted = new String[res.size()];
		res.copyInto(splitted);

		return splitted;
	}
}