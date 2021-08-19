const app = Vue.createApp({
	data: function () {
		return {
			treeData: treeData,
		}
	},
	created: function () {
		this.fetchData();
	},

	methods: {
		makeFolder: function (item) {
			item.children = [];
			this.addItem(item);
		},
		addItem: function (item) {
			item.children.push({
				name: "new stuff"
			});
		},
		onChangeData: function (treeData) {
			console.log(JSON.stringify(treeData))
		},
		fetchData: function () {
			let xhr = new XMLHttpRequest();
			xhr.open('GET',
				applicationContextPath + pathSections);
			xhr.send();
			xhr.onload = function () {
				let x = treeData;
				if (treeData.children == undefined)
					treeData.children = [];

				let loadedData = JSON.parse(xhr.response);
				treeData.children = loadedData.children
			}
			return true;
		}
	}
})


let selectedItem = null;
app.provide('selectedItem', selectedItem);

app.component("tree-item", {
	template: '#item-template',
	isSelected: false,
	props: {
		item: Object
	},
	data: function () {
		return {
			hover: false,
			isOpen: false,
			style: this.getStyleClass()
		};
	},
	computed: {
		isFolder: function () {
			return this.item.children && this.item.children.length;
		}
	},
	methods: {
		toggle: function () {
			this.autoUpdate();

			if (this.isFolder) {
				this.isOpen = !this.isOpen;
			}

			if (selectedItem)
				selectedItem.$forceUpdate();

			selectedItem = this;
			selectedItem.$forceUpdate();
			console.log(this.item.id);
		},
		getStyleClass: function () {
			let style = "item";
			if (this.hover == true)
				style = "bold";

			if ((selectedItem) && (selectedItem.item.id == this.item.id))
				style = "selected";

			return style;
		},
		autoUpdate: function () {
			let x = this.item;
			let y = this.isFolder;

		}
	},
})

app.mount('#book-navigator')