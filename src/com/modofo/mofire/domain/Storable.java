package com.modofo.mofire.domain;

public interface Storable {
	byte[] toByteArray();

	int getId();

	boolean fromByteArray(byte[] ba);

	void setId(int id);
	
	String toString();
}
