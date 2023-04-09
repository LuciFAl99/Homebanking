const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            account: {},
            transactions: [],
            id: (new URLSearchParams(location.search)).get("id")
        };
    },

    created() {
        this.loadData();
    },

    methods: {
        loadData() {
            axios.get(`http://localhost:8080/api/accounts/` + this.id)
                .then(response => {
                    this.account = response.data;
                    this.sortTransactions();
                    console.log(this.transactions);
                })
                .catch(error => console.log(error));
        },

        sortTransactions() {
            this.transactions = this.account.transactions.sort((a, b) => b.id - a.id);
            this.transactions = this.transactions.reverse();
        }
    }
});

app.mount('#app');
