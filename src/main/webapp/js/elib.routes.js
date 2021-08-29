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
        console.log(this.getBooksList(sectionId) + '/' + page);
        return this.getBooksList(sectionId) + '/' + page;
    }
};