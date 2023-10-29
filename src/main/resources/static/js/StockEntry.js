/**
 * 
 */
window.onload = function() {
	let elemList = document.getElementById("transactType");

	elemList.addEventListener("click", onUsage);

}

function onUsage(event) {
	let elem = event.currentTarget;
	let customerInput = document.getElementById("customerInput");

	if (elem.options[elem.selectedIndex].text == "USAGE") {
		customerInput.innerHTML = "Customer: <input type=\"text\" th:field=\"*{customer}\" class=\"form-control\">";
	} else {
		customerInput.innerHTML = "Customer: <input type=\"text\" th:field=\"*{customer}\" class=\"form-control\" disabled>";
	}
}