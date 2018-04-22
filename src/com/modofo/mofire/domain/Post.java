package com.modofo.mofire.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class Post implements Storable {
	private String title="",content="";
	private String category="";
	private String tags="";
	private int id ;
	private Vector images = new Vector(); 
	private Vector imagePaths = new Vector(); //image path(String):used when restore post from local
	private Vector imageAttachments = new Vector();
	private SoundAttachment soundAttachment;
	private byte[] sound;
	private String soundPath;
	private String link;
	
	public String getSoundPath() {
		return soundPath;
	}
	public void setSoundPath(String soundPath) {
		this.soundPath = soundPath;
	}
	public byte[] getSound() {
		return sound;
	}
	public void setSound(byte[] sound) {
		this.sound = sound;
	}
	public Vector getImageAttachments() {
		return imageAttachments;
	}
	public void setImageAttachments(Vector imageAttachments) {
		this.imageAttachments = imageAttachments;
	}
	public void addImageAttachment(ImageAttachment ia) {
		imageAttachments.addElement(ia);
	}
	public void removeImageAttachment(ImageAttachment ia) {
		imageAttachments.removeElement(ia);
	}
	public SoundAttachment getSoundAttachment() {
		return soundAttachment;
	}
	public void setSoundAttachment(SoundAttachment soundAttachment) {
		this.soundAttachment = soundAttachment;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public String toString(){
		return new StringBuffer()
		.append(id)
		.append(";")
		.append(title)
		.append(";")
		.append(content).toString();
	}
	public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);
        byte[] reval=null;
        try {
        	os.writeInt(id);
            os.writeUTF(title);
            os.writeUTF(content);
            os.writeUTF(category);
            os.writeUTF(tags);
            os.writeInt(images.size());
            int size = imagePaths.size();
            String s = null;
            for(int i=0;i<size;i++){
            	s = (String) imagePaths.elementAt(i);
            	os.writeUTF(s);
            }
            if(soundPath!=null){
            	os.writeBoolean(true);
            	os.writeUTF(soundPath);
            }else{
            	os.writeBoolean(false);
            }
            os.flush();
            reval = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	if(baos!=null)
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
        return reval;
    }
	
	public boolean fromByteArray(byte[] data) {
		DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
        try {
        	id = is.readInt();
            title = is.readUTF();
            content = is.readUTF();
            category = is.readUTF();
            tags = is.readUTF();
            int size = is.readInt();
            for(int i =0;i<size;i++){
            	imagePaths.addElement(is.readUTF());
            }
            boolean hasSound = false;
            hasSound = is.readBoolean();
            if(hasSound){
            	soundPath = is.readUTF();
            }
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }finally{
        	if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
    }
	public Vector getImages() {
		return images;
	}
	public void setImages(Vector images) {
		this.images = images;
	}
	public void addImage(byte[] image){
		this.images.addElement(image);
	}
	public void removeImage(int index){
		if(images.size()>=index+1){
			images.removeElementAt(index);
		}
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Vector getImagePaths() {
		return imagePaths;
	}
	public void setImagePaths(Vector imagePaths) {
		this.imagePaths = imagePaths;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
