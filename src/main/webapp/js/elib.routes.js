var router = {
    applicationContextPath: 'res/',
    pathSections: 'sections',
    pathBooks: '/books',
    getSectionsPath: function() {
        return this.applicationContextPath + this.pathSections;
    },
    getBooksPath: function(id){
        return this.getSectionsPath() + this.pathBooks + '/' + id;
    }
};