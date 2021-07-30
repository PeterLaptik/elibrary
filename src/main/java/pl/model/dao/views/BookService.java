package pl.model.dao.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pl.model.dao.BookDao;
import pl.model.dao.impl.BookDaoImpl;
import pl.model.entities.Book;
import pl.model.entities.Section;

@Named("bookService")
@ViewScoped
public class BookService implements Serializable{
	private static final long serialVersionUID = -6832219154544980243L;
	private final static int MAX_BOOKS = 1000000;
	
	private String name;
	private String description;
	private String format;
	private String fileName;
	private Section section;
	Section selectedSection = null;
	private UploadedFile file = null;
	BookDao bookDao = new BookDaoImpl();


	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Section getSection() {
		return section;
	}
	
	public void setSection(Section section) {
		this.section = section;
	}
	
	public BookDao getBookDao() {
		return bookDao;
	}

	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	
	/**
	 * Uploads file.
	 * Append book to volume and database implements in 'appendBook'-method
	 * @param event - event from page
	 */
	public void uploadBook(FileUploadEvent event) {
		file = event.getFile();
        if(file!=null && file.getContents()!=null && file.getContents().length > 0 
        		&& file.getFileName()!=null) {
            FacesMessage msg = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Error!", 
						"Error occured on file loading...");
	        PrimeFaces.current().dialog().showMessageDynamic(message);
	        file = null;
	        return;
		}
	}
	
	public void appendBook() {
		if(file==null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"No uploaded file...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		if(description.equals("") || name.equals("")) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Input book name and description...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		if(section==null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Section is not chosen...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		if(section.getName().equals(Section.ROOT_NAME)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Books cannot be added to the root section...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		/**
		 * Write file from temporary directory to web.xml-defined volume.
		 * Direct write file does not work. The reason is shaded 'org.owasp.esapi.ESAPI'
		 */
		InputStream in = null;
		try {
			in = file.getInputstream();
		} catch (IOException e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Error on file processing...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			file = null;
			return;
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String directory = externalContext.getInitParameter("fileUploadDirectory");
		String fileName = generateFileName(file.getFileName(), directory);
		File targetFile = new File("C:\\Java\\tmp\\" + fileName);
		OutputStream out = null;
		try {
			out = new FileOutputStream(targetFile);
		} catch (FileNotFoundException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Error on file processing...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			e.printStackTrace();
			file = null;
			return;
		}

		int bytesRead;
		byte[] buffer = new byte[8 * 1024];
		try {
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Error on file processing...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			e.printStackTrace();
			file = null;
			return;
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(out);
		}
		setName("");
		setDescription("");
		file = null;
		
		
		Book book = new Book();
		book.setName(name);
		book.setDescription(description);
		book.setFileName(fileName);
		book.setFormat(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
		book.setSection(section);
		
		try {
			bookDao.createBook(book);
		} catch (Exception e) {
			e.printStackTrace();
			targetFile.delete();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Error on book writing to db...");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			e.printStackTrace();
		}
	}
	
	private String generateFileName(String currentName, String dir) {
		Set<Integer> names = new TreeSet<Integer>();
		File directory = new File(dir);
		File[] list = directory.listFiles();
		for(File file: list) {
			String name = file.getName();
			String idValue = name.substring(0, name.lastIndexOf(".")-1);
			System.out.println(name.substring(0, name.lastIndexOf(".")-1));
			try {
				Integer id = Integer.parseInt(idValue);
				names.add(id);
			} catch (Exception e) {
				// wrong existing file name
				// do nothing
			}
		}
		
		int fileId = 0;
		for(fileId=0; fileId<MAX_BOOKS; fileId++) {
			if(names.contains(fileId)==false)
				break;
		}
		return String.format("%06d", fileId) + "." 
				+ currentName.substring(currentName.lastIndexOf("."), currentName.length());
	}
}
