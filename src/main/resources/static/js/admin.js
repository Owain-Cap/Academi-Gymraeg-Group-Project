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

	// Set the form submission URL to the exact users delete url, like: admin/delete/5
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

/**
 * Filters the user table based on the selected role.
 */
function filterUsersByRole() {
	const filterValue = document.getElementById("roleFilter").value;
	// Get all rows in the table body
	const rows = document.querySelectorAll("table tbody tr");

	rows.forEach(row => {
		// The role is in the 6th column (index 5)
		const roleCell = row.cells[5];
		if (roleCell) {
			const role = roleCell.textContent.trim();
			// Show the row if the filter is "All" or if the role matches the filter value
			if (filterValue === "All" || role === filterValue) {
				row.style.display = "";
			} else {
				row.style.display = "none";
			}
		}
	});
}