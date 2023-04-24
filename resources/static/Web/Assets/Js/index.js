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
            alerts: {
                email: '',
                password: ''
            },
            errorLogin:false,
            confirmPassword:"",
            errorPassCreate:false,
            errorUserCreate:false,
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
          
        // register() {
        //     if(this.postPassword == this.confirmPassword){ 
        //         axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.postEmail}&password=${this.postPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => {
        //             if(response.status == 201){
        //                 this.login();
        //             }
        //         }).catch(error=>this.errorUserCreate = true);
        //     }else{
        //         this.errorPassCreate = true;
        //     } 

        // }

        register() {
            axios.post('/api/clients', "firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.postEmail + "&password=" + this.postPassword, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                
                .then(() => {

                    axios.post('/api/login', "email=" + this.postEmail + "&password=" + this.postPassword, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(() => window.location.href = "/Web/accounts.html")

                })
                .catch(err => ('Datos Incorrectos ' + err))

        }
        
    },
})

app.mount('#app')