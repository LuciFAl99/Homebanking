const { createApp } = Vue;

const app = createApp({
  data() {
    return {
      clients: [],
      createdAccount: false
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
              console.log(this.clients);
            })
        },
        logout() {
          axios.post('/api/logout')
              .then(() => window.location.href = "/Web/BigWing/index.html")
      },
      calculateAmountWithoutInterest(amount) {
        return amount / 1.2;
      },
      createAccount(){
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
      }
       }
    })

app.mount("#app");
