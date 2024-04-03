// Toggles the cart container to be visible when the cart icon is clicked
function toggleTable() {
    var table = document.getElementById("cart-container");
    table.classList.toggle("hidden");
}
const priceElements = document.querySelectorAll('.price-element');

// Formatting price amounts to be two decimal places
priceElements.forEach(element => {
    const price = parseFloat(element.textContent);
    element.textContent = '$' + price.toFixed(2);
});

// Front end validation for customer registration
$(document).ready(function () {
    $("#registrationForm").validate({
        rules: {
            username: {
                required: true,
                minlength: 4
            },
            password: {
                required: true,
                minlength: 8
            },
            email: {
                required: true,
                email: true
            },
            address: "required",
            fullName: "required",
            cardNumber: "required"
        },
        messages: {
            username: {
                required: "Please enter a username.",
                minlength: "Username must be at least 4 characters long."
            },
            password: {
                required: "Please enter a password.",
                minlength: "Password must be at least 8 characters long."
            },
            email: {
                required: "Please enter an email address.",
                email: "Please enter a valid email address."
            },
            address: "Please enter your address.",
            fullName: "Please enter your full name.",
            cardNumber: "Please enter your card number."
        },
        errorPlacement: function (error, element) {
            $("#validation-message").html(error);
        }
    });
})

// Validation for product registration
$(document).ready(function() {
    $("#productForm").validate({
      rules: {
        productName: "required",
        availableStock: {
          required: true,
          min: 0
        },
        imageURL: {
          required: true,
          url: true
        },
        price: {
          required: true,
          min: 0.01
        }
      },
      messages: {
        productName: "Please enter a product name.",
        availableStock: {
          required: "Please enter available stock quantity.",
          min: "Stock quantity cannot be negative."
        },
        imageURL: {
          required: "Please enter an image URL.",
          url: "Please enter a valid image URL."
        },
        price: {
          required: "Please enter a price.",
          min: "Price cannot be negative."
        }
      },
      errorPlacement: function(error, element) {
        $("#validation-message").html(error);
      }
    });
  });