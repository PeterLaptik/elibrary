var router = {
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
    }
};