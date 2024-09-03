document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("login-form");

    loginForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Evita o envio padrão do formulário
        alert("TO FUNCIONANDO MEO");

        // Você pode adicionar lógica aqui para processar o login, se necessário
        // Se você deseja enviar o formulário após o alerta, remova a linha `event.preventDefault();`
        // ou chame `loginForm.submit();` após o alerta.
    });
});
