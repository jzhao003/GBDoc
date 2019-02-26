package gbdoc.doc;

import java.io.File;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class GbFileParagraph {

	private File file;
	private List<XWPFParagraph> paragraphList;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<XWPFParagraph> getParagraphList() {
		return paragraphList;
	}

	public void setParagraphList(List<XWPFParagraph> paragraphList) {
		this.paragraphList = paragraphList;
	}
}
