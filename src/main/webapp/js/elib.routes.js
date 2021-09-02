var router = {
	pathReader: 'book',
    applicationContextPath: 'res',
    pathSections: '/sections',
    pathBooks: '/books',
    
    getSectionsPath: function() {
        return this.applicationContextPath + this.pathSections;
    },
    
    getBooksList: function(sectionId){
        return this.getSectionsPath() + this.pathBooks + '/' + sectionId;
    },

    getBooksPage: function(sectionId, page){
        return this.getBooksList(sectionId) + '/' + page;
    },
	
	getReader: function(id, ext){
		return this.pathReader + '?id=' + id + "&ext=" + ext;
	},
	
	getBook: function(id){
		return this.applicationContextPath + this.pathBooks + '/' + id;
	}
};