const { createApp } = Vue

let app = createApp({
    data() {
        return{
            clientes : [],
            accounts: [],
            accountsExcludingO: [],
            radioTransfer:null,
            selectAccount:null,
            selectAccountTransferTo: '',
            amount: null,
            description: '',
            accountThirdD: '',
        }
    },
    mounted(){
        this.loadData()
    },
     methods:{
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
            .then((data) => {
                this.clientes = data.data
                this.accounts = this.clientes.accounts
                console.log(this.clientes);
            })
            .catch((error) => {

                console.log(error);

            })
        },

        sendTransfer(){
            console.log(this.amount);
            console.log(this.description);
            console.log(this.selectAccount);
            console.log(this.accountThirdD);
            axios.post('/api/clients/current/transactions',`amount=${this.amount}&description=${this.description}&accountOriginNumber=${this.selectAccount}&destinationAccountNumber=${this.accountThirdD}`)
            .then((response) =>  Swal.fire({
                icon: 'success',  
                text: 'La transacción fue exitosa',
               
              }
          
              ))
            .then(() => window.location.href = "/Web/accounts.html")
            .catch((error) => Swal.fire({
                icon: 'error',
                text: 'Algo anduvo mal!',
              }))
           
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/Web/index.html")
        },
        surePopUp(){
            Swal.fire({
                title: '¿Estás seguro de que quieres realizar esta transacción?',
                text: "La acción no se podrá revertir",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
              }).then((result) => {
                if (result.isConfirmed) {
                    this.sendTransfer()
                }
              })
        }
      

    },
    computed:{
        selectedAccount(){
            this.accountsExcludingO = this.accounts.filter(account => account.number != this.selectAccount)
            
        }

    },
    
}).mount('#app');