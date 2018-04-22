package com.modofo.mofire.ui;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

import com.modofo.mofire.MoFire;
import com.modofo.mofire.Res;
import com.modofo.mofire.domain.Post;

public class PostList extends List implements CommandListener{
	private Vector posts;
	private Command editCmd,deleteCmd,backCmd,newCmd;
	private Post selectedPost;
	private MoFire midlet;
	public PostList(MoFire midlet) {
		super("Drafts", List.EXCLUSIVE);
		this.midlet = (MoFire) midlet;
		
		editCmd = new Command("Edit", Command.OK, 2);
		newCmd = new Command("New", Command.OK, 3);
		backCmd = new Command("Back", Command.BACK, 1);
		deleteCmd = new Command("Delete", Command.BACK, 1);
		
		addCommand(editCmd);
		addCommand(newCmd);
		addCommand(backCmd);
		addCommand(deleteCmd);
		
		setCommandListener(this);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if(arg0 == editCmd) doEditCmd();
		else if(arg0 == deleteCmd) doDeleteCmd();
		else if(arg0 == backCmd) doBackCmd();
		else if(arg0 == newCmd) doNewCmd();
	}

	private void doNewCmd() {
		midlet.getPostForm().setPost(new Post());
		midlet.showPostForm();
	}

	private void doBackCmd() {
		midlet.showPostForm();
	}

	private void doEditCmd() {
		if(posts.size()==0) return;
		if(getSelectedIndex()<0 || getSelectedIndex()> posts.size()) return;
		selectedPost =(Post) posts.elementAt(this.getSelectedIndex());
		midlet.getPostForm().setPost(selectedPost);
		midlet.showPostForm();
	}

	void doDeleteCmd(){
		if(posts.size()==0) return;
		if(getSelectedIndex()<0 || getSelectedIndex()> posts.size()) return;
		showConfirmation("Confirmation",Res.getString("post.delete.confirm"));
	}
	public Vector getPosts() {
		return posts;
	}

	public void setPosts(Vector posts) {
		this.posts = posts;
		if(this.posts == null) return;
		this.deleteAll();
		for(int i=0;i<posts.size();i++){
			Post p = (Post)posts.elementAt(i);
			this.append(p.getTitle(),null);
		}
	}
	
	protected void showConfirmation(String title, String text) {
        Alert alert = new Alert(title, text, null, AlertType.CONFIRMATION);
        alert.addCommand(new Command ("Yes", Command.OK, 1));
        alert.addCommand(new Command("No", Command.CANCEL, 1));
        alert.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                if (c.getLabel().equals("Yes")) {
                	selectedPost =(Post) posts.elementAt(PostList.this.getSelectedIndex());
            		try {
            			MoFire.getMgr().delPost(selectedPost.getId());
            			setPosts(MoFire.getMgr().getPosts(null));
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
                }
                Display.getDisplay(midlet.getMidlet()).setCurrent(PostList.this);
            }
        });
        Display.getDisplay(midlet.getMidlet()).setCurrent(alert, this);
    }
}
