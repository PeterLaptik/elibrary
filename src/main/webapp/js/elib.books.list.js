const app = Vue.createApp({
    data() {
        return { bookList: bookList }
    },
    methods:{
        updateBooks: function(id) {
            let xhr = new XMLHttpRequest();
            xhr.open('GET',
            router.getBooksPath(id));
            xhr.send();
            xhr.callBackObject = this;
            xhr.onload = function () {
                let loadedData = JSON.parse(xhr.response);
                bookList.books = loadedData.books;
                this.callBackObject.$forceUpdate();
                updateCollapsibles();
            }
        }
    }
}).mount('#book-list');