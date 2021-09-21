package pl.view.jsf.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import pl.model.dao.BookDao;
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
	private String authors;
	private String codeUdc;
	private String codeIsbn;
	private String codeIssn;
	private String magazine;
	private String publisher;
	private String publicationYear;

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}
	
	private String city;
	private Section section;
	
	private List<Book> selectedBooks = null;
	
	Section selectedSection = null;
	private UploadedFile file = null;
	
	@EJB
	private BookDao bookDao;// = new BookDaoImpl();

	private TreeNode node; 

	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}

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
	
	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getCodeUdc() {
		return codeUdc;
	}

	public void setCodeUdc(String code) {
		this.codeUdc = code;
	}

	public String getCodeIsbn() {
		return codeIsbn;
	}

	public void setCodeIsbn(String codeIsbn) {
		this.codeIsbn = codeIsbn;
	}

	public String getCodeIssn() {
		return codeIssn;
	}

	public void setCodeIssn(String codeIssn) {
		this.codeIssn = codeIssn;
	}
	
	public String getMagazine() {
		return magazine;
	}

	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}
	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public List<Book> getSelectedBooks() {
		return selectedBooks;
	}

	public void setSelectedBooks(List<Book> selectedBooks) {
		this.selectedBooks = selectedBooks;
	}

	public void deleteSelectedBook() {
		if(selectedBooks==null || selectedBooks.size()<1)
			return;
		
		try {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			String directory = externalContext.getInitParameter(Book.BOOKS_VOLUME);
			if (directory.charAt(directory.length()-1)!='\\' && directory.charAt(directory.length()-1)!='/')
				directory += File.separator;

			for(Book selectedBook: selectedBooks) {
				String filePath = directory + selectedBook.getFileName();
				File file = new File(filePath);
				bookDao.deleteBook(selectedBook);
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"Internal error on book deleting.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
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
	
	@SuppressWarnings("deprecation")
	public void appendBook() {
		if(file==null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Error!", 
					"No uploaded file...");
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
		String directory = externalContext.getInitParameter(Book.BOOKS_VOLUME);
		if (directory.charAt(directory.length()-1)!='\\' && directory.charAt(directory.length()-1)!='/')
			directory += File.separator;
		
		
		String fileName = generateFileName(file.getFileName(), directory);
		File targetFile = new File(directory + fileName);
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
		file = null;
		
		/** Make record in DB **/
		Book book = new Book();
		book.setName(name);
		book.setDescription(description);
		book.setFileName(fileName);
		book.setFormat(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
		book.setSection(section);
		book.setCodeUdc(codeUdc);
		book.setCodeIsbn(codeIsbn);
		book.setCodeIssn(codeIssn);
		book.setAuthors(authors);
		book.setMagazine(magazine);
		book.setPublisher(publisher);
		book.setCity(city);
		// Convert year
		Integer year = null;
		try {
			year = Integer.parseInt(publicationYear);
		} catch (Exception e) {
			System.err.println("Wrong year value: " + publicationYear);
			year = 0;
		}
		book.setYear(year.shortValue());
		
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
		} finally {
			setName("");
			setDescription("");
			book.setCodeUdc("");
			book.setCodeIsbn("");
			book.setCodeIssn("");
			book.setAuthors("");
			book.setMagazine("");
		}
	}
	
	/**
	 * Returns new file name on a volume.
	 * Names margin: 000000...1000000 + extension
	 * @param currentName - name and extension for extension extracting
	 * @param dir - directory
	 * @return - generated unique name
	 */
	private String generateFileName(String currentName, String dir) {
		Set<Integer> names = new TreeSet<Integer>();
		File directory = new File(dir);
		File[] list = directory.listFiles();
		for(File file: list) {
			if(file.isDirectory())
				continue;
			
			String name = file.getName();
			String idValue = name.substring(0, name.lastIndexOf("."));
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
		String generatedFileName = String.format("%06d", fileId)
				+ currentName.substring(currentName.lastIndexOf("."), currentName.length());
		
		return generatedFileName;
	}
}
