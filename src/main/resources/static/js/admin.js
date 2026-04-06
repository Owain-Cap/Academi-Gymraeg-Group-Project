// Get DOM references for the modal components
const modal = document.getElementById("deleteModal");
const form = document.getElementById("confirmDeleteForm");
const userNameSpan = document.getElementById("deleteUserName");

/**
 * Opens the delete confirmation modal.
 * Extracts the users specific action URL and username from the buttons data attributes,
 * updates the modals hidden form and text, and displays the modal.
 * 
 * @param {HTMLElement} button - The button element that was clicked
 */
function openDeleteModal(button) {
	// Retrieve data bound to the button using Thymeleaf
	const actionUrl = button.getAttribute("data-action");
	const username = button.getAttribute("data-username");

	// Set the form submission URL to the exact users delete url
	form.action = actionUrl;
	// Update the modal text to clearly show which user is being deleted
	userNameSpan.textContent = username;

	// Make the modal visible by setting its display property
	modal.style.display = "flex";
}

/**
 * Closes the delete confirmation modal by hiding it.
 */
function closeDeleteModal() {
	modal.style.display = "none";
}

// Global event listener to close the modal if the user clicks anywhere outside of the modal dialog box
window.onclick = function (event) {
	if (event.target == modal) {
		closeDeleteModal();
	}
}