function loginFailed() { // Shake the window
	var originalLeft = jq("$loginWin").position().left;
	var loginWin = jq("$loginWin");
	var previousBackgroundColor = loginWin.css('background-color');

	loginWin.animate({
		left : originalLeft - 25,
		backgroundColor : "red"
	}, 50).animate({
		left : originalLeft
	}, 50).animate({
		left : originalLeft + 25
	}, 50).animate({
		left : originalLeft
	}, 50).animate({
		backgroundColor : previousBackgroundColor
	}, 250, function() {
		loginWin.css('background-color', '');
	});
}