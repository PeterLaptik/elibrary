package pl.elibrary.route.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Following resources are acceptable:
 * res/sections
 * res/books/${section_id}
 * res/books/${section_id}/${page}
 * res/books/${book_id}
 * res/bookhistory/${book_id}
 * res/pagehistory/${page_id}
 * res/search/bookname
 * res/search/author
 * res/bookmarks/post
 * res/bookmarks/delete
 * res/user
 */
@ApplicationPath("/res")
public class ResourceApi extends Application {
	
}
