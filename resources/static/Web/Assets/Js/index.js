const { createApp } = Vue

const app = createApp({
    data() {
        return {
            email: "",
            password: "",
            firstName: "",
            lastName: "",
            postEmail: "",
            postPassword: "",
            errorLogin:false,
            confirmPassword:"",
            errorPassCreate:false,
            errorMessage: '',
            errorEmailCreate:false
        }
    },
    methods: {
        login() {

          
            axios.post('/api/login', "email=" + this.email + "&password=" + this.password)
              .then(() => {
                if (this.email === "admin@admin.com") {
                  window.location.href = "/manager.html";
                } else {
                  window.location.href = "/Web/accounts.html";
                }
              })
              .catch(err=>this.errorLogin = true);
              
          },

        register() {
            
            axios.post('/api/clients', "firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.postEmail + "&password=" + this.postPassword, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                
                .then(() => {

                    axios.post('/api/login', "email=" + this.postEmail + "&password=" + this.postPassword,
                     { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(() => window.location.href = "/Web/accounts.html")

                })
                .catch(err => {
                  if (this.postEmail == this.email) {
                    this.errorMessage = 'El correo electrónico ya existe';
                  } else {
                    console.error(err);
                    this.errorMessage = 'Ha ocurrido un error al registrar el usuario';
                  }
                  if(this.postPassword !== this.confirmPassword){
                    this.errorPassCreate = true
                  }
                });    
        }
        
    },
})

app.mount('#app')