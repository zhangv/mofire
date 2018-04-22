package com.modofo.mofire.ui;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.media.MediaException;

import com.modofo.camera.Camera;
import com.modofo.camera.CameraEvent;
import com.modofo.camera.CameraListener;
import com.modofo.fileexplorer.FileExplorer;
import com.modofo.fileexplorer.FileExplorerEvent;
import com.modofo.fileexplorer.FileExplorerListener;
import com.modofo.jmeutil.ImageUtils;
import com.modofo.jmeutil.StringUtils;
import com.modofo.jmeutil.Utils;
import com.modofo.mofire.Constants;
import com.modofo.mofire.MoFire;
import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireEventType;
import com.modofo.mofire.MoFireListener;
import com.modofo.mofire.Res;
import com.modofo.mofire.call.CallFactory;
import com.modofo.mofire.call.NewPostCall;
import com.modofo.mofire.domain.Post;
import com.modofo.mofire.domain.Setting;
import com.modofo.recordr.Recorder;
import com.modofo.recordr.RecorderEvent;
import com.modofo.recordr.RecorderListener;

public class PostForm extends BaseForm implements CommandListener,
MoFireListener,CameraListener,RecorderListener,FileExplorerListener {

	public static int IMAGE_SCALE = 6; // rescale image for overview in form
	
	private Command settingCmd, editContentCmd, postCmd, listCmd, cameraCmd,
			removeImageCmd, saveCmd,imageListCmd,soundClipCmd,playCmd,stopPlayCmd, newCmd
			,deleteImgCmd,previewImgCmd,deleteSoundCmd,showPostFormCmd,exitCmd;

	private TextField titleFld;
	private ChoiceGroup catChoice;
	private ContentInput contentInput;
	private TextField tagsFld;
	private Post post = new Post();
	private ImageItem soundIcon;
	
	//private Alert alert;
	private Setting curSetting;
	String[] catnames, catids;
	// for camera
	private Camera camera;
	// for recorder
	private Recorder recorder;
	private byte[] tempSoundClip; //temporary storing the sound clip,
	//
	private FileExplorer fileExplorer;

	private int imageIndex;
	private ImageItemCommandListener imageItemCommandListener = new ImageItemCommandListener();
	private SoundItemCommandListener soundItemCommandListener = new SoundItemCommandListener();
	
	private Command postConfirmYesCmd = new Command ("Yes", Command.OK, 1);
	private Command postConfirmNoCmd = new Command("No", Command.CANCEL, 1);
	private Command delSoundConfirmYesCmd = new Command ("Yes", Command.OK, 1);
	private Command delSoundConfirmNoCmd = new Command("No", Command.CANCEL, 1);
	private Command overwriteSoundConfirmYesCmd = new Command ("Yes", Command.OK, 1);
	private Command overwriteSoundConfirmNoCmd = new Command("No", Command.CANCEL, 1);
	
	public PostForm(MoFire midlet,String title) {
		super(midlet,title);
		this.mofire = (MoFire) midlet;
		settingCmd 		= new Command(Res.get("cmd.setting"), Command.BACK, 1);
		editContentCmd 	= new Command(Res.get("cmd.edit.content"), Command.OK, 0);
		postCmd 		= new Command(Res.get("cmd.post"), Command.SCREEN, 1);
		newCmd 			= new Command(Res.get("cmd.new"),Command.SCREEN,1);
		listCmd 		= new Command(Res.get("cmd.drafts"), Command.SCREEN, 6);
		saveCmd 		= new Command(Res.get("cmd.save"), Command.SCREEN, 4);
		imageListCmd 	= new Command(Res.get("cmd.load.image"),Command.SCREEN, 5);
		soundClipCmd 	= new Command(Res.get("cmd.recorder"),Command.SCREEN,7);

		playCmd 		= new Command(Res.get("cmd.play"),Command.ITEM,1);
		stopPlayCmd	 	= new Command(Res.get("cmd.stopplay"),Command.ITEM,2);
		deleteSoundCmd 	= new Command(Res.get("cmd.delete.sound"),Command.ITEM,3);
		// command on image item
		deleteImgCmd 	= new Command(Res.get("cmd.delete.image"),Command.ITEM,0);
		previewImgCmd   = new Command(Res.get("cmd.preview.image"),Command.ITEM,1);
		
		// command in text input
		showPostFormCmd = new Command(Res.get("cmd.back"),Command.BACK,1);
		exitCmd = 		new Command(Res.get("cmd.exit"), Command.EXIT, 1);

		titleFld = new TextField(Res.get("title"), "", 200, TextField.ANY);
		catChoice = new ChoiceGroup(Res.get("category"), ChoiceGroup.POPUP);
		contentInput = new ContentInput(this);
		tagsFld = new TextField(Res.get("tag"),"",200,TextField.ANY);
		
		append(titleFld);
		append(catChoice);
		append(tagsFld);
		
		addCommand(exitCmd);
		
		addCommand(settingCmd);
		addCommand(editContentCmd);
		addCommand(postCmd);
		addCommand(newCmd);
		addCommand(listCmd);
		addCommand(saveCmd);
		addCommand(imageListCmd);
		addCommand(soundClipCmd);
		
		// if camera not supported,wont show the command
		if (this.mofire.isCameraSupported()) {
			cameraCmd = new Command(Res.get("cmd.camera"), Command.SCREEN, 2);
			removeImageCmd = new Command(Res.get("cmd.remove.all.image"), Command.SCREEN, 3);
			addCommand(cameraCmd);
			camera = new Camera(this);
			camera.addListener(this);
		}
		
		//init recorder
		recorder = new Recorder(this.midlet,this);
		recorder.addListener(this);
		setCommandListener(this);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if (arg0 == editContentCmd)
			doOkCmd();
		else if (arg0 == settingCmd)
			doSettingCmd();
		else if (arg0 == listCmd)
			doListCmd();
		else if (arg0 == postCmd)
			showConfirmation(Res.get("confirmation"), Res.get("confirm.post"),this,postConfirmYesCmd,postConfirmNoCmd);
		else if(arg0 == postConfirmYesCmd){
			doPostCmd();
		}
		else if(arg0 == postConfirmNoCmd){
			Display.getDisplay(midlet).setCurrent(PostForm.this);
		}
		else if (arg0 == delSoundConfirmYesCmd){
			post.setSound(null);
			//re-layout the images
			refreshImages();
			Display.getDisplay(midlet).setCurrent(PostForm.this);
		}else if(arg0==delSoundConfirmNoCmd){
			Display.getDisplay(midlet).setCurrent(PostForm.this);
		}else if (arg0 == overwriteSoundConfirmYesCmd){
			appendSoundIcon();
			post.setSound(tempSoundClip);
		}else if(arg0==overwriteSoundConfirmNoCmd){
			//do nothing
		}else if (arg0 == cameraCmd)
			showCamera();
		// remove all the images
//		else if (arg0 == removeImageCmd)
//			doRemoveImageCmd();
		else if (arg0 == saveCmd)
			savePost();
		else if (arg0 == imageListCmd)
			showImageList();
		else if(arg0 == soundClipCmd){
			recorder.showRecorder();
		}else if(arg0 ==newCmd){
			doNewCmd();
		}else if(arg0 == showPostFormCmd){
			mofire.showPostForm();
		}
		else if(arg0 == exitCmd) mofire.exit();
	}
	
	private void doNewCmd() {
		this.setPost(new Post());
		imageIndex = 0;
		mofire.showPostForm();
	}

	private void showImageList() {
		new Thread(new Runnable(){
			public void run(){
				Display.getDisplay(midlet).setCurrent(getFileExplorer());
			}
		}).start();
	}

	public void doPostCmd() {
		String title = titleFld.getString().trim();
		if (null == title || "".equals(title)) {
			Utils.alert(midlet,Res.get("post.title.blank"), Res.get("information"),
					3000);
		} else {
			post.setTitle(title);
			System.out.println(catids);
			System.out.println(catChoice.getSelectedIndex());
			if(catids == null || catids.length==0){
				post.setCategory("0");
			}else{
				post.setCategory(catids[catChoice.getSelectedIndex()]);
			}
			post.setTags(tagsFld.getString().trim());
			post.setContent(contentInput.getString().trim());
			postContent();
			Display.getDisplay(midlet).setCurrent(PostForm.this);
		}
	}

	private void showConfirmation(String title, String text,CommandListener listener,Command yesCmd,Command noCmd) { 
		Alert alert = new Alert(title, text, null, AlertType.CONFIRMATION);
        alert.addCommand(yesCmd);
        alert.addCommand(noCmd);
        alert.setCommandListener(listener);
        Display.getDisplay(midlet).setCurrent(alert, this);
	}

	public void showCamera() {
		try {
			camera.start(midlet);
			//camera.start(this);
		} catch (IOException ioe) {
			Utils.error(midlet,ioe.toString());
		} catch (MediaException me) {
			Utils.error(midlet,me.toString());
		}
	}
	
	public Camera getCamera(){ //for unit testing
		return this.camera;
	}
	private void doSettingCmd() {
		mofire.showSettingForm();
	}

	private void doListCmd() {
		mofire.showPostList();
	}

	private void doOkCmd() {
		post.setTitle(titleFld.getString().trim());
		contentInput.setTitle(post.getTitle());
		mofire.setCurrent(contentInput);
	}

	public void postContent(){
		NewPostCall npc = (NewPostCall) CallFactory.getNewPostCall(curSetting, post);
		npc.addListener(mofire);
		npc.go();
	}
	
	public void setCurSetting(Setting curSetting2) {
		this.curSetting = curSetting2;
		setTitle(curSetting.getName());
		String catStr = curSetting.getCategories();
		
		// if default setting and it has no category info stored
		if (catStr == null)
			return;
		Vector cats = StringUtils.split(catStr, ';');

		int len = cats.size();
		catnames = new String[len];
		catids = new String[len];
		catChoice.deleteAll();
		for (int i = 0; i < len; i++) {
			Vector v = StringUtils.split((String) cats.elementAt(i), ',');
			catids[i] = (String) v.elementAt(0);
			catnames[i] = (String) v.elementAt(1);
			catChoice.append(catnames[i], null);
		}
	}

	public void clear(){
		post = null;
		titleFld.setString("");
		tagsFld.setString("");
		contentInput.setString("");
		if(catids!=null){
			catChoice.setSelectedIndex(0, true);
		}
		//remove attachment
		while(this.size()>3){
			this.delete(3);
		}
	}
	public void setPost(Post post) {
		this.post = post;
		imageIndex=0;
		removeAttachmentIcons();
		titleFld.setString(post.getTitle());
		tagsFld.setString(post.getTags());
		// catFld.setString(post.getCategory()+"");
		String category = post.getCategory();
		if (catids != null || catids.length>0) { //if not retrieved category info from server, then have to ignore the category here
			if (category == null)
				catChoice.setSelectedIndex(0, true);
			else {// search in category
				int foundidx = 0;
				for (int i = 0; i < catids.length; i++) {
					if (catnames[i].equals(category)) {
						foundidx = i;
						break;
					}
				}
				if(foundidx>0){//if no category exists(catids.length==0), will IndexOutOfBoundsException 0>-1 -FIXED
					catChoice.setSelectedIndex(foundidx, true); 
				}
			}
		}
		contentInput.setString(post.getContent());
		
		if(post.getSoundPath()!=null){
			byte[] bytes = null;
			try{
				bytes = Utils.readFile(post.getSoundPath());
			}catch(Exception e){
				Utils.error(midlet,Res.get("err.reading.soundfile"));
				bytes = null;
			}
			if(bytes!=null){
				appendSound(bytes);
			}
		}
		
		Vector imgs = post.getImagePaths();
		int sz = imgs.size();
		String imgpath;
		for (int i = 0; i < sz; i++) {
			imgpath = (String) imgs.elementAt(i);
			//might throw filenotexistexception, as the image might be removed
			byte[] bytes = null;
			try{
				bytes = Utils.readFile(imgpath);
			}catch(Exception e){
				Utils.error(midlet,Res.get("err.reading.imagefile"));
				bytes = null;
			}
			if (bytes != null) {
				//not append item here, will cause inconsist while clearing images
				//only for debug
				//this.append(new StringItem("Image", imgpath));
				appendImage(bytes);
			}
		}
		
	}
	
	public void savePost() {
		post.setTitle(titleFld.getString().trim());
		post.setContent(contentInput.getString().trim());
		post.setTags(tagsFld.getString().trim());
		// if categories are not got from server, then assign a dummy category -
		// 0
		if (catids == null || catChoice.getSelectedIndex()==-1)
			post.setCategory("0");
		else
			post.setCategory(catids[catChoice.getSelectedIndex()]);
		
		try {
			int newid = MoFire.getMgr().savePost(post);
			post.setId(newid);
			Utils.info(midlet,Res.get("post.saved"));
		} catch (Exception e) {
			Utils.error(midlet,e.toString());
		}
		//refresh post list
		mofire.showPostList();
	}
	
	public void appendImage(byte[] raw) {
		post.addImage(raw);
		appendThumbnail(raw);
	}
	
	//called when restore from local draft
	public void appendThumbnail(byte[] raw) {
		//TODO check the source of crateImage, see if there's any perforamnce tuning
		Image image = Image.createImage(raw, 0, raw.length);
		appendThumbnail(image);
		image=null;
	}
	
	private void appendThumbnail(Image image){
		ImageItem imi = new ImageItem((++imageIndex)+"",null,ImageItem.LAYOUT_LEFT,"Cannot display");
		imi.addCommand(deleteImgCmd);
		imi.addCommand(previewImgCmd);
		Image img = ImageUtils.rescaleImage(image);
		imi.setImage(img);
		imi.setItemCommandListener(imageItemCommandListener);
		PostForm.this.append(imi);
		image=null;
	}
	
	private void appendSound(byte[] raw){
		if(raw == null) return;
		if(post.getSound()==null){ // no sound clip yet
			appendSoundIcon();
			post.setSound(raw);
		}else{ //already exists one sound clip
			//Confirm to overwrite last sound clip
			//Utils.confirm("Overwrite?", "Confirm", Alert.FOREVER);a
			tempSoundClip  = raw;
			showConfirmation(Res.get("confirmation"), Res.get("confirm.overwrite.sound"),this,overwriteSoundConfirmYesCmd,overwriteSoundConfirmNoCmd);
		}
	}
	
	private void appendSoundIcon(){
		Image playImage=null;
		try {
			playImage = Image.createImage("/control-play_32.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
		soundIcon = new ImageItem("",null,ImageItem.LAYOUT_CENTER,"");
		soundIcon.setImage(playImage);
		soundIcon.addCommand(playCmd);
		soundIcon.setDefaultCommand(playCmd);
		soundIcon.addCommand(stopPlayCmd);
		soundIcon.addCommand(deleteSoundCmd);
		soundIcon.setItemCommandListener(soundItemCommandListener);
		this.insert(3, soundIcon); //insert clip icon before image attachment
	}
	
	private void removeAttachmentIcons(){
		int formsize = this.size();
		int fromIndex=3;
		for (int i = fromIndex; i < formsize; i++) {
			this.delete(fromIndex);
		}
	}
	
	private void refreshImages(){
		int formsize = this.size();
		//hard-coded, index of images (from third, after title,category,tag)
		int fromIndex=3; //append after the sound clip icon
		byte[] soundClip=post.getSound();
		if(soundClip!=null) {
			//20090330 removed following one line
			fromIndex=4;
		}
		//re-append
		for (int i = fromIndex; i < formsize; i++) {
			this.delete(fromIndex);
		}
		Vector images = post.getImages();
		imageIndex = 0;
		for(int ii=0;ii<images.size();ii++){
			appendThumbnail((byte[])images.elementAt(ii));
		}
	}
	
	
	public void onEvent(MoFireEvent evt) {
		if(evt.getType() == MoFireEventType.POST_ERROR){
			Utils.error(midlet,evt.getErrorMessage());
		}else if(evt.getType()==MoFireEventType.RECORDER_STOP){
			appendSound(evt.getByteData());
		}
	}

	class SoundItemCommandListener implements ItemCommandListener{
		public void commandAction(Command arg0, Item arg1) {
			if(arg0 ==playCmd){
				if(post.getSound()!=null){
					recorder.playSound("audio/x-wav", post.getSound());
				}
			}else if(arg0 == stopPlayCmd){
				try {
					recorder.stopAudioPlay();
				} catch (MediaException e) {
					e.printStackTrace();
				}
			}else if(arg0 == deleteSoundCmd){
				showConfirmation(Res.get("confirmation"), Res.get("confirm.delete.sound"), PostForm.this, delSoundConfirmYesCmd, delSoundConfirmNoCmd);
			}
		}
		
	}
	class ImageItemCommandListener implements ItemCommandListener{
		public void commandAction(Command arg0, Item arg1) {
			if(arg0 == deleteImgCmd){
				int idx = Integer.parseInt(arg1.getLabel());
				post.removeImage(idx-1);
				refreshImages();
			} else if(arg0 == previewImgCmd){
				int idx = Integer.parseInt(arg1.getLabel());
				Image image = (Image)post.getImages().elementAt(idx-1);
				getPreviewCanvas().preview(image);
			}
		}
	}
	public void photoCaptured(CameraEvent ce) {
		byte[] raw = ce.getBytes();
		appendImage(raw);
	}

	public void onAudioPlayStop(RecorderEvent re) {
		//TODO cache the image
		Image stopImage=null;
		try {
			stopImage = Image.createImage("/control-play_32.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
		soundIcon.setImage(stopImage);
	}

	public void onAudioRecordStop(RecorderEvent re) {
		appendSound(re.getBytes());
		Display.getDisplay(midlet).setCurrent(this);
	}
	
	public void onAudioRecordCancel(RecorderEvent re) {
		Display.getDisplay(midlet).setCurrent(this);
	}
	
	public void onAudioPlayStart(RecorderEvent re) {
		Image stopImage=null;
		try {
			stopImage = Image.createImage("/control-stop_32.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
		soundIcon.setImage(stopImage);
	}
	
	private FileExplorer getFileExplorer(){
		if(fileExplorer==null){
			String imagePath = System.getProperty(Constants.PHOTO_DIRECTORY);
			fileExplorer = new FileExplorer("Images",imagePath,this,midlet);
		}
		return fileExplorer;
	}

	public void onFileSelected(FileExplorerEvent evt) {
		if(evt.getType()==FileExplorerEvent.FILE_EXPLORER_EVENT_SELECT_FILE){
			String filepath = evt.getPath();
			try{
				byte[] bytes = Utils.readFile(filepath);
				Utils.info(midlet,bytes.length+"");
				if(bytes!=null) appendImage(bytes);
				Display.getDisplay(midlet).setCurrent(this);
			}catch(Exception e){
				Utils.error(midlet,e.toString());
				e.printStackTrace();
			}
		}
	}
	private PreviewImageCanvas previewCanvas;
	private PreviewImageCanvas getPreviewCanvas(){
		if(previewCanvas==null){
			previewCanvas = new PreviewImageCanvas(this);
		}
		return previewCanvas;
	}
	
	class ContentInput extends TextBox {
		public ContentInput(PostForm parent) {
			super("", "", 2000, TextField.ANY);
			addCommand(postCmd);
			addCommand(saveCmd);
			addCommand(showPostFormCmd);
			setCommandListener(parent);
		}
	}
	
	class PreviewImageCanvas extends Canvas {
		private Image image; //image to view
		public PreviewImageCanvas(PostForm parent){
			addCommand(showPostFormCmd);
			setCommandListener(parent);
		}
		protected void paint(Graphics g) {
			if(image == null) return;
			g.setGrayScale (255);
		    g.fillRect (0, 0, getWidth (), getHeight ());

		    int screenW = getWidth();
		    int screenH = getHeight();
		    int imgW = image.getWidth();
			int imgH = image.getHeight();
			double factor = 1d;
			int h,w = 0;
			if(imgW/imgH >= screenW/screenH){
				factor = screenW/imgW;
				h = (int) (imgH*factor);
				w = screenW;
			}else{
				factor = screenH/imgH;
				h = screenH;
				w = (int) (imgW*factor);
			}
		    g.drawImage (image, 0, 0, Graphics.HCENTER | Graphics.VCENTER);

		}
		
		public void preview(Image image){
			this.image = image;
			this.repaint();
			Display.getDisplay(midlet).setCurrent(this);
		}
	}
}
