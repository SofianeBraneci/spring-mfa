document.getElementById("btn-login").addEventListener("click", (e)=> {
    e.preventDefault()
    
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            [csrfHeader]: csrfToken
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`,
    })
        .then(response => console.log(response))
        .catch(error => console.error("Error adding item:", error))

})
