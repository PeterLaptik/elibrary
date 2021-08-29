package pl.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table (name="books")
public class Book implements Serializable {
	private static final long serialVersionUID = -2333712729066071996L;
	
	/** Web.xml parameter name:
	 *  keeps folder path to books storage.
	 *  Example:
	 *  	...
	 *  	<context-param>
	 *  		<param-name>fileUploadDirectory</param-name>
	 *  		<param-value>C:/tmp/</param-value>
	 *  	</context-param>
	 *  	...
	 **/
	public static final String BOOKS_VOLUME = "fileUploadDirectory";
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "book_name";
	public static final String FIELD_AUTHORS = "authors";
	public static final String FIELD_CODE_UDC = "book_code_udc";
	public static final String FIELD_CODE_ISBN = "book_code_isbn";
	public static final String FIELD_CODE_ISSN = "book_code_issn";
	public static final String FIELD_MAGAZINE = "magazine";
	public static final String FIELD_DESCR = "book_description";
	public static final String FIELD_PUBLISHER = "publisher";
	public static final String FIELD_CITY = "city";
	public static final String FIELD_YEAR = "publication_year";
	public static final String FIELD_FILE_FORMAT = "file_format";
	public static final String FIELD_FILE_NAME = "file_name";
	public static final String FIELD_FILE_CREATED = "created_date";
	
	@Id
	@SequenceGenerator(name = "book_seq",
						sequenceName = "SEQ_BOOK",
						allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "book_seq")
	@Column(name=FIELD_ID)
	private int id;
	
	@Column(name=FIELD_NAME)
	private String name = "";
	
	@Column(name=FIELD_DESCR, columnDefinition="TEXT")
	private String description = "";
	
	@Column(name=FIELD_FILE_FORMAT)
	private String format = "";
	
	@Column(name=FIELD_FILE_NAME)
	private String fileName = "";
	
	/** Denormalized **/
	@Column(name=FIELD_AUTHORS)
	private String authors = "";
	
	@Column(name=FIELD_CODE_UDC)
	private String codeUdc = "";
	
	@Column(name=FIELD_CODE_ISBN)
	private String codeIsbn = "";

	@Column(name=FIELD_CODE_ISSN)
	private String codeIssn = "";
	
	/** Attribute is only for articles: 
	 * name - article name, code - magazine issue **/
	@Column(name=FIELD_MAGAZINE)
	private String magazine = "";
	
	@Column(name=FIELD_PUBLISHER)
	private String publisher = "";
	
	@Column(name=FIELD_CITY)
	private String city = "";
	
	@Column(name=FIELD_YEAR)
	private int year = 0;
	
	@Column(name=FIELD_FILE_CREATED)
	private Date bookCreated;

	@ManyToOne(cascade=CascadeType.DETACH)
	private Section section;

	public Book() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public void setCodeUdc(String codeUdc) {
		this.codeUdc = codeUdc;
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
	
	public Date getBookCreated() {
		return bookCreated;
	}

	public void setBookCreated(Date bookCreated) {
		this.bookCreated = bookCreated;
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

	public int getYear() {
		return year;
	}

	public void setYear(short year) {
		this.year = year;
	}
	
	public JsonObjectBuilder toJsonBuilder() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("name", name);
		builder.add("description", description);
		builder.add("author", authors);
		builder.add("codeUdc", codeUdc);
		builder.add("codeIsbn", codeIsbn);
		builder.add("codeIssn", codeIssn);
		builder.add("format", format);
		builder.add("publisher", publisher);
		builder.add("city", city);
		builder.add("year", year);
		return builder;
	}
	
	public JsonObject toJsonObject() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("name", name);
		builder.add("description", description);
		builder.add("author", authors);
		builder.add("codeUdc", codeUdc);
		builder.add("codeIsbn", codeIsbn);
		builder.add("codeIssn", codeIssn);
		builder.add("format", format);
		builder.add("publisher", publisher);
		builder.add("city", city);
		builder.add("year", year);
		return builder.build();
	}
	
	@PrePersist
	public void bookCreated() {
		this.bookCreated = new Date();
	}
}
