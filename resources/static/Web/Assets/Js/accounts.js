const { createApp } = Vue;

const app = createApp({
  data() {
    return {
      clients: [],
      id: (new URLSearchParams(location.search)).get("id")
    };
  },
  created() {
    this.getClientInfo()
  },
  methods: {
           getClientInfo() {
             axios.get("http://localhost:8080/api/clients/current")
             .then(response => {
             this.clients = response.data;
              console.log(this.clients);
            })
        },
        logout() {
          axios.post('/api/logout')
              .then(() => window.location.href = "/Web/index.html")
      }
       }
    })

app.mount("#app");
