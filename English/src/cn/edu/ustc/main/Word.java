package cn.edu.ustc.main;



public class Word {

	private String word;
	private String trans;
	private String phonetic;
	private String tags;
	private int progress;
		
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getTrans() {
		return trans;
	}
	
	public void setTrans(String trans) {
		this.trans = trans;
	}
	
	public String getPhonetic() {
		return phonetic;
	}
	
	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}
	
	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}		
}
