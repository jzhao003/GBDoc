package gbdoc.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class EditDoc {
	public void copyDocFromTemplate() {

	}

	public static void editTemplate(String[] inputValues) throws Exception {
		if (inputValues==null || inputValues.length==0) {
			return;
		}
		String oldFileName = "/Users/jzhao/work/workspace/myown/"
				+ "javaspace/GBDocGen/doc_template/new.docx";
		String newFileName = "/Users/jzhao/work/workspace/myown/javaspace/GBDocGen/doc_template/new1.docx";
		
		FileInputStream input = new FileInputStream(oldFileName);
		FileOutputStream output = new FileOutputStream(newFileName);
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(input));
		List<XWPFParagraph> paragraphList = xdoc.getParagraphs();
		
		int inputValueIndex = 0;
		for (XWPFParagraph paragraph : paragraphList) {
			List<XWPFRun> runs = paragraph.getRuns();
			for (int i=0;i<runs.size();i++) {
				if (runs.get(i).getColor()!=null && runs.get(i).getColor().equals("FF0000")) {
					// 去掉红色
					runs.get(i).setColor("000000");
					// 去掉原文字
					runs.get(i).setText("", 0);
					if (i<runs.size()&& runs.get(i+1).getColor()!=null && runs.get(i+1).getColor().equals("FF0000")) {
						continue;
					} else {
						runs.get(i).setText(inputValues[inputValueIndex]);
						inputValueIndex++;
					}
				}
			}
		}
		xdoc.write(output);
		xdoc.close();
		File file = new File(oldFileName);
		file.delete();
		File newFile = new File(newFileName);
		newFile.renameTo(file);
	}
	
	public static void editDoc(String inputValue) throws Exception {
		String oldFileName = "/Users/jzhao/work/workspace/myown/"
				+ "javaspace/GBDocGen/doc_template/new.docx";
		String newFileName = "/Users/jzhao/work/workspace/myown/javaspace/GBDocGen/doc_template/new1.docx";
		
		File file = new File(oldFileName);
		
		
		FileInputStream input = new FileInputStream(oldFileName);
		FileOutputStream output = new FileOutputStream(newFileName);
		
		
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(input));
		List<XWPFParagraph> paragraphList = xdoc.getParagraphs();
		
		for (XWPFParagraph paragraph : paragraphList) {
			for (XWPFRun rn : paragraph.getRuns()) {
				rn.setText("aaa");
			}
		}
		
		xdoc.write(output);
		
		xdoc.close();
		
		
		
		file.delete();
		File newFile = new File(newFileName);
		newFile.renameTo(file);
		
	}

	public static List<XWPFParagraph> getWordFileParagraph() throws InvalidFormatException, IOException {
		FileInputStream fis = new FileInputStream(
				"/Users/jzhao/work/workspace/myown/javaspace/GBDocGen/doc_template/new.docx");

		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		List<XWPFParagraph> paragraphList = xdoc.getParagraphs();
		xdoc.close();
		return paragraphList;
	}

	public static void readWordFile() throws InvalidFormatException, IOException {
		FileInputStream fis = new FileInputStream(
				"/Users/jzhao/work/workspace/myown/javaspace/GBDocGen/doc_template/new.docx");

		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);

		List<XWPFParagraph> paragraphList = xdoc.getParagraphs();
//		System.out.println(paragraphList.size());

		for (XWPFParagraph paragraph : paragraphList) {
			for (XWPFRun rn : paragraph.getRuns()) {

				System.out.println("color: " + rn.getColor());
//									System.out.println(rn.getFontSize());
				System.out.println("rn: " + rn);

//									rn.getColor();
			}

		}
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

	public static void main(String args[]) {
		String src = "/Users/jzhao/work/workspace/myown/javaspace/GBDocGen/doc_template/mytemplate.docx";
		try {
//			copyFileUsingStream(new File(src),new File("/Users/jzhao/work/workspace/myown/javaspace/GBDocGen/doc_template/new.docx"));
//			readWordFile();
			editDoc(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
