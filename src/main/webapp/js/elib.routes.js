var router = {
    applicationContextPath: 'res/',
    pathSections: 'sections',
    pathBooks: '/books',
    getSectionsPath: function() {
        console.log('SECT:' + this.applicationContextPath + this.pathSections);
        return this.applicationContextPath + this.pathSections;
    },
    getBooksPath: function(id){
    	console.log('ADDR:' + this.getSectionsPath() + this.pathBooks + '/' + id);
        return this.getSectionsPath() + this.pathBooks + '/' + id;
    }
};