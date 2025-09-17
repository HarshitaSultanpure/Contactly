console.log("this is script file")

const toggleSidebar = () => {
	if($(".sidebar").is(":visible")) {
		//if the sidebar is open then close it.....

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {
		//if sidebar is close show it.....
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};