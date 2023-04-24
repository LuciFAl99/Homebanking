const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            cards: [],
            debitCards: [],
            creditCards: [],
            id: (new URLSearchParams(location.search)).get("id")
        }
    },
    created() {
        this.getCardsInfo()
    },
    methods: {
        getCardsInfo() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.cards = response.data.cards;
                    console.log(this.cards);
                    this.debitCards = this.cards.filter(card => card.type == "DEBITO");
                    console.log(this.debitCards);
                    this.creditCards = this.cards.filter(card => card.type == "CREDITO");
                    console.log(this.creditCards);


                })
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/Web/index.html")
        }
    }
})

app.mount("#app");
