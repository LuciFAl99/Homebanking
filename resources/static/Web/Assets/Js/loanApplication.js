const { createApp } = Vue

let app = createApp({
  data() {
    return {
      clients: [],
      loans: [],
      accounts: [],
      loanSelected: [],
      selectedLoan: null,
      selectAccount: "",
      amount: 0,
      selectedPayment: 0,
      filteredPayments: [],
      totalCuotas:  this.amount/this.selectedPayment
    }
  },
  mounted() {
    this.loadData()
    this.selectLoan()
    this.selectedLoanObject()
  },
  methods: {
    loadData() {
      axios.get("http://localhost:8080/api/clients/current")
        .then((data) => {
          this.clients = data.data
          this.accounts = this.clients.accounts
        })
        .catch((error) => {
          console.log(error);
        })
      axios.get("http://localhost:8080/api/loans")
        .then((data) => {
          this.loans = data.data
          console.log(this.loans);
          console.log(this.selectedLoan);
        })
    },
    loadPayments() {
      if (this.selectedLoan) {
        const selectedLoan = this.loans.find(loan => loan.id === this.selectedLoan)
        console.log(selectedLoan);
        if (selectedLoan.payments && selectedLoan.payments.length > 0) {
          this.filteredPayments = selectedLoan.payments
        } else {
          this.filteredPayments = []
        }
      } else {
        this.filteredPayments = []
      }
      this.selectedPayment = null
    },
    getMaxAmount(loanId) {
      const loan = this.loans.find(loan => loan.id === loanId)
      return loan ? loan.maxAmount : 0
    },
    selectLoan() {
      this.selectedLoan = this.loans.find(loan => loan.id === parseInt(this.selectedLoan))
    },
    sendLoan() {
      console.log(this.selectedLoan);
      console.log(this.amount);
      console.log(this.selectedPayment);
      console.log(this.selectAccount);
      axios.post('http://localhost:8080/api/loans', {
        "loanId": this.selectedLoan,
        "amount": this.amount,
        "payments": this.selectedPayment,
        "destinationAccountNumber": this.selectAccount
      }
      )

        .then((response) => Swal.fire({
          icon: 'success',
          text: 'El préstamo se realizó correctamente',

        }

        ))
        .then(() => window.location.href = "/Web/accounts.html")
        .catch((error) => {
          console.log(error.response.data)
          Swal.fire({
            icon: 'error',
            text: 'Algo anduvo mal!',
          })
        })

    },
    logout() {
      axios.post('/api/logout')
        .then(() => window.location.href = "/Web/index.html")
    },
    surePopUp() {
      Swal.fire({
        title: '¿Estás seguro de que quieres realizar este préstamo?',
        text: 'Terminarás pagando el préstamo '+ this.amount * 1.2+'\n\n'+'Pagarás ' + this.selectedPayment + ' cuotas a '  + this.amount/this.selectedPayment + ' pesos',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes'
      }).then((result) => {
        if (result.isConfirmed) {
          this.sendLoan()
        }
      })
    }

  }
})

app.mount('#app')

