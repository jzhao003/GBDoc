package gbdoc.doc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import gbdoc.util.GbDocFileUtil;

public class FileListContainsSection {

	private List<File> fileList= new ArrayList<File>();

	public List<File> getFileList() {
		return fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}
	
	public void findFileNameInDirectory(File dir, String section) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				findFileNameInDirectory(file, section);
			} else {
				if (GbDocFileUtil.getFileExtendName(file.getName()).equals("docx") && file.length() != 0) {
//					System.out.println(file.getAbsolutePath());
					if (isContainSection(file,section)) {
						fileList.add(file);
					}
				}
			}
		}
	}

	private boolean isContainSection (File file, String section) {
		boolean isContainSection = false;
		try {
			// open word file
			XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(file));
			List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

			for (XWPFParagraph para : paragraphList) {
				if (para.getText().startsWith("#" + section + "#")) {
					isContainSection = true;
					break;
				}
			}
			xdoc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return isContainSection;
	}
}
