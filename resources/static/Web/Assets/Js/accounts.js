const { createApp } = Vue;

const app = createApp({
  data() {
    return {
      clients: [],
      createdAccount: false,
      accounts: [],
      deletedAccounts: []
    };
  },
  created() {
    this.getClientInfo()
  },
  methods: {
    getClientInfo() {
      axios.get("/api/clients/current")
        .then(response => {
          this.clients = response.data;
          this.accounts = this.clients.accounts.filter(account => account.active);
          console.log(this.accounts);
          console.log(this.clients);

        })
    },
    logout() {
      axios.post('/api/logout')
        .then(() => window.location.href = "/Web/BigWing/index.html")
    },
    calculateAmountWithoutInterest(amount) {
      return amount / 1.20;
    },
    createAccount() {
      axios.post('/api/clients/current/accounts')
        .then(response => {
          if (response.status == "201") {
            console.log(response),
              this.createdAccount = true
            this.getClientInfo()
          }
        })
        .catch(error => {
          console.log(error);
          if (error.code == "ERR_BAD_REQUEST") {
            console.log(error)
          }
        })
    },
    eliminarCuenta(id) {
      Swal.fire({
        title: '¿Estás seguro de que quieres eliminar esta cuenta?',
        text: "La acción no se podrá revertir",
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes',
        preConfirm: () => {
          return axios.put('/api/clients/current/accounts', `id=${id}`)
            .then(response => {
              Swal.fire({
                icon: 'success',
                text: 'La cuenta se eliminó correctamente',
                showConfirmButton: false,
                timer: 2000,
              }).then(() => {
                // Actualizar la lista de cuentas después de eliminar una cuenta
                this.getClientInfo();
                window.location.href = "/Web/accounts.html";
              });
            })
            .catch(error => {
              Swal.fire({
                icon: 'error',
                text: error.response.data,
                confirmButtonColor: "#7c601893",
              });
            });
        },
        allowOutsideClick: () => !Swal.isLoading()
      });
    },
    
  }
})

app.mount("#app");
