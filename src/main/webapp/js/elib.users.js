// User info and settings
// Password change
app.loadUserInfo();

let setChangePassword = function() {
	app.message = '';
	app.changingPassword = true;
	app.changingName = false;
}

let undoSetPassword = function() {
	app.message = '';
	app.changingPassword = false;
	app.changingName = false;
}

let changePasswordOk = function() {
	let xhr = new XMLHttpRequest();

	let oldPass = document.getElementById('pass-input-old').value;
	let newPass = document.getElementById('pass-input-new').value;
	let newPassR = document.getElementById('pass-input-new-repeat').value;

	if (newPass != newPassR) {
		app.message = "Values of new password do not match!";
		return;
	}
	app.message = '';

	let object = { "oldPass": oldPass, "newPass": newPass };
	xhr.open('POST', router.getUserPath());
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify(object));
	xhr.callBackObject = this;
	
	xhr.onload = function() {
		let loadedData = xhr.response;
		app.message = loadedData;
	}
}

let changePasswordCancel = function() {
	app.changingPassword = false;
}

// User name change
let setChangeName = function() {
	app.message = '';
	app.changingPassword = false;
	app.changingName = true;
}

let undoChangeName = function() {
	app.message = '';
	app.changingPassword = false;
	app.changingName = false;
}


let setNewName = function() {
	let xhr = new XMLHttpRequest();
	let newName = document.getElementById('pass-input-name').value;

	if (!newName) {
		app.message = "Wrong value!";
		return;
	}
	app.message = '';

	let object = { "newName": newName };
	xhr.open('POST', router.getUserPath());
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify(object));
	xhr.callBackObject = this;

	xhr.onload = function() {
		let loadedData = xhr.response;
		app.loadUserInfo();
		app.message = loadedData;
	}
}