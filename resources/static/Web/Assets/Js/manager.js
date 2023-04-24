const { createApp } = Vue

const app = createApp({
  data() {
    return {
      clientes: [],
      firstName: "",
      lastName: "",
      email:"",
    }
  },
  created(){
    this.loadData()
  },
  methods: {
    loadData() {
      axios.get("http://localhost:8080/api/clients/")
        .then(response => {
          this.clientes = response.data;
          console.log(this.clientes)
        })
        .catch(error => console.log(error));
        
    },
    logout() {
        axios.post('/api/logout')
            .then(() => window.location.href = "/Web/index.html")
    },
    addClient() {
      this.postClient();
    },
    postClient() {
      const newClient = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email
      };
    
      axios.post('http://localhost:8080/rest/clients', newClient)
       .then(response => {
         // Agregar el nuevo cliente al array clientes
         this.clientes.push(response.data);
         // Limpiar los campos del formulario
         this.firstName = "";
         this.lastName = "";
         this.email = "";
       })
       .catch(error => console.log(error));
    }
    

  },
 
})

app.mount('#app')
