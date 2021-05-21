$(function(){
    let customer = getCustomer();
    let tempUserID = getCookie("tempUserID");
    let customerID = getCookie("customerID");
    $("#addressUpdated").hide();

    // if user is logged in - fill out the address-field automatically
    if(customerID!==""){
        $.get("/customers/"+customerID, function (Customer){
            console.log("Setting shipping info...");
            document.getElementById("firstname").value = Customer.firstname;
            document.getElementById("lastname").value = (Customer.lastname);
            document.getElementById("address").value = (Customer.address);
            document.getElementById("zipcode").value = (Customer.zipcode);
            document.getElementById("city").value = (Customer.city);
            document.getElementById("phone").value = (Customer.telephone);
            document.getElementById("email").value = (Customer.email);
        });
    }

    $("#registerOrderBtn").click(function(){

        // if no ID is set/ cookie with ID is deleted --> generate a new one
        if(customerID==="" && tempUserID==="") {
            deleteCookie("email");
            deleteCookie("customerID");
            let tempUserID = Math.random().toString(36).substring(7);
            setCookie("tempUserID", tempUserID, 1);
            let cookieTemp = getCookie("tempUserID");
            console.log("No cookie is set, making a new one and relocating: ", cookieTemp);
            location.reload();
        }

        else {
            let todaysDate = new Date();

            // generating Order and Ordercontent
            $.get("/orders/generateOrderID", function (orderID) {
                console.log("OrderID: " + orderID);
                setCookie("orderID", orderID, 1);

                //If user on website is not logged in, check for products in temporary cart
                if (customer.customerID === null || customer.customerID.length === 0) {
                    $.get("/getCartItems", {customerID: tempUserID}, function (products) {
                        const newOrder = {
                            orderID: orderID,
                            orderDate: todaysDate,
                            totalPrice: 0,
                            amount: products.length,
                            customerID: tempUserID
                        };

                        $.post("/orders/addOrder", newOrder, function (message) {
                            console.log(message);
                            if (message === "Order added!") {
                                $.post("/orders/addOrdercontent", {
                                    orderID: orderID,
                                    customerID: tempUserID
                                }, function (addOrdercontentMessage) {
                                    console.log(addOrdercontentMessage);
                                    window.location.href = "confirmation.html";
                                    $.post("/emptyCart", {customerID: tempUserID}, function (emptyCartMsg) {
                                        console.log(emptyCartMsg);

                                    });
                                });
                            }
                        });
                    });
                }

                //If user is logged in, get products from customers cart
                else {
                    // check if userID is valid (a registered customer) if customer is logged in
                    $.get("/customers/"+customerID, function(cust){
                        console.log(cust);
                        if(cust===""){
                            console.log("This cookie has been changed or deleted, Please log out and try again.")
                        }
                        else{
                            $.get("/getCartItems", {customerID: customer.customerID}, function (products) {
                                const newOrder = {
                                    orderID: orderID,
                                    orderDate: todaysDate,
                                    totalPrice: 0,
                                    amount: products.length,
                                    customerID: customer.customerID
                                };

                                $.post("/orders/addOrder", newOrder, function (message) {
                                    console.log(message);
                                    if (message === "Order added!") {
                                        $.post("/orders/addOrdercontent", {
                                            orderID: orderID,
                                            customerID: customer.customerID
                                        }, function (addOrderContentMessage) {
                                            console.log(addOrderContentMessage);
                                            $.post("/emptyCart", {customerID: customerID}, function (emptyCartMsg) {
                                                console.log(emptyCartMsg);
                                                window.location.href = "confirmation.html";
                                            });
                                        });
                                    }
                                });
                            });
                        }
                    });
                }
            });
        }
    });

    $("#updateAddress").click(function(){
        $("#addressUpdated").show();
    });
});